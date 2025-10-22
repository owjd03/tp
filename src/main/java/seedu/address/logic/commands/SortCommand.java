package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts all persons in the address book in alphabetical order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons by the specific field.\n"
            + "Parameters: FIELD [DIRECTION]"
            + "FIELD: name, phone, email, address, salary, dateofbirth, maritalstatus, occupation, dependent\n"
            + "DIRECTION: ascending, descending\n"
            + "Example: " + COMMAND_WORD + " name descending";
    public static final String MESSAGE_SUCCESS = "Sorted all persons by ";

    private final SortField sortField;
    private final SortDirection sortDirection;

    /**
     * @param sortField of the selected sort criteria
     * @param sortDirection of the selected sort criteria
     */
    public SortCommand(SortField sortField, SortDirection sortDirection) {
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> comparator = createComparator(sortField, sortDirection);
        model.sortPersonList(comparator);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        String directionText = sortDirection.toString().toLowerCase();
        String message = MESSAGE_SUCCESS + sortField + " in " + directionText + " order";
        return new CommandResult(message);
    }

    /**
     * Creates a comparator for sorting persons based on a particular field
     * and returns the comparator
     *
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS, SALARY,
     *                  DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENT
     * @param sortDirection the direction is sorted by ASCENDING OR DESCENDING
     * @return comparator that compares two persons based on the provided field and direction
     * @throws AssertionError if an invalid field is provided
     **/
    private static Comparator<Person> createComparator(SortField sortField, SortDirection sortDirection) {
        Comparator<Person> baseComparator;

        switch (sortField) {
        case NAME:
            baseComparator = (person1, person2) ->
                    person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName);
            break;
        case PHONE:
            baseComparator = (person1, person2) ->
                    person1.getPhone().value.compareToIgnoreCase(person2.getPhone().value);
            break;
        case EMAIL:
            baseComparator = (person1, person2) ->
                    person1.getEmail().value.compareToIgnoreCase(person2.getEmail().value);
            break;
        case ADDRESS:
            baseComparator = (person1, person2) ->
                    person1.getAddress().value.compareToIgnoreCase(person2.getAddress().value);
            break;
        case SALARY:
            baseComparator = Comparator.comparingDouble(person -> Double.parseDouble(person.getSalary().value));
            break;
        case DATEOFBIRTH:
            baseComparator = (person1, person2) ->
                    person1.getDateOfBirth().value.compareToIgnoreCase(person2.getDateOfBirth().value);
            break;
        case MARITALSTATUS:
            baseComparator = (person1, person2) ->
                    person1.getMaritalStatus().value.compareToIgnoreCase(person2.getMaritalStatus().value);
            break;
        case OCCUPATION:
            baseComparator = (person1, person2) ->
                    person1.getOccupation().value.compareToIgnoreCase(person2.getOccupation().value);
            break;
        case DEPENDENT:
            baseComparator = Comparator.comparingInt(person -> person.getDependents().value);
            break;
        default:
            throw new AssertionError("Invalid sort field" + sortField);
        }

        return sortDirection == SortDirection.DESCENDING ? baseComparator.reversed() : baseComparator;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof SortCommand)) {
            return false;
        }

        SortCommand secondSortCommand = (SortCommand) object;
        return sortField.equals(secondSortCommand.sortField)
                && sortDirection.equals(secondSortCommand.sortDirection);
    }

    /**
     * Represents the available fields to sort by
     **/
    public enum SortField {
        NAME, PHONE, EMAIL, ADDRESS, SALARY, DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENT
    }

    /**
     * Represents the available directions to sort by
     **/
    public enum SortDirection {
        ASCENDING, DESCENDING
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("sortField", sortField)
                .add("sortDirection", sortDirection)
                .toString();
    }
}
