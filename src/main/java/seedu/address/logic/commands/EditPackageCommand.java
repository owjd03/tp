package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
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
            + "by the package name used in the displayed insurance package list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_INSURANCE_PACKAGE + "PACKAGE_NAME] "
            + "[" + PREFIX_DESCRIPTION + "PACKAGE_DESC]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_INSURANCE_PACKAGE + "Package to edit "
            + PREFIX_DESCRIPTION + "This is an edited description";

    public static final String MESSAGE_SUCCESS = "Edited insurance package: %1$s";
    public static final String MESSAGE_EDIT_PACKAGE_SUCCESS = "Edited Insurance Package: %1$s";

    private static final Logger logger = LogsCenter.getLogger(EditPackageCommand.class);

    private final String packageName;
    private final String editedPackageDesc;

    /**
     * Creates an EditPackageCommand to add the specified {@code InsurancePackage}
     */
    public EditPackageCommand(String packageName, String editedPackageDesc) {
        requireNonNull(packageName);
        requireNonNull(editedPackageDesc);
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
        InsurancePackage targetPackage = model.getInsuranceCatalog().getInsurancePackageList().stream()
                .filter(pkg -> pkg.getPackageName().equalsIgnoreCase(packageName)).findFirst().orElse(null);

        if (targetPackage == null) {
            logger.warning("Invalid package specified for edit: " + packageName);
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("packageName", packageName)
                .add("newDescription", editedPackageDesc)
                .toString();
    }
}
