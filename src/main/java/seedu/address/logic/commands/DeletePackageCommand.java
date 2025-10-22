package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Deletes an insurance package from the address book.
 */
public class DeletePackageCommand extends Command {

    public static final String COMMAND_WORD = "deletep";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the insurance package identified by the name used in the displayed insurance package list.\n"
            + "Parameters: " + PREFIX_INSURANCE_NAME + "PACKAGE_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INSURANCE_NAME + "Gold";

    public static final String MESSAGE_DELETE_PACKAGE_SUCCESS = "Deleted Package: %1$s";
    public static final String MESSAGE_PACKAGE_NOT_FOUND = "This insurance package does not exist in the address book.";
    public static final String MESSAGE_PACKAGE_IN_USE =
            "This insurance package is assigned to one or more clients and cannot be deleted.";
    public static final String MESSAGE_CANNOT_DELETE_STANDARD_PACKAGE =
            "Standard insurance packages (Gold, Silver, Bronze, Undecided) cannot be deleted.";

    private static final Set<String> STANDARD_PACKAGES =
            new HashSet<>(Arrays.asList("Gold", "Silver", "Bronze", "Undecided"));

    private final String packageName;

    /**
     * @param packageName of the insurance package to be deleted
     */
    public DeletePackageCommand(String packageName) {
        requireNonNull(packageName);
        this.packageName = packageName;
    }

    /**
     * Executes the command to delete an insurance package from the address book.
     *
     * @param model {@code Model} which the command should operate on.
     * @return {@code CommandResult} representing the result of the command execution.
     * @throws CommandException If the package is a standard package, not found, or is currently in use by a client.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (STANDARD_PACKAGES.stream().anyMatch(s -> s.equalsIgnoreCase(packageName))) {
            throw new CommandException(MESSAGE_CANNOT_DELETE_STANDARD_PACKAGE);
        }

        Optional<InsurancePackage> packageToDelete = model.getFilteredInsurancePackageList()
                .stream()
                .filter(p -> p.getPackageName().equalsIgnoreCase(packageName))
                .findFirst();

        if (packageToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_PACKAGE_NOT_FOUND);
        }

        InsurancePackage actualPackage = packageToDelete.get();

        boolean isPackageInUse = model.getAddressBook().getPersonList().stream()
                .anyMatch(person -> person.getInsurancePackage().equals(actualPackage));

        if (isPackageInUse) {
            throw new CommandException(MESSAGE_PACKAGE_IN_USE);
        }

        model.deleteInsurancePackage(actualPackage);
        return new CommandResult(String.format(MESSAGE_DELETE_PACKAGE_SUCCESS, actualPackage.getPackageName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeletePackageCommand // instanceof handles nulls
                && packageName.equalsIgnoreCase(((DeletePackageCommand) other).packageName));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("packageName", packageName)
                .toString();
    }
}
