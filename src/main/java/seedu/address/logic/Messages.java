package seedu.address.logic;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Name;
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
            "\nList of all existing commands: [help] [add] [list] [edit] [find] [view] [filter] [sort] [export] "
            + "[delete] [addp] [editp] [deletep] [listp] [clear] [exit]";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    private static void appendIfPresent(StringBuilder builder, String label, Optional<?> optField) {
        optField.ifPresent(o -> builder.append("; ").append(label).append(": ").append(o));
    }

    /**
     * Formats the success message for an edited person after a successful edit command, showing only changed fields.
     */
    public static String formatEditedPerson(Name name, EditCommand.EditPersonDescriptor descriptor) {
        final StringBuilder builder = new StringBuilder();
        builder.append(name);

        appendIfPresent(builder, "Phone", descriptor.getPhone());
        appendIfPresent(builder, "Email", descriptor.getEmail());
        appendIfPresent(builder, "Address", descriptor.getAddress());
        appendIfPresent(builder, "Salary", descriptor.getSalary());
        appendIfPresent(builder, "Date of Birth", descriptor.getDateOfBirth());
        appendIfPresent(builder, "Marital Status", descriptor.getMaritalStatus());
        appendIfPresent(builder, "Occupation", descriptor.getOccupation());
        appendIfPresent(builder, "Dependents", descriptor.getDependents());
        appendIfPresent(builder, "Insurance Package", descriptor.getInsurancePackage());

        descriptor.getTags().ifPresent(tags -> {
            if (!tags.isEmpty()) {
                builder.append("; Tags: ");
                tags.forEach(builder::append);
            }
        });

        return builder.toString();
    }

    private static void appendIfSpecified(StringBuilder builder, String label, Object field) {
        if (!field.toString().equals("Unspecified")) {
            builder.append(label).append(field);
        }
    }

    /**
     * Formats the {@code person} for display to the user after a successful add or delete command.
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
                .append("; Insurance Package: ")
                .append(person.getInsurancePackage());
        appendIfSpecified(builder, "; Salary: ", person.getSalary());
        appendIfSpecified(builder, "; Date of Birth: ", person.getDateOfBirth());
        appendIfSpecified(builder, "; Marital Status: ", person.getMaritalStatus());
        appendIfSpecified(builder, "; Dependents: ", person.getDependents());
        appendIfSpecified(builder, "; Occupation: ", person.getOccupation());

        if (!person.getTags().isEmpty()) {
            builder.append("; Tags: ");
            person.getTags().forEach(tag -> builder.append(tag).append(" "));
        }
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
