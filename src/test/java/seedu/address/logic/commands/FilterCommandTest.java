package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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
import seedu.address.logic.parser.filter.FilterDescriptivePrefixParser;
import seedu.address.logic.parser.filter.FilterMultiplePrefixParser;
import seedu.address.logic.parser.filter.FilterNumericalPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
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
        FilterDescriptivePrefixParser firstParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        firstParser.parse("first");
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(firstParser));

        // Setup second predicate
        FilterDescriptivePrefixParser secondParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        secondParser.parse("second");
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(secondParser));

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("NonExistentName");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleDescriptiveKeyword_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Benson");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleDescriptiveKeywords_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);
        FilterDescriptivePrefixParser phoneParser =
                new FilterDescriptivePrefixParser(PREFIX_PHONE, p -> p.getPhone().value);
        phoneParser.parse("94351253");
        parsers.add(phoneParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friends");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(tagParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleTags_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friends");
        tagParser.parse("owesMoney");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(tagParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_descriptiveAndTags_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        parsers.add(nameParser);
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friends");
        parsers.add(tagParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_salaryEquals_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse("7000");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(salaryParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_salaryGreaterThan_multiplePersonsFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse(">9999");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(salaryParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void execute_dependentsLessThanOrEqual_multiplePersonsFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 5);
        FilterNumericalPrefixParser depParser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, p -> p.getDependents().getNumericValue());
        depParser.parse("<=1");

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(depParser));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(model.getFilteredPersonList(), Arrays.asList(ALICE, CARL, DANIEL, ELLE, GEORGE));
    }

    @Test
    public void execute_numericalAndDescriptive_singlePersonFound() throws ParseException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        List<FilterPrefixParser> parsers = new ArrayList<>();
        FilterNumericalPrefixParser depParser = new
                FilterNumericalPrefixParser(PREFIX_DEPENDENTS, p -> p.getDependents().getNumericValue());
        depParser.parse(">=3");
        parsers.add(depParser);
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Fiona");
        parsers.add(nameParser);

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() throws ParseException {
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("keyword");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(nameParser));
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
