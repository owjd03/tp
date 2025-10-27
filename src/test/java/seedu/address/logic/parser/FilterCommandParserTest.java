package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.filter.FilterNumericalPrefixParser.MESSAGE_INVALID_NUMERICAL_FORMAT;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterDescriptivePrefixParser;
import seedu.address.logic.parser.filter.FilterMultiplePrefixParser;
import seedu.address.logic.parser.filter.FilterNumericalPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class FilterCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_preamble_throwsParseException() {
        assertParseFailure(parser, "some random string", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "some random string " + PREFIX_NAME + "Alice", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_emptyKeyword_throwsParseException() {
        // Prefix present but no keyword
        assertParseFailure(parser, " " + PREFIX_NAME + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + PREFIX_NAME));

        // Multiple prefixes, one with empty keyword
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + PREFIX_ADDRESS));

        // Tag prefix with empty keyword
        assertParseFailure(parser, " " + PREFIX_TAG + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + PREFIX_TAG));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String duplicateName = " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob";
        String expectedMessage = "Multiple values specified for the following single-valued field(s): n/";
        assertParseFailure(parser, duplicateName, expectedMessage);

        String duplicateAddress = " " + PREFIX_ADDRESS + "Main St " + PREFIX_ADDRESS + "North St";
        expectedMessage = "Multiple values specified for the following single-valued field(s): a/";
        assertParseFailure(parser, duplicateAddress, expectedMessage);

        String multipleDuplicates = " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + "Main St "
                + PREFIX_NAME + "Bob " + PREFIX_ADDRESS + "North St";
        expectedMessage = "Multiple values specified for the following single-valued field(s): n/ a/";
        assertParseFailure(parser, multipleDuplicates, expectedMessage);
    }

    @Test
    public void parse_validDescriptiveArgs_returnsFilterCommand() throws ParseException {
        // Single keyword
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alpha");
        parsers.add(nameParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha", expectedCommand);

        // Multiple keywords
        parsers.clear();
        nameParser = new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alpha");
        parsers.add(nameParser);
        FilterDescriptivePrefixParser addressParser =
                new FilterDescriptivePrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Beta");
        parsers.add(addressParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha " + PREFIX_ADDRESS + "Beta", expectedCommand);

        // Multi-word keyword
        parsers.clear();
        addressParser = new FilterDescriptivePrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Kent Ridge");
        parsers.add(addressParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "Kent Ridge", expectedCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFilterCommand() throws ParseException {
        // Single tag
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friend");
        parsers.add(tagParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend", expectedCommand);

        // Multiple tags
        parsers.clear();
        tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friend");
        tagParser.parse("colleague");
        parsers.add(tagParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "colleague", expectedCommand);

        // Duplicate tags - should be treated as one
        parsers.clear();
        tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friend");
        tagParser.parse("friend");
        parsers.add(tagParser);
        // We build the expected command with only one "friend" tag to confirm duplicate is handled
        List<FilterPrefixParser> expectedParsers = new ArrayList<>();
        FilterMultiplePrefixParser expectedTagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        expectedTagParser.parse("friend");
        expectedParsers.add(expectedTagParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(expectedParsers));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "friend", expectedCommand);
    }

    @Test
    public void parse_validNumericalArgs_returnsFilterCommand() throws ParseException {
        // Single numerical keyword (equals)
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse("50000");
        parsers.add(salaryParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_SALARY + "50000", expectedCommand);

        // Numerical with operator
        parsers.clear();
        FilterNumericalPrefixParser depParser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, p -> p.getDependents().getNumericValue());
        depParser.parse(">=2");
        parsers.add(depParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        assertParseSuccess(parser, " " + PREFIX_DEPENDENTS + ">=2", expectedCommand);
    }

    @Test
    public void parse_invalidNumericalArgs_throwsParseException() {
        // Non-numeric value
        assertParseFailure(parser, " " + PREFIX_SALARY + "abc", MESSAGE_INVALID_NUMERICAL_FORMAT);

        // Invalid operator
        assertParseFailure(parser, " " + PREFIX_SALARY + ">>500", MESSAGE_INVALID_NUMERICAL_FORMAT);

        // Decimal value for dependents
        assertParseFailure(parser,
                " " + PREFIX_DEPENDENTS + "2.5",
                "Dependents value must be an integer and cannot be a decimal.");
    }

    @Test
    public void parse_validMixedArgs_returnsFilterCommand() throws ParseException {
        List<FilterPrefixParser> parsers = new ArrayList<>();

        // Descriptive
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);

        // Numerical
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse("<120000");
        parsers.add(salaryParser);

        // Multiple
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friend");
        parsers.add(tagParser);

        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers));
        String userInput = " " + PREFIX_NAME + "Alice " + PREFIX_SALARY + "<120000 " + PREFIX_TAG + "friend";
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
