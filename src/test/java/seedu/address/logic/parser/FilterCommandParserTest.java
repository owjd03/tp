package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FilterCommandParser.MESSAGE_MISSING_KEYWORDS;
import static seedu.address.logic.parser.filter.FilterComparisonPrefixParser.MESSAGE_DEPENDENTS_MUST_BE_INTEGER;
import static seedu.address.logic.parser.filter.FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY;
import static seedu.address.logic.parser.filter.FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FOR_OPERATOR;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterComparisonPrefixParser;
import seedu.address.logic.parser.filter.FilterContainsPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.logic.parser.filter.FilterTagParser;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class FilterCommandParserTest {

    private static final Function<Person, Double> GET_SALARY_DOUBLE =
            p -> p.getSalary().getNumericValue();
    private static final Function<Person, Double> GET_DEPENDENTS_DOUBLE =
            p -> (double) p.getDependents().getNumericValue();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED =
            p -> p.getSalary().isUnspecified();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

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
                String.format(MESSAGE_MISSING_KEYWORDS, "prefix", PREFIX_NAME));

        // Multiple prefixes, one with empty keyword
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + " ",
                String.format(MESSAGE_MISSING_KEYWORDS, "prefix", PREFIX_ADDRESS));

        // Multiple prefixes, both with empty keywords
        assertParseFailure(parser, " " + PREFIX_NAME + " " + PREFIX_ADDRESS + " ",
                String.format(MESSAGE_MISSING_KEYWORDS, "prefixes", PREFIX_NAME + ", " + PREFIX_ADDRESS));

        // Tag prefix with empty keyword
        assertParseFailure(parser, " " + PREFIX_TAG + " ",
                String.format(MESSAGE_MISSING_KEYWORDS, "prefix", PREFIX_TAG));
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
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alpha");
        parsers.add(nameParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "n/Alpha");
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha", expectedCommand);

        // Multiple keywords
        parsers.clear();
        nameParser = new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alpha");
        parsers.add(nameParser);
        FilterContainsPrefixParser addressParser =
                new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Beta");
        parsers.add(addressParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "n/Alpha a/Beta");
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha " + PREFIX_ADDRESS + "Beta", expectedCommand);

        // Multi-word keyword
        parsers.clear();
        addressParser = new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Kent Ridge");
        parsers.add(addressParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "a/Kent Ridge");
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "Kent Ridge", expectedCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFilterCommand() throws ParseException {
        // Single tag
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friend");
        parsers.add(tagParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "t/friend");
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend", expectedCommand);

        // Multiple tags
        parsers.clear();
        tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friend");
        tagParser.parse("colleague");
        parsers.add(tagParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "t/friend t/colleague");
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "colleague", expectedCommand);

        // Duplicate tags - should be treated as one
        parsers.clear();
        tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friend");
        tagParser.parse("friend");
        parsers.add(tagParser);

        // We build the expected command with only one "friend" tag to confirm duplicate is handled
        List<FilterPrefixParser> expectedParsers = new ArrayList<>();
        FilterTagParser expectedTagParser = new FilterTagParser(PREFIX_TAG);
        expectedTagParser.parse("friend");
        expectedParsers.add(expectedTagParser);
        expectedCommand = new FilterCommand(
                new PersonContainsKeywordsPredicate(expectedParsers), "t/friend t/friend");
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "friend", expectedCommand);
    }

    @Test
    public void parse_validNumericalArgs_returnsFilterCommand() throws ParseException {
        // Single numerical keyword (equals)
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser.parse("50000");
        parsers.add(salaryParser);
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "s/50000");
        assertParseSuccess(parser, " " + PREFIX_SALARY + "50000", expectedCommand);

        // Numerical with operator
        parsers.clear();
        FilterComparisonPrefixParser depParser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        depParser.parse(">=2");
        parsers.add(depParser);
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "dep/>=2");
        assertParseSuccess(parser, " " + PREFIX_DEPENDENTS + ">=2", expectedCommand);
    }

    @Test
    public void parse_invalidNumericalArgs_throwsParseException() {
        // Invalid value
        assertParseFailure(parser, " " + PREFIX_SALARY + "=50e10",
                String.format(MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY, "50e10"));
        assertParseFailure(parser, " " + PREFIX_SALARY + ">.12",
                String.format(MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY, ".12"));

        // Invalid operator
        assertParseFailure(parser, " " + PREFIX_SALARY + ">>500",
                String.format(MESSAGE_INVALID_NUMBER_FOR_OPERATOR, ">500"));

        // Decimal value for dependents
        assertParseFailure(parser, " " + PREFIX_DEPENDENTS + "=2.5", MESSAGE_DEPENDENTS_MUST_BE_INTEGER);
    }

    @Test
    public void parse_validMixedArgs_returnsFilterCommand() throws ParseException {
        List<FilterPrefixParser> parsers = new ArrayList<>();

        // Descriptive
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);

        // Numerical
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser.parse("<120000");
        parsers.add(salaryParser);

        // Multiple
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friend");
        parsers.add(tagParser);

        FilterCommand expectedCommand =
                new FilterCommand(new PersonContainsKeywordsPredicate(parsers), "n/Alice s/<120000 t/friend");
        String userInput = " " + PREFIX_NAME + "Alice " + PREFIX_SALARY + "<120000 " + PREFIX_TAG + "friend";
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
