package seedu.address.logic.parser.filter;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Objects;
import java.util.function.Function;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses descriptive prefixes (e.g., name, address, phone) for the filter command.
 * These prefixes use case-insensitive {@code contains} matching.
 */
public class FilterDescriptivePrefixParser implements FilterPrefixParser {
    private final Prefix prefix;
    private final Function<Person, String> getPersonField;
    private String keyword;

    /**
     * Constructs a {@code FilterDescriptivePrefixParser}.
     *
     * @param prefix The descriptive prefix to handle.
     * @param getPersonField A function to get the relevant String field from a Person.
     */
    public FilterDescriptivePrefixParser(Prefix prefix, Function<Person, String> getPersonField) {
        requireAllNonNull(prefix, getPersonField);
        this.prefix = prefix;
        this.getPersonField = getPersonField;
    }

    @Override
    public Prefix getPrefix() {
        return this.prefix;
    }

    @Override
    public void parse(String args) throws ParseException {
        requireNonNull(args);
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + this.prefix));
        }
        this.keyword = args.toLowerCase();
    }

    @Override
    public boolean test(Person person) {
        String value = this.getPersonField.apply(person);
        if (value == null) {
            return false;
        }
        return value.toLowerCase().contains(this.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("prefix", this.prefix)
                .add("keyword", this.keyword)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterDescriptivePrefixParser)) {
            return false;
        }

        FilterDescriptivePrefixParser otherParser = (FilterDescriptivePrefixParser) other;

        // No need to compare getPersonField, prefix and keyword suffices to check
        return this.prefix.equals(otherParser.prefix)
                && this.keyword.equals(otherParser.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.keyword);
    }
}
