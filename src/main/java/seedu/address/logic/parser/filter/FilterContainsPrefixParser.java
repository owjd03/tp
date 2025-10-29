package seedu.address.logic.parser.filter;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.function.Function;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses prefixes (e.g., name, address, phone) using contains logic for the filter command.
 * These prefixes use case-insensitive {@code contains} matching.
 */
public class FilterContainsPrefixParser implements FilterPrefixParser {
    private final Prefix prefix;
    private final Function<Person, String> getPersonField;
    private String keyword;

    /**
     * Constructs a {@code FilterContainsPrefixParser}.
     *
     * @param prefix The descriptive prefix to handle.
     * @param getPersonField A function to get the relevant String field from a Person.
     */
    public FilterContainsPrefixParser(Prefix prefix, Function<Person, String> getPersonField) {
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
        if (!(other instanceof FilterContainsPrefixParser)) {
            return false;
        }

        FilterContainsPrefixParser otherParser = (FilterContainsPrefixParser) other;

        // No need to compare getPersonField, prefix and keyword suffices to check
        return this.prefix.equals(otherParser.prefix)
                && this.keyword.equals(otherParser.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.keyword);
    }
}
