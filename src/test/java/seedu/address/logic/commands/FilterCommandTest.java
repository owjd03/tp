package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FilterCommand.MESSAGE_NO_PERSONS_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterComparisonPrefixParser;
import seedu.address.logic.parser.filter.FilterContainsPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.logic.parser.filter.FilterTagParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());
    }

    @Test
    public void equals_variousScenarios_correctResults() throws ParseException {
        // Setup first predicate
        FilterContainsPrefixParser firstParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        firstParser.parse("first");
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(firstParser));

        // Setup second predicate
        FilterContainsPrefixParser secondParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        secondParser.parse("second");
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(secondParser));

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate, "n/first");
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate, "n/second");

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate, "n/first");
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));

        // different args
        assertFalse(filterFirstCommand.equals(new FilterCommand(firstPredicate, "n/second")));
    }

    @Test
    public void toString_validCommand_correctStringRepresentation() throws ParseException {
        FilterContainsPrefixParser nameParser = createNameParser("keyword");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand filterCommand = new FilterCommand(predicate, "n/keyword");
        String expected = FilterCommand.class.getCanonicalName()
                + "{predicate=" + predicate + ", "
                + "args=" + "n/keyword" + "}";
        assertEquals(expected, filterCommand.toString());
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() throws ParseException {
        String userInput = "n/NonExistentName";
        FilterContainsPrefixParser nameParser = createNameParser("NonExistentName");
        assertFilterSuccess(userInput, Collections.emptyList(), nameParser);
    }

    @Test
    public void execute_singleDescriptiveKeyword_singlePersonFound() throws ParseException {
        String userInput = "n/Benson";
        FilterContainsPrefixParser nameParser = createNameParser("Benson");
        assertFilterSuccess(userInput, Collections.singletonList(BENSON), nameParser);
    }

    @Test
    public void execute_multipleDescriptiveKeywords_singlePersonFound() throws ParseException {
        String userInput = "n/Alice p/94351253";
        FilterContainsPrefixParser nameParser = createNameParser("Alice");
        FilterContainsPrefixParser phoneParser = createPhoneParser("94351253");
        assertFilterSuccess(userInput, Collections.singletonList(ALICE), nameParser, phoneParser);
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() throws ParseException {
        String userInput = "t/friends";
        FilterTagParser tagParser = createTagParser("friends");
        assertFilterSuccess(userInput, Arrays.asList(ALICE, BENSON, DANIEL), tagParser);
    }

    @Test
    public void execute_multipleTags_singlePersonFound() throws ParseException {
        String userInput = "t/friends t/owesMoney";
        FilterTagParser tagParser = createTagParser("friends", "owesMoney");
        assertFilterSuccess(userInput, Collections.singletonList(BENSON), tagParser);
    }

    @Test
    public void execute_descriptiveAndTags_singlePersonFound() throws ParseException {
        String userInput = "n/Alice t/friends";
        FilterContainsPrefixParser nameParser = createNameParser("Alice");
        FilterTagParser tagParser = createTagParser("friends");
        assertFilterSuccess(userInput, Collections.singletonList(ALICE), nameParser, tagParser);
    }

    @Test
    public void execute_salaryEquals_singlePersonFound() throws ParseException {
        String userInput = "s/7000";
        FilterComparisonPrefixParser salaryParser = createSalaryParser("7000");
        assertFilterSuccess(userInput, Collections.singletonList(CARL), salaryParser);
    }

    @Test
    public void execute_salaryGreaterThan_multiplePersonsFound() throws ParseException {
        String userInput = "s/>9999";
        FilterComparisonPrefixParser salaryParser = createSalaryParser(">9999");
        assertFilterSuccess(userInput, Arrays.asList(FIONA, GEORGE), salaryParser);
    }

    @Test
    public void execute_dependentsLessThanOrEqual_multiplePersonsFound() throws ParseException {
        String userInput = "dep/<=1";
        FilterComparisonPrefixParser depParser = createDependentsParser("<=1");
        assertFilterSuccess(userInput, Arrays.asList(ALICE, CARL, DANIEL, ELLE, GEORGE), depParser);
    }

    @Test
    public void execute_numericalAndDescriptive_singlePersonFound() throws ParseException {
        String userInput = "dep/>=3 n/Fiona";
        FilterComparisonPrefixParser depParser = createDependentsParser(">=3");
        FilterContainsPrefixParser nameParser = createNameParser("Fiona");
        assertFilterSuccess(userInput, Collections.singletonList(FIONA), depParser, nameParser);
    }

    //----- Helper Methods -----

    /**
     * Asserts that the execution of a {@code FilterCommand} with the given parsers is successful.
     */
    private void assertFilterSuccess(String userInput, List<Person> expectedFilteredList,
                                     FilterPrefixParser... parsers) {
        List<FilterPrefixParser> parserList = Arrays.stream(parsers).collect(Collectors.toList());
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parserList);
        FilterCommand command = new FilterCommand(predicate, userInput);

        String expectedMessage;
        if (expectedFilteredList.isEmpty()) {
            expectedMessage = MESSAGE_NO_PERSONS_FOUND + "\nCommand: filter " + userInput;
        } else {
            expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedFilteredList.size())
                    + "\nCommand: filter " + userInput;
        }

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedFilteredList, model.getFilteredPersonList());
    }

    private FilterContainsPrefixParser createNameParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse(keyword);
        return parser;
    }

    private FilterContainsPrefixParser createPhoneParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_PHONE, p -> p.getPhone().value);
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

    private FilterComparisonPrefixParser createSalaryParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(PREFIX_SALARY,
                p -> p.getSalary().toString(),
                p -> p.getSalary().isUnspecified());
        parser.parse(keyword);
        return parser;
    }

    private FilterComparisonPrefixParser createDependentsParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(PREFIX_DEPENDENTS,
                p -> p.getDependents().toString(),
                p -> p.getDependents().isUnspecified());
        parser.parse(keyword);
        return parser;
    }
}
