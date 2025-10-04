package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Changes the sort of an existing person in the address book.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from sort");
    }
}
