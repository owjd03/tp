package seedu.address.logic.commands;

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
    }
}
