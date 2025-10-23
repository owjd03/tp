package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Adds an insurance package to address book
 */
public class AddPackageCommand extends Command {

    public static final String COMMAND_WORD = "addp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a insurance package to the address book. "
            + "Parameters: "
            + PREFIX_INSURANCE_NAME + "PACKAGE_NAME "
            + PREFIX_DESCRIPTION + "PACKAGE_DESC\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INSURANCE_NAME + "Budget Package "
            + PREFIX_DESCRIPTION + "Classic coverages for your basic needs at an affordable price point";

    public static final String MESSAGE_SUCCESS = "New insurance package added: %1$s";
    public static final String MESSAGE_DUPLICATE_PACKAGE = "This insurance package already exists in the address book";

    private final InsurancePackage toAdd;

    /**
     * Creates an AddPackageCommand to add the specified {@code InsurancePackage}
     */
    public AddPackageCommand(InsurancePackage insurancePackage) {
        requireNonNull(insurancePackage);
        toAdd = insurancePackage;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasInsurancePackage(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PACKAGE);
        }

        model.addInsurancePackage(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPackageCommand)) {
            return false;
        }

        AddPackageCommand otherAddPackageCommand = (AddPackageCommand) other;
        return toAdd.equals(otherAddPackageCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
