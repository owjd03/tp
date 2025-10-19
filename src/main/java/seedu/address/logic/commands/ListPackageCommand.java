package seedu.address.logic.commands;

import seedu.address.logic.ViewData;
import seedu.address.model.Model;

/**
 * Open the list of available insurance packages.
 */
public class ListPackageCommand extends Command {

    public static final String COMMAND_WORD = "listp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the available packages.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_PACKAGE_MESSAGE = "Opened package window.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_PACKAGE_MESSAGE, false, new ViewData(false, null),
                true, false);
    }
}
