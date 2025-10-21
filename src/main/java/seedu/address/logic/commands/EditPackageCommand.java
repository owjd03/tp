package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Edits the details of an existing insurance package in the address book.
 */
public class EditPackageCommand extends Command {

    public static final String COMMAND_WORD = "editp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the insurance package identified "
            + "by the index number used in the displayed insurance package list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_INSURANCE_NAME + "PACKAGE_NAME "
            + "[" + PREFIX_DESCRIPTION + "PACKAGE_DESC\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INSURANCE_NAME + "Edited Package Name "
            + PREFIX_DESCRIPTION + "This is an edited package description";

    public static final String MESSAGE_SUCCESS = "Edited insurance package: %1$s";
    public static final String MESSAGE_EDIT_PACKAGE_SUCCESS = "Edited Insurance Package: %1$s";

    private final String packageName;
    private final String editedPackageDesc;

    /**
     * Creates an EditPackageCommand to add the specified {@code InsurancePackage}
     */
    public EditPackageCommand(String packageName, String editedPackageDesc) {
        this.packageName = packageName;
        this.editedPackageDesc = editedPackageDesc;
    }


    /**
     * Executes the command to edit the description of an existing insurance package.
     * Finds the target package by its name (case-insensitive) and replaces it
     * with a new package containing the updated description.
     *
     * @param model {@code Model} which the command should operate on.
     * @return a {@code CommandResult} with the success message.
     * @throws CommandException if the package name does not exist in the model.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the target package by name by filtering (case-insensitive)
        InsurancePackage targetPackage = model.getFilteredInsurancePackageList().stream()
                .filter(pkg -> pkg.getPackageName().equalsIgnoreCase(packageName))
                .findFirst()
                .orElse(null);

        if (targetPackage == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PACKAGE);
        }

        InsurancePackage editedInsurancePackage = new InsurancePackage(packageName, editedPackageDesc);

        model.setInsurancePackage(targetPackage, editedInsurancePackage);
        return new CommandResult(String.format(MESSAGE_EDIT_PACKAGE_SUCCESS, Messages.format(editedInsurancePackage)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EditPackageCommand
                && packageName.equals(((EditPackageCommand) other).packageName)
                && editedPackageDesc.equals(((EditPackageCommand) other).editedPackageDesc));
    }
}
