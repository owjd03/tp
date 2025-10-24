package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.Model;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Salary;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_INSURANCE_PACKAGE + "INSURANCE_PACKAGE "
            + "[" + PREFIX_SALARY + "SALARY] "
            + "[" + PREFIX_DATE_OF_BIRTH + "DATE_OF_BIRTH] "
            + "[" + PREFIX_MARITAL_STATUS + "MARITAL_STATUS] "
            + "[" + PREFIX_DEPENDENTS + "NUMBER_OF_DEPENDENTS] "
            + "[" + PREFIX_OCCUPATION + "OCCUPATION] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_INSURANCE_PACKAGE + "Gold "
            + PREFIX_SALARY + "5000 "
            + PREFIX_DATE_OF_BIRTH + "1999-01-01 "
            + PREFIX_MARITAL_STATUS + "Single "
            + PREFIX_OCCUPATION + "Engineer "
            + PREFIX_DEPENDENTS + "2 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        String desiredPackageName = toAdd.getInsurancePackage().getPackageName();
        if (!InsuranceCatalog.isValidInsurancePackage(desiredPackageName)) {
            String validNamesString = InsuranceCatalog.getValidInsurancePackageNames();
            throw new CommandException("The insurance package '"
                    + desiredPackageName + "' does not exist.\n"
                    + "Available packages are: " + validNamesString);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

    /**
     * Stores the details to add the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class AddPersonDescriptor {
        private Salary salary;
        private DateOfBirth dateOfBirth;
        private MaritalStatus maritalStatus;
        private Occupation occupation;
        private Dependents dependents;

        public AddPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public AddPersonDescriptor(AddCommand.AddPersonDescriptor toCopy) {
            setSalary(toCopy.salary);
            setDateOfBirth(toCopy.dateOfBirth);
            setMaritalStatus(toCopy.maritalStatus);
            setOccupation(toCopy.occupation);
            setDependents(toCopy.dependents);
        }

        public void setSalary(Salary salary) {
            this.salary = salary;
        }

        public Optional<Salary> getSalary() {
            return Optional.ofNullable(salary);
        }

        public void setDateOfBirth(DateOfBirth dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Optional<DateOfBirth> getDateOfBirth() {
            return Optional.ofNullable(dateOfBirth);
        }

        public void setMaritalStatus(MaritalStatus maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public Optional<MaritalStatus> getMaritalStatus() {
            return Optional.ofNullable(maritalStatus);
        }

        public void setOccupation(Occupation occupation) {
            this.occupation = occupation;
        }

        public Optional<Occupation> getOccupation() {
            return Optional.ofNullable(occupation);
        }

        public void setDependents(Dependents dependents) {
            this.dependents = dependents;
        }

        public Optional<Dependents> getDependents() {
            return Optional.ofNullable(dependents);
        }
    }
}
