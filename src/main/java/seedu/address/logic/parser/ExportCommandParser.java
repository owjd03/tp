package seedu.address.logic.parser;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     * Export command does not take any arguments for now.
     */
    public ExportCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
        return new ExportCommand();
    }
}
