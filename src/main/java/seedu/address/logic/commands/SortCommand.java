package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts all persons in the address book by name in alphabetical order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all persons by name in alphabetical order.\n"
            + "Parameters: name or phone or email or address\n"
            + "Example: " + COMMAND_WORD + " name";
    public static final String MESSAGE_SUCCESS = "Sorted all persons in alphabetical order by ";

    private final SortField sortField;

    /**
     * @param sortField of the selected sort criteria
     */
    public SortCommand(SortField sortField) {
        this.sortField = sortField;
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> nameComparator = nameComparator(sortField);
        model.sortPersonList(nameComparator);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS + sortField);
    }

    /**
     * Creates a comparator for sorting persons based on a particular field
     * and returns the comparator
     *
     * @param sortField the field is sorted by NAME, PHONE, EMAIL, ADDRESS
     * @return comparator that compares two persons based on the provided field
     * @throws AssertionError if an invalid field is provided
     **/
    private static Comparator<Person> nameComparator(SortField sortField) {
        switch (sortField) {
        case NAME:
            return (person1, person2) ->
                    person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName);
        case PHONE:
            return (person1, person2) ->
                    person1.getPhone().value.compareToIgnoreCase(person2.getPhone().value);
        case EMAIL:
            return (person1, person2) ->
                    person1.getEmail().value.compareToIgnoreCase(person2.getEmail().value);
        case ADDRESS:
            return (person1, person2) ->
                    person1.getAddress().value.compareToIgnoreCase(person2.getAddress().value);
        default:
            throw new AssertionError("Invalid sort field" + sortField);
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
        return sortField.equals(secondSortCommand.sortField);
    }

    /**
     * Represents the available fields to sort by
     **/
    public enum SortField {
        NAME, PHONE, EMAIL, ADDRESS
    }
}
