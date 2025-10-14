package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_DUPLICATE_NAME;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_NOVIEW_NAME;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_VIEW_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.ViewData;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

public class ViewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        final ViewCommand standardIndexCommand = new ViewCommand(INDEX_FIRST_PERSON);
        final ViewCommand standardPredicateCommand = new ViewCommand("Alice");

        // same values -> returns true
        ViewCommand commandWithSameIndexValues = new ViewCommand(INDEX_FIRST_PERSON);
        ViewCommand commandWithSamePredicateValues = new ViewCommand("Alice");
        assertTrue(standardIndexCommand.equals(commandWithSameIndexValues));
        assertTrue(standardPredicateCommand.equals(commandWithSamePredicateValues));

        // same object -> returns true
        assertTrue(standardIndexCommand.equals(standardIndexCommand));
        assertTrue(standardPredicateCommand.equals(standardPredicateCommand));

        // null -> returns false
        assertFalse(standardIndexCommand.equals(null));
        assertFalse(standardPredicateCommand.equals(null));

        // different types -> returns false
        assertFalse(standardIndexCommand.equals(new ClearCommand()));
        assertFalse(standardPredicateCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardIndexCommand.equals(new ViewCommand(INDEX_SECOND_PERSON)));

        // different predicate -> returns false
        assertFalse(standardPredicateCommand.equals(new ViewCommand("Bob")));

    }

    @Test
    public void execute_validIndexInput_successPersonToView() {
        Person expectedPerson = expectedModel.getFilteredPersonList().get(0);
        CommandResult result = new CommandResult(MESSAGE_VIEW_SUCCESS, false,
                new ViewData(true, expectedPerson), false);
        ViewCommand command = new ViewCommand(INDEX_FIRST_PERSON);
        assertCommandSuccess(command, model, result, expectedModel);
    }

    @Test
    public void execute_invalidIndexInput_throwCommandException() {
        model.updateFilteredPersonList(preparePredicate("Alice"));
        ViewCommand command = new ViewCommand(INDEX_THIRD_PERSON);
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                + ".\nTry input index based on the list you currently seeing";
        assertCommandFailure(command, model, expectedMessage);

    }

    @Test
    public void execute_validKeywords_successPersonToView() {
        Person expectedPerson = expectedModel.getFilteredPersonList().get(0);
        CommandResult result = new CommandResult(MESSAGE_VIEW_SUCCESS, false,
                new ViewData(true, expectedPerson), false);
        ViewCommand command = new ViewCommand("Alice");
        assertCommandSuccess(command, model, result, expectedModel);
    }

    @Test
    public void execute_zeroKeywords_throwCommandException() {
        String expectedMessage = String.format(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        ViewCommand command = new ViewCommand("");
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_randomKeywords_noPersonToView() {
        ViewCommand command = new ViewCommand("asdfasdf");
        assertCommandFailure(command, model, MESSAGE_NOVIEW_NAME);
    }

    @Test
    public void execute_generalKeywords_tooManyPersonToView() {
        ViewCommand command = new ViewCommand("a");
        assertCommandFailure(command, model, MESSAGE_DUPLICATE_NAME);
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
