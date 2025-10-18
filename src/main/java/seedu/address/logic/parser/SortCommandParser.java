package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortDirection;
import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArguments = args.trim();

        if (trimmedArguments.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String[] argumentsArray = trimmedArguments.split("\\s+");
        String sortType = argumentsArray[0].toLowerCase();

        SortDirection direction = SortDirection.ASCENDING;

        if (argumentsArray.length > 1) {
            String directionOfSort = argumentsArray[1].toLowerCase();
            switch(directionOfSort) {
            case "ascending":
                direction = SortDirection.ASCENDING;
                break;
            case "descending":
                direction = SortDirection.DESCENDING;
                break;
            default:
                break;
            }
        }

        SortField field;
        switch(sortType) {
        case "name":
            field = SortField.NAME;
            break;
        case "phone":
            field = SortField.PHONE;
            break;
        case "email":
            field = SortField.EMAIL;
            break;
        case "address":
            field = SortField.ADDRESS;
            break;
        case "salary":
            field = SortField.SALARY;
            break;
        case "dateofbirth":
            field = SortField.DATEOFBIRTH;
            break;
        case "maritalstatus":
            field = SortField.MARITALSTATUS;
            break;
        case "occupation":
            field = SortField.OCCUPATION;
            break;
        case "dependent":
            field = SortField.DEPENDENT;
            break;
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(field, direction);
    }
}
