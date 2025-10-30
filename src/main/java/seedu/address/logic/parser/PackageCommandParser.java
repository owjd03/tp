package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddPackageCommand;
import seedu.address.logic.commands.DeletePackageCommand;
import seedu.address.logic.commands.EditPackageCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.insurance.InsurancePackage;


/**
 * A central parser for all commands related to Insurance Packages.
 * Provides static methods to parse arguments for add, edit, and delete package commands.
 */
public class PackageCommandParser {

    private static String parseAndValidatePackageName(ArgumentMultimap argMultimap) throws ParseException {
        String packageName = argMultimap.getValue(PREFIX_INSURANCE_PACKAGE).get().trim();
        if (packageName.isEmpty()) {
            throw new ParseException(InsurancePackage.MESSAGE_CONSTRAINTS);
        }
        return packageName;
    }

    private static String parseAndValidatePackageDescription(ArgumentMultimap argMultimap) {
        return argMultimap.getValue(PREFIX_DESCRIPTION).get().trim();
    }


    /**
     * Parses the given {@code String} of arguments in the context of the AddPackageCommand
     * and returns an AddPackageCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public static AddPackageCommand parseAddPackage(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = parseAndValidateAddEdit(args, AddPackageCommand.MESSAGE_USAGE);

        String packageName = parseAndValidatePackageName(argMultimap);
        String packageDescription = parseAndValidatePackageDescription(argMultimap);

        InsurancePackage insurancePackage = new InsurancePackage(packageName, packageDescription);
        return new AddPackageCommand(insurancePackage);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditPackageCommand
     * and returns an EditPackageCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public static EditPackageCommand parseEditPackage(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = parseAndValidateAddEdit(args, EditPackageCommand.MESSAGE_USAGE);

        String packageName = parseAndValidatePackageName(argMultimap);
        String packageDescription = parseAndValidatePackageDescription(argMultimap);

        return new EditPackageCommand(packageName, packageDescription);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePackageCommand
     * and returns a DeletePackageCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public static DeletePackageCommand parseDeletePackage(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INSURANCE_PACKAGE);

        if (!arePrefixesPresent(argMultimap, PREFIX_INSURANCE_PACKAGE) || !argMultimap.getPreamble().isEmpty()) {
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
     * A private helper to tokenize and validate arguments for Add and Edit commands.
     * Checks for presence of INSURANCE_PACKAGE and DESCRIPTION prefixes, empty preamble,
     * and no duplicate prefixes.
     *
     * @param args The arguments string.
     * @param messageUsage The usage message to display on error.
     * @return The parsed ArgumentMultimap.
     * @throws ParseException if validation fails.
     */
    private static ArgumentMultimap parseAndValidateAddEdit(String args, String messageUsage) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION);
        return argMultimap;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
