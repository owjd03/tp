package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;

import java.util.stream.Stream;

import seedu.address.logic.commands.DeletePackageCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Parses input arguments and creates a new DeletePackageCommand object
 */
public class DeletePackageCommandParser implements Parser<DeletePackageCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePackageCommand
     * and returns a DeletePackageCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeletePackageCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INSURANCE_PACKAGE);

        if (!arePrefixesPresent(argMultimap, PREFIX_INSURANCE_PACKAGE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePackageCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INSURANCE_PACKAGE);
        String packageName = argMultimap.getValue(PREFIX_INSURANCE_PACKAGE)
                .orElseThrow(() -> new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeletePackageCommand.MESSAGE_USAGE)))
                .trim();

        if (packageName.isEmpty()) {
            throw new ParseException(InsurancePackage.MESSAGE_CONSTRAINTS);
        }

        return new DeletePackageCommand(packageName);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
