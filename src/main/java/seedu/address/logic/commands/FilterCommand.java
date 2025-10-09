package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.*;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters for relevant entries based on the specifications.\n"
            + "Parameters: category:\"description\" (must be an existing category)\n"
            + "Example: filter " + PREFIX_NAME + "\"alex yeoh\" " + PREFIX_ADDRESS + "\"Geylang Street\"";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Filter command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
