package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * View the full details of a person in the address book (case-sensitive)
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person\n"
            + "Parameters: NAME or INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " alex\n"
            + "Example 2: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d";

    private final Index index;

    /**
     * @param index of the person in the filtered person list to edit the remark
     */
    public ViewCommand(Index index) {
        requireAllNonNull(index);

        this.index = index;
    }

    public static final String MESSAGE_SUCCESS = "Here's the full detail!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(
                String.format(MESSAGE_ARGUMENTS, index.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand e = (ViewCommand) other;
        return index.equals(e.index);
    }
}
