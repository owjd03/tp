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
            + "Parameters: name\n"
            + "Example: " + COMMAND_WORD + " name";
    public static final String MESSAGE_SUCCESS = "Sorted all persons by name in alphabetical order";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> nameComparator = nameComparator();
        model.sortPersonList(nameComparator);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private static Comparator<Person> nameComparator() {
        return (person1, person2) ->
                person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName);
    }
}
