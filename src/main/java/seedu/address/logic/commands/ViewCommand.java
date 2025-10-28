package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.ViewData;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * View the full details of a person in the address book.
 * Keyword matching is case-insensitive.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the full details of a Person.\n"
            + "Parameters: [NAME-KEYWORD] or [INDEX] (must be a positive integer)\n"
            + "Example #1: " + COMMAND_WORD + " alex\n"
            + "Example #2: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_SUCCESS = "Here's the full detail of ";
    public static final String MESSAGE_NOVIEW_NAME = "There's no one with that name in the displayed list!";
    public static final String MESSAGE_DUPLICATE_NAME = "Found at least two people with the keyword!\n"
            + "Please write the full name or use INDEX instead.";

    private final Index index;
    private final String predicate;
    private Person personToView;

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
    public ViewCommand(String predicate) {
        requireNonNull(predicate);
        this.index = null;
        this.predicate = predicate;
    }

    /**
     * Configure message with the person's name when viewing is successful
     * @param name Name of the person being viewed
     */
    public String getMessageViewSuccess(Name name) {
        String messageSuccess = MESSAGE_VIEW_SUCCESS + name.fullName + "!";
        return messageSuccess;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (index != null) {
            return executeByIndex(model);
        } else if (!predicate.isBlank()) {
            return executeByName(model);
        } else {
            throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Execute the command based on the input index
     *
     * @param model Current model being used
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult executeByIndex(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX
                    + ".\nTry input index based on the list you currently seeing");
        }

        this.personToView = lastShownList.get(index.getZeroBased());

        ViewData last = new ViewData(true, this.personToView);

        return new CommandResult(getMessageViewSuccess(this.personToView.getName()), false, last,
                false, false);
    }

    /**
     * Execute the command based on the input keyword
     *
     * @param model Current model being used
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult executeByName(Model model) throws CommandException {
        List<Person> filteredList = model.getFilteredPersonList()
                .stream()
                .filter(p ->
                        p.getName().fullName.toLowerCase()
                                   .contains(predicate.toLowerCase())
                        )
                .toList();

        if (filteredList.isEmpty()) {
            throw new CommandException(MESSAGE_NOVIEW_NAME);
        } else if (filteredList.size() > 1) {
            throw new CommandException(MESSAGE_DUPLICATE_NAME);
        } else {
            this.personToView = filteredList.get(0);

            ViewData last = new ViewData(true, this.personToView);

            return new CommandResult(getMessageViewSuccess(this.personToView.getName()), false, last,
                    false, false);
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
        if (index != null) {
            return e.index.equals(index);
        } else {
            return e.predicate.equals(predicate);
        }
    }
}
