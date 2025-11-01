package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.FilterCommandParser;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Filters for persons in the address book based on the specifications to the parameters.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters contacts who match ALL specified criteria.\n"
            + "By default, all fields use a case-insensitive 'contains' search (e.g., n/jo, s/50).\n"
            + "For Salary (s/) and Dependents (dep/), you can also use operators (<, <=, >, >=, =) "
            + "for strict numerical comparison.\n"
            + "You must provide at least one prefix (e.g., n/, s/, t/).\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_DATE_OF_BIRTH + "DATE_OF_BIRTH] "
            + "[" + PREFIX_MARITAL_STATUS + "MARITAL_STATUS] "
            + "[" + PREFIX_SALARY + "SALARY] "
            + "[" + PREFIX_DEPENDENTS + "NUMBER_OF_DEPENDENTS] "
            + "[" + PREFIX_OCCUPATION + "OCCUPATION] "
            + "[" + PREFIX_INSURANCE_PACKAGE + "INSURANCE_PACKAGE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " n/alex s/>=50000 t/friend";

    public static final String MESSAGE_NO_PERSONS_FOUND = "No persons found matching your criteria.";

    private final PersonContainsKeywordsPredicate predicate;
    private final String args;

    /**
     * Constructs a new FilterCommand object.
     *
     * @param predicate
     * @param args The user's input for filter. Guaranteed to be non-null and non-empty by
     *             {@code FilterCommandParser.parse}.
     *
     * @seealso {@link FilterCommandParser#parse(String)}
     */
    public FilterCommand(PersonContainsKeywordsPredicate predicate, String args) {
        this.predicate = predicate;
        this.args = args;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int listSize = model.getFilteredPersonList().size();
        String message = "%s\nCommand: filter %s";
        if (listSize == 0) {
            return new CommandResult(String.format(message, MESSAGE_NO_PERSONS_FOUND, this.args));
        }

        String numPersons = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, listSize);
        return new CommandResult(String.format(message, numPersons, this.args));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return this.predicate.equals(otherFilterCommand.predicate)
                && this.args.equals(otherFilterCommand.args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("args", args)
                .toString();
    }
}
