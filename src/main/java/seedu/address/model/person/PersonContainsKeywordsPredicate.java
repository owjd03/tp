package seedu.address.model.person;

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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s attributes matches all the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final Map<Prefix, String> keywords;
    private final Set<Tag> tags;

    /**
     * Constructs a {@code PersonContainsKeywordsPredicate} with the specified keywords.
     * @param keywords Map mapping prefixes to the keyword to filter for.
     * @param tags Set of tags to filter for.
     */
    public PersonContainsKeywordsPredicate(Map<Prefix, String> keywords, Set<Tag> tags) {
        this.keywords = keywords;
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        // returns true if the person matches ALL specified filter criteria.
        // filter n/josh a/kent ridge ->  filter (name=josh AND address=kent ridge)
        // Keywords or tags may be empty
        boolean matchKeywords;
        boolean matchTags;

        if (this.keywords.isEmpty() && this.tags.isEmpty()) {
            // This case should not happen as this check will have been done in FilterCommandParser.
            return false;
        }

        if (this.keywords.isEmpty()) {
            // If keywords is empty, then it is vacuously true.
            matchKeywords = true;
        } else {
            matchKeywords = this.keywords.entrySet()
                    .stream().allMatch(entry -> checkKeywordsMatch(person, entry));
        }

        if (this.tags.isEmpty()) {
            // If tags is empty, then it is vacuously true.
            matchTags = true;
        } else {
            matchTags = this.tags.stream().allMatch(tag -> checkTagsMatch(person, tag.tagName));
        }
        return matchKeywords && matchTags;
    }

    private boolean checkKeywordsMatch(Person person, Entry<Prefix, String> entry) {
        Prefix prefix = entry.getKey();
        String keyword = entry.getValue();

        if (prefix.equals(PREFIX_ADDRESS)) {
            return containsIgnoreCase(person.getAddress().value, keyword);
        } else if (prefix.equals(PREFIX_DATE_OF_BIRTH)) {
            return containsIgnoreCase(person.getDateOfBirth().value, keyword);
        } else if (prefix.equals(PREFIX_DEPENDENTS)) {
            return containsIgnoreCase(person.getDependents().toString(), keyword);
        } else if (prefix.equals(PREFIX_EMAIL)) {
            return containsIgnoreCase(person.getEmail().value, keyword);
        } else if (prefix.equals(PREFIX_INSURANCE_PACKAGE)) {
            return containsIgnoreCase(person.getInsurancePackage().getPackageName(), keyword);
        } else if (prefix.equals(PREFIX_MARITAL_STATUS)) {
            return containsIgnoreCase(person.getMaritalStatus().value, keyword);
        } else if (prefix.equals(PREFIX_NAME)) {
            return containsIgnoreCase(person.getName().fullName, keyword);
        } else if (prefix.equals(PREFIX_OCCUPATION)) {
            return containsIgnoreCase(person.getOccupation().value, keyword);
        } else if (prefix.equals(PREFIX_PHONE)) {
            return containsIgnoreCase(person.getPhone().value, keyword);
        } else if (prefix.equals(PREFIX_SALARY)) {
            return containsIgnoreCase(person.getSalary().value, keyword);
        } else if (prefix.equals(PREFIX_INSURANCE_PACKAGE)) {
            return containsIgnoreCase(person.getInsurancePackage().getPackageName(), keyword);
        }

        return false;
    }

    /**
     * Returns true if the first string contains the second string, ignoring case.
     */
    private boolean containsIgnoreCase(String first, String second) {
        if (first == null || second == null || first.isEmpty() || second.isEmpty()) {
            return false;
        }

        return first.toLowerCase().contains(second.toLowerCase());
    }

    private boolean checkTagsMatch(Person person, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return false;
        }

        return person.getTags().stream().anyMatch(tag -> containsIgnoreCase(tag.tagName, keyword));
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
        return this.keywords.equals(otherPersonContainsKeywordsPredicate.keywords)
                && this.tags.equals(otherPersonContainsKeywordsPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", this.keywords)
                .add("tags", this.tags)
                .toString();
    }
}
