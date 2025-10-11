package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ViewCommand.MESSAGE_VIEW_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

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

    }

    @Test
    public void execute_validIndexInput_successPersonToView() {
        Person expectedPerson = expectedModel.getFilteredPersonList().get(0);
        CommandResult result = new CommandResult(MESSAGE_VIEW_SUCCESS, false,
                new ViewData(true, expectedPerson), false);
        ViewCommand command = new ViewCommand(INDEX_FIRST_PERSON);
        assertCommandSuccess(command, model, result, expectedModel);
    }


    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
