package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.ViewData;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * View the full details of a person in the address book (case-sensitive)
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_SUCCESS = "Here's the full detail!";
    public static final String MESSAGE_NOVIEW = "There's no one to be viewed!";

    private final Index index;
    private Person personToView;

    /**
     * @param index of the person in the filtered person list
     */
    public ViewCommand(Index index) {
        requireNonNull(index);
        this.index = index;
        this.personToView = null;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                    + ".\nTry input index based on the list you currently seeing");
        }

        this.personToView = lastShownList.get(index.getZeroBased());

        ViewData last = new ViewData(true, this.personToView);

        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_NOVIEW);
        } else {
            return new CommandResult(MESSAGE_VIEW_SUCCESS, false, last, false);
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
        return e.index.equals(index);
    }
}
