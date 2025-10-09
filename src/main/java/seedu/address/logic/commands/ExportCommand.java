package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Handles exporting contact details of the address book.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + "Allows for exporting of client contact details for external safekeeping.\n"
            + "Usage: export";


    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Export command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}