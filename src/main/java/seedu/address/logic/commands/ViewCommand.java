package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * View the full details of a person in the address book (case-sensitive)
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person.\n"
            + "Parameters: NAME or INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " alex\n"
            + "Example 2: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_SUCCESS = "Here's the full detail!";
    public static final String MESSAGE_NOVIEW = "There's no one to be viewed!";

    private final Index index;
    private final NameContainsKeywordsPredicate predicate;

    /**
     * @param index of the person in the filtered person list
     */
    public ViewCommand(Index index) {
        this.index = index;
        this.predicate = null;
    }

    /**
     * @param predicate of the person in the filtered person list
     */
    public ViewCommand(NameContainsKeywordsPredicate predicate) {
        this.index = null;
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (index != null) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToView = lastShownList.get(index.getZeroBased());
            String[] personToViewName = personToView.getName().toString().split("\\s+");;
            model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(personToViewName)));
        } else {
            model.updateFilteredPersonList(predicate);
        }

        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_NOVIEW);
        } else {
            return new CommandResult(MESSAGE_VIEW_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand e = (ViewCommand) other;
        if (index == null) {
            return e.index == null && predicate.equals(e.predicate);
        } else {
            return e.predicate == null && index.equals(e.index);
        }
    }
}
