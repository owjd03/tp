package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Salary;

/**
 * Sorts all persons in the address book in alphabetical order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons by the specific field.\n"
            + "Parameters: FIELD [DIRECTION]"
            + "FIELD: name, phone, email, address, salary, dateofbirth, "
            + "maritalstatus, occupation, dependents, insurancepackage\n"
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
     * Helper method to check if a Person's sorted field is "Unspecified".
     *
     * @param person The person to check.
     * @param sortField The field being sorted.
     * @return true if the person's value for that field is "Unspecified" or -1, false otherwise.
     */
    private static boolean isPersonUnspecified(Person person, SortField sortField) {
        switch(sortField) {
        case SALARY:
            return person.getSalary().getValue().equals(Salary.UNSPECIFIED_VALUE);
        case DATEOFBIRTH:
            return person.getDateOfBirth().getValue().equals(DateOfBirth.UNSPECIFIED_VALUE);
        case MARITALSTATUS:
            return person.getMaritalStatus().getValue().equals(MaritalStatus.UNSPECIFIED_VALUE);
        case OCCUPATION:
            return person.getOccupation().getValue().equals(Occupation.UNSPECIFIED_VALUE);
        case DEPENDENTS:
            return person.getDependents().getValue() == Dependents.UNSPECIFIED_VALUE;
        case NAME:
        case PHONE:
        case EMAIL:
        case ADDRESS:
        case INSURANCEPACKAGE:
        default:
            return false;
        }
    }

    /**
     * Creates a comparator for sorting persons based on a particular field
     * and returns the comparator
     *
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS, SALARY,
     *                  DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENT, INSURANCEPACKAGE
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
            baseComparator = Comparator.comparingDouble(person -> Double.parseDouble(person.getSalary()
                    .getValue()));
            break;
        case DATEOFBIRTH:
            baseComparator = (person1, person2) ->
                    person1.getDateOfBirth().getValue().compareToIgnoreCase(person2.getDateOfBirth().getValue());
            break;
        case MARITALSTATUS:
            baseComparator = (person1, person2) ->
                    person1.getMaritalStatus().getValue().compareToIgnoreCase(person2.getMaritalStatus().getValue());
            break;
        case OCCUPATION:
            baseComparator = (person1, person2) ->
                    person1.getOccupation().getValue().compareToIgnoreCase(person2.getOccupation().getValue());
            break;
        case DEPENDENTS:
            baseComparator = Comparator.comparingInt(person -> person.getDependents().getValue());
            break;
        case INSURANCEPACKAGE:
            baseComparator = (person1, person2) ->
                    person1.getInsurancePackage().getPackageName().compareToIgnoreCase(
                            person2.getInsurancePackage().getPackageName());
            break;
        default:
            throw new AssertionError("Invalid sort field" + sortField);
        }

        Comparator<Person> directedComparator;
        if (sortDirection == SortDirection.DESCENDING) {
            directedComparator = baseComparator.reversed();
        } else {
            directedComparator = baseComparator;
        }

        // "Unspecified" values are always sorted to the bottom
        // regardless of sort direction. This applies to optional fields: Salary, DateOfBirth,
        // MaritalStatus, Occupation, and Dependents (which uses -1 as its unspecified value).
        return (p1, p2) -> {
            boolean p1Unspecified = isPersonUnspecified(p1, sortField);
            boolean p2Unspecified = isPersonUnspecified(p2, sortField);

            if (p1Unspecified && p2Unspecified) {
                // Both are "Unspecified" or -1
                return 0;
            } else if (p1Unspecified) {
                // p1 is "Unspecified" or -1, sort it last
                return 1;
            } else if (p2Unspecified) {
                // p2 is "Unspecified" or -1, sort it last
                return -1;
            } else {
                // Neither is "Unspecified" or -1
                return directedComparator.compare(p1, p2);
            }
        };
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
        NAME, PHONE, EMAIL, ADDRESS, SALARY, DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENTS, INSURANCEPACKAGE;
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
