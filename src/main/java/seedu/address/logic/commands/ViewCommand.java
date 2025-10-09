package seedu.address.logic.commands;

import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * View the full details of a person in the address book (case-sensitive)
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person\n"
            + "Parameters: NAME or INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " alex"
            + "Example 2: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Here's the full detail!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
