package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;
import java.util.function.Function;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Salary;

/**
 * Sorts all persons in the address book by specified field and direction.
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
     * Creates a SortCommand to sort persons by the specified field and direction
     *
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
     * Creates a comparator for sorting persons based on a particular field and direction
     * and returns the comparator
     *
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS, SALARY,
     *                  DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENTS, INSURANCEPACKAGE
     * @param sortDirection the direction is sorted by ASCENDING OR DESCENDING
     * @return comparator that compares two persons based on the provided field and direction
     */
    private static Comparator<Person> createComparator(SortField sortField, SortDirection sortDirection) {
        Comparator<Person> baseComparator = createBaseComparator(sortField);
        Comparator<Person> directedComparator = applyDirection(baseComparator, sortDirection);
        return handleUnspecifiedValues(directedComparator, sortField);
    }

    /**
     * Creates a comparator for sorting persons based on a particular field
     * and returns the comparator
     *
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS, SALARY,
     *                  DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENTS, INSURANCEPACKAGE
     * @return base comparator that compares two persons based on the provided field and direction
     */
    private static Comparator<Person> createBaseComparator(SortField sortField) {
        switch (sortField) {
        case NAME:
            return createStringComparator(person -> person.getName().fullName);
        case PHONE:
            return createStringComparator(person -> person.getPhone().value);
        case EMAIL:
            return createStringComparator(person -> person.getEmail().value);
        case ADDRESS:
            return createStringComparator(person -> person.getAddress().value);
        case SALARY:
            return createNumericComparator(person -> Double.parseDouble(person.getSalary().getValue()));
        case DATEOFBIRTH:
            return createStringComparator(person -> person.getDateOfBirth().getValue());
        case MARITALSTATUS:
            return createStringComparator(person -> person.getMaritalStatus().getValue());
        case OCCUPATION:
            return createStringComparator(person -> person.getOccupation().getValue());
        case DEPENDENTS:
            return Comparator.comparingInt(person -> person.getDependents().getValue());
        case INSURANCEPACKAGE:
            return createStringComparator(person -> person.getInsurancePackage().getPackageName());
        default:
            throw new AssertionError("Invalid sort field: " + sortField);
        }
    }

    /**
     * Creates a string comparator using the provided mapper function.
     *
     * @param mapper function that gets a string value from a Person for comparison
     * @return string comparator that compares two persons based on the provided field and direction
     */
    private static Comparator<Person> createStringComparator(Function<Person, String> mapper) {
        return (person1, person2) -> mapper.apply(person1).compareToIgnoreCase(mapper.apply(person2));
    }

    /**
     * Creates a numeric comparator using the provided mapper function.
     *
     * @param mapper function that gets a double value from a Person for comparison
     * @return numeric comparator that compares two persons based on the provided field and direction
     */
    private static Comparator<Person> createNumericComparator(Function<Person, Double> mapper) {
        return Comparator.comparing(mapper);
    }

    /**
     * Applies the sort direction to the base comparator.
     *
     * @param baseComparator base comparator that compares two persons based on provided field
     * @param sortDirection the direction is sorted by ASCENDING OR DESCENDING
     * @return comparator with the specified direction applied
     */
    private static Comparator<Person> applyDirection(Comparator<Person> baseComparator, SortDirection sortDirection) {
        if (sortDirection == SortDirection.DESCENDING) {
            return baseComparator.reversed();
        }
        return baseComparator;
    }

    /**
     * Wraps the comparator to handle unspecified values by placing them at the bottom.
     *
     * @param directedComparator direction comparator that compares two persons based on provided field and direction
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS, SALARY,
     *                       DATEOFBIRTH, MARITALSTATUS, OCCUPATION, DEPENDENTS, INSURANCEPACKAGE
     * @return comparator that accounts for Unspecified or -1 values
     */
    private static Comparator<Person> handleUnspecifiedValues(Comparator<Person> directedComparator,
                                                              SortField sortField) {
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
