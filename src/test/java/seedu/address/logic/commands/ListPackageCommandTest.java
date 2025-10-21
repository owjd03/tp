package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ListPackageCommand.SHOWING_PACKAGE_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ViewData;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ListPackageCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_listPackage_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_PACKAGE_MESSAGE, false,
                new ViewData(false, null), true, false);
        assertCommandSuccess(new ListPackageCommand(), model, expectedCommandResult, expectedModel);
    }
}
