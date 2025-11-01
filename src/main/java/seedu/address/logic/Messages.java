package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_PACKAGE =
            "The insurance package specified does not exist in the address book.";
    public static final String MESSAGE_LIST_OF_COMMANDS =
            "\nList of all existing commands: [add] [edit] [delete] [list] [clear] [filter] [sort] [view] [export] "
            + "[addp] [editp] [deletep] [listp] [help] [exit]";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Salary: ")
                .append(person.getSalary())
                .append("; Date of Birth: ")
                .append(person.getDateOfBirth())
                .append("; Marital Status: ")
                .append(person.getMaritalStatus())
                .append("; Dependents: ")
                .append(person.getDependents())
                .append("; Occupation: ")
                .append(person.getOccupation())
                .append("; Insurance Package: ")
                .append(person.getInsurancePackage())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code insurancePackage} for display to the user.
     */
    public static String format(InsurancePackage insurancePackage) {
        final StringBuilder builder = new StringBuilder();
        builder.append(insurancePackage.getPackageName())
                .append("; Description: ")
                .append(insurancePackage.getPackageDescription());
        return builder.toString();
    }
}
