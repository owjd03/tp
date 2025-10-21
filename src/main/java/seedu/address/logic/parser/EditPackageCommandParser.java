package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.EditPackageCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new EditPackageCommand object
 */
public class EditPackageCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the EditPackageCommand
     * and returns an EditPackageCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditPackageCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INSURANCE_NAME, PREFIX_DESCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_INSURANCE_NAME, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPackageCommand.MESSAGE_USAGE));
        }

        String packageName = argMultimap.getValue(PREFIX_INSURANCE_NAME).get().trim();
        String packageDescription = argMultimap.getValue(PREFIX_DESCRIPTION).get().trim();

        if (packageName.isEmpty()) {
            throw new ParseException("Package name cannot be empty");
        }

        return new EditPackageCommand(packageName, packageDescription);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
