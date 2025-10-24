package seedu.address.logic.parser.filter;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.parseTags;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Parses prefixes that can appear multiple times (tags only) for the filter command.
 */
public class FilterMultiplePrefixParser implements FilterPrefixParser {

    private final Prefix prefix;
    private Set<String> keywords = new HashSet<>();
    private Set<Tag> parsedTags;

    /**
     * Constructs a {@code FilterMultiplePrefixParser}.
     *
     * @param prefix The multiple-occurrence prefix to handle (only PREFIX_TAG).
     */
    public FilterMultiplePrefixParser(Prefix prefix) {
        requireNonNull(prefix);
        this.prefix = prefix;
    }

    @Override
    public Prefix getPrefix() {
        return prefix;
    }

    /**
     * {@inheritDoc}<br>
     * This method adds the given tag string to its internal list of keywords
     * and reparses all accumulated keywords into tags.
     */
    @Override
    public void parse(String args) throws ParseException {
        // Do not allow empty tags, i.e. filter t/
        requireNonNull(args);
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + this.prefix));
        }
        this.keywords.add(args);
        this.parsedTags = parseTags(this.keywords);
    }

    @Override
    public boolean test(Person person) {
        if (this.parsedTags == null || this.parsedTags.isEmpty()) {
            // If no tags were specified, this filter is vacuously true
            return true;
        }
        // All specified tags must be present in the person's tags (case-insensitive contains)
        return this.parsedTags.stream()
                .allMatch(filterTag -> person.getTags().stream()
                        .anyMatch(personTag ->
                                personTag.tagName.toLowerCase().contains(filterTag.tagName.toLowerCase())));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("prefix", this.prefix)
                .add("parsedTags", this.parsedTags)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterMultiplePrefixParser)) {
            return false;
        }

        FilterMultiplePrefixParser otherParser = (FilterMultiplePrefixParser) other;

        return this.prefix.equals(otherParser.prefix)
                && this.parsedTags.containsAll(otherParser.parsedTags)
                && otherParser.parsedTags.containsAll(this.parsedTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.parsedTags);
    }
}
