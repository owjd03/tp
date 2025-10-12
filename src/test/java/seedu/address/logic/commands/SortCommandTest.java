package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_sort_success() {
        assertCommandSuccess(
                new SortCommand(SortField.NAME),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.NAME,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.PHONE),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.PHONE,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.EMAIL),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.EMAIL,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.ADDRESS),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.ADDRESS,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.SALARY),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.SALARY,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.DATEOFBIRTH),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.MARITALSTATUS),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.MARITALSTATUS,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.OCCUPATION),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION,
                expectedModel);

        assertCommandSuccess(
                new SortCommand(SortField.DEPENDENT),
                model,
                SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENT,
                expectedModel);
    }

    @Test
    public void equals() {
        SortCommand sortNameCommand = new SortCommand(SortField.NAME);
        SortCommand sortPhoneCommand = new SortCommand(SortField.PHONE);

        // same object -> returns true
        assertTrue(sortNameCommand.equals(sortNameCommand));

        // same values -> returns true
        SortCommand commandWithSameSortFieldValues = new SortCommand(SortField.NAME);
        assertTrue(sortNameCommand.equals(commandWithSameSortFieldValues));

        // different types -> returns false
        assertFalse(sortNameCommand.equals(1));

        // null -> returns false
        assertFalse(sortNameCommand.equals(null));

        // different sort field -> returns false
        assertFalse(sortNameCommand.equals(sortPhoneCommand));
    }

    @Test
    public void toStringMethod() {
        SortCommand sortCommand = new SortCommand(SortField.NAME);
        String expected = SortCommand.class.getCanonicalName() + "{sortField=" + SortField.NAME + "}";
        assertEquals(expected, sortCommand.toString());
    }
}
