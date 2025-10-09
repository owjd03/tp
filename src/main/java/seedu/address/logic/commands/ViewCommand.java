package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * View the full details of a person in the address book (case-sensitive)
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person.\n"
            + "Parameters: NAME or INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " alex\n"
            + "Example 2: " + COMMAND_WORD + " 1";

    public static final String SHOWING_VIEW_MESSAGE = "Opened full details of the person.";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d";

    private final Index index;
    private final NameContainsKeywordsPredicate predicate;

    /**
     * @param index of the person in the filtered person list
     */
    public ViewCommand(Index index) {
        requireNonNull(index);

        this.index = index;
        this.predicate = null;
    }

    /**
     * @param predicate of the person in the filtered person list
     */
    public ViewCommand(NameContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);

        this.index = null;
        this.predicate = predicate;
    }


    public static final String MESSAGE_SUCCESS = "Here's the full detail!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (index != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToView = lastShownList.get(index.getZeroBased());

        } else {
            throw new CommandException(
                    String.format(MESSAGE_ARGUMENTS, index.getOneBased()));
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
        return index.equals(e.index);
    }
}
