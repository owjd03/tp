package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.filter.FilterPrefixParser;

/**
 * Tests that a {@code Person}'s attributes matches all the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<FilterPrefixParser> filterList;

    /**
     * Constructs a {@code PersonContainsKeywordsPredicate} with the specified keywords.
     * @param filterList A list of {@code FilterPrefixParser} objects, each encapsulating the
     *                   parsing and testing logic for a specific prefix
     */
    public PersonContainsKeywordsPredicate(List<FilterPrefixParser> filterList) {
        this.filterList = filterList;
    }

    @Override
    public boolean test(Person person) {
        // A person matches if they satisfy ALL the specified filters.
        return filterList.stream().allMatch(filterPrefixParser -> filterPrefixParser.test(person));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPersonContainsKeywordsPredicate = (PersonContainsKeywordsPredicate) other;
        return this.filterList.equals(otherPersonContainsKeywordsPredicate.filterList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filters", this.filterList)
                .toString();
    }
}
