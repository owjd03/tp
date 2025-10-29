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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void equals() throws ParseException {
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
    public void execute_zeroKeywords_noPersonFound() throws ParseException {
        String userInput = "n/NonExistentName";
        String expectedMessage = MESSAGE_NO_PERSONS_FOUND + "\nCommand: filter " + userInput;
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("NonExistentName");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand command = new FilterCommand(predicate, userInput);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleDescriptiveKeyword_singlePersonFound() throws ParseException {
        String userInput = "n/Benson";
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter " + userInput;
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Benson");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand command = new FilterCommand(predicate, userInput);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleDescriptiveKeywords_singlePersonFound() throws ParseException {
        String userInput = "n/Alice p/94351253";
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter " + userInput;
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);
        FilterContainsPrefixParser phoneParser =
                new FilterContainsPrefixParser(PREFIX_PHONE, p -> p.getPhone().value);
        phoneParser.parse("94351253");
        parsers.add(phoneParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate, userInput);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() throws ParseException {
        String userInput = "t/friends";
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3) + "\nCommand: filter " + userInput;
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friends");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(tagParser));
        FilterCommand command = new FilterCommand(predicate, "t/friends");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleTags_singlePersonFound() throws ParseException {
        String userInput = "t/friends t/owesMoney";
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter " + userInput;
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friends");
        tagParser.parse("owesMoney");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(tagParser));
        FilterCommand command = new FilterCommand(predicate, "t/friends t/owesMoney");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_descriptiveAndTags_singlePersonFound() throws ParseException {
        String userInput = "n/Alice t/friends";
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter " + userInput;
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friends");
        parsers.add(tagParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate, "n/Alice t/friends");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_salaryEquals_singlePersonFound() throws ParseException {
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter s/7000";
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY,
                        p -> p.getSalary().getNumericValue(),
                        p -> p.getSalary().isUnspecified());
        salaryParser.parse("7000");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(salaryParser));
        FilterCommand command = new FilterCommand(predicate, "s/7000");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_salaryGreaterThan_multiplePersonsFound() throws ParseException {
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2) + "\nCommand: filter s/>9999";
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY,
                        p -> p.getSalary().getNumericValue(),
                        p -> p.getSalary().isUnspecified());
        salaryParser.parse(">9999");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(salaryParser));
        FilterCommand command = new FilterCommand(predicate, "s/>9999");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void execute_dependentsLessThanOrEqual_multiplePersonsFound() throws ParseException {
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 5) + "\nCommand: filter dep/<=1";
        FilterComparisonPrefixParser depParser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS,
                        p -> p.getDependents().getNumericValue(),
                        p -> p.getDependents().isUnspecified());
        depParser.parse("<=1");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(depParser));
        FilterCommand command = new FilterCommand(predicate, "dep/<=1");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(model.getFilteredPersonList(), Arrays.asList(ALICE, CARL, DANIEL, ELLE, GEORGE));
    }

    @Test
    public void execute_numericalAndDescriptive_singlePersonFound() throws ParseException {
        String expectedMessage =
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1) + "\nCommand: filter dep/>=3 n/Fiona";
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterComparisonPrefixParser depParser = new
                FilterComparisonPrefixParser(PREFIX_DEPENDENTS,
                p -> p.getDependents().getNumericValue(),
                p -> p.getDependents().isUnspecified());
        depParser.parse(">=3");
        parsers.add(depParser);
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Fiona");
        parsers.add(nameParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate, "dep/>=3 n/Fiona");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() throws ParseException {
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("keyword");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand filterCommand = new FilterCommand(predicate, "n/keyword");
        String expected = FilterCommand.class.getCanonicalName()
                + "{predicate=" + predicate + ", "
                + "args=" + "n/keyword" + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
