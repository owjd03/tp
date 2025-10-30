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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterComparisonPrefixParser;
import seedu.address.logic.parser.filter.FilterContainsPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.logic.parser.filter.FilterTagParser;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Contains unit tests for {@code FilterCommandParser}.
 */
public class FilterCommandParserTest {

    private static final Function<Person, Double> GET_SALARY_DOUBLE =
            p -> p.getSalary().getNumericValue();
    private static final Function<Person, Double> GET_DEPENDENTS_DOUBLE =
            p -> p.getDependents().getNumericValue();
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
        String userInput1 = " " + PREFIX_NAME + "Alpha";
        FilterCommand expectedCommand1 = createExpectedFilterCommand(userInput1,
                createNameContainsParser("Alpha"));
        assertParseSuccess(parser, userInput1, expectedCommand1);

        // Multiple keywords
        String userInput2 = " " + PREFIX_NAME + "Alpha " + PREFIX_ADDRESS + "Beta";
        FilterCommand expectedCommand2 = createExpectedFilterCommand(userInput2,
                createNameContainsParser("Alpha"),
                createAddressContainsParser("Beta"));
        assertParseSuccess(parser, userInput2, expectedCommand2);

        // Multi-word keyword
        String userInput3 = " " + PREFIX_ADDRESS + "Kent Ridge";
        FilterCommand expectedCommand3 = createExpectedFilterCommand(userInput3,
                createAddressContainsParser("Kent Ridge"));
        assertParseSuccess(parser, userInput3, expectedCommand3);
    }

    @Test
    public void parse_validTagArgs_returnsFilterCommand() throws ParseException {
        // Single tag
        String userInput1 = " " + PREFIX_TAG + "friend";
        FilterCommand expectedCommand1 = createExpectedFilterCommand(userInput1,
                createTagParser("friend"));
        assertParseSuccess(parser, userInput1, expectedCommand1);

        // Multiple tags
        String userInput2 = " " + PREFIX_TAG + "friend " + PREFIX_TAG + "colleague";
        FilterCommand expectedCommand2 = createExpectedFilterCommand(userInput2,
                createTagParser("friend", "colleague"));
        assertParseSuccess(parser, userInput2, expectedCommand2);
    }

    @Test
    public void parse_validNumericalArgs_returnsFilterCommand() throws ParseException {
        // Single numerical keyword (equals)
        String userInput1 = " " + PREFIX_SALARY + "50000";
        FilterCommand expectedCommand1 = createExpectedFilterCommand(userInput1,
                createSalaryComparisonParser("50000"));
        assertParseSuccess(parser, userInput1, expectedCommand1);

        // Numerical with operator
        String userInput2 = " " + PREFIX_DEPENDENTS + ">=2";
        FilterCommand expectedCommand2 = createExpectedFilterCommand(userInput2,
                createDependentsComparisonParser(">=2"));
        assertParseSuccess(parser, userInput2, expectedCommand2);
    }

    @Test
    public void parse_invalidNumericalArgs_throwsParseException() {
        // Invalid value format for salary
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
        String userInput = " " + PREFIX_NAME + "Alice " + PREFIX_SALARY + "<120000 " + PREFIX_TAG + "friend";
        FilterCommand expectedCommand = createExpectedFilterCommand(userInput,
                createNameContainsParser("Alice"),
                createSalaryComparisonParser("<120000"),
                createTagParser("friend"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    //----- Helper Methods -----

    /**
     * Creates an expected {@code FilterCommand} with the given user input and an array of {@code FilterPrefixParser}.
     */
    private FilterCommand createExpectedFilterCommand(String userInput, FilterPrefixParser... parsers) {
        List<FilterPrefixParser> parserList = Arrays.stream(parsers).collect(Collectors.toList());
        return new FilterCommand(new PersonContainsKeywordsPredicate(parserList), userInput.trim());
    }

    private FilterContainsPrefixParser createNameContainsParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse(keyword);
        return parser;
    }

    private FilterContainsPrefixParser createAddressContainsParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        parser.parse(keyword);
        return parser;
    }

    private FilterComparisonPrefixParser createSalaryComparisonParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(
                PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse(keyword);
        return parser;
    }

    private FilterComparisonPrefixParser createDependentsComparisonParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(
                PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse(keyword);
        return parser;
    }

    private FilterTagParser createTagParser(String... keywords) throws ParseException {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        for (String keyword : keywords) {
            parser.parse(keyword);
        }
        return parser;
    }
}
