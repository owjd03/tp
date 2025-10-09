package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_NOVIEW;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_VIEW_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class ViewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        final ViewCommand standardIndexCommand = new ViewCommand(INDEX_FIRST_PERSON);
        final ViewCommand standardNameCommand = new ViewCommand(preparePredicate("view Alice"));

        // same values -> returns true
        ViewCommand commandWithSameIndexValues = new ViewCommand(INDEX_FIRST_PERSON);
        assertTrue(standardIndexCommand.equals(commandWithSameIndexValues));

        // same object -> returns true
        assertTrue(standardIndexCommand.equals(standardIndexCommand));

        // null -> returns false
        assertFalse(standardIndexCommand.equals(null));

        // different types -> returns false
        assertFalse(standardIndexCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardIndexCommand.equals(new ViewCommand(INDEX_SECOND_PERSON)));

        // same predicate -> returns true
        assertTrue(standardNameCommand.equals(new ViewCommand(preparePredicate("view Alice"))));
    }

    @Test
    public void execute_zeroKeywords_noPersonToView() {
        String expectedMessage = String.format(MESSAGE_NOVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        ViewCommand command = new ViewCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_validIndexInput_successPersonToView() {
        String expectedMessage = String.format(MESSAGE_VIEW_SUCCESS);
        NameContainsKeywordsPredicate predicate = preparePredicate("view Alice");
        ViewCommand command = new ViewCommand(INDEX_FIRST_PERSON);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
