package seedu.address.model.person;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;

/**
 * Tests that a {@code Person}'s attributes matches all the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final Map<Prefix, String> keywords;

    public PersonContainsKeywordsPredicate(Map<Prefix, String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        // returns true if the person matches ALL specified filter criteria.
        // filter n/josh a/kent ridge ->  filter (name=josh AND address=kent ridge)
        return keywords.entrySet().stream().allMatch(entry -> {
            if (entry.getValue().isEmpty()) {
                return false;
            }
            return checkPersonAttribute(person, entry);
        });
    }

    private boolean checkPersonAttribute(Person person, Entry<Prefix, String> entry) {
        Prefix prefix = entry.getKey();
        String keyword = entry.getValue();

        if (prefix.equals(PREFIX_ADDRESS)) {
            return containStringCaseInsensitive(person.getAddress().value, keyword);
        } else if (prefix.equals(PREFIX_DATE_OF_BIRTH)) {
            return containStringCaseInsensitive(person.getDateOfBirth().value, keyword);
        } else if (prefix.equals(PREFIX_DEPENDENTS)) {
            return containStringCaseInsensitive(person.getDependents().toString(), keyword);
        } else if (prefix.equals(PREFIX_EMAIL)) {
            return containStringCaseInsensitive(person.getEmail().value, keyword);
        } else if (prefix.equals(PREFIX_MARITAL_STATUS)) {
            return containStringCaseInsensitive(person.getMaritalStatus().value, keyword);
        } else if (prefix.equals(PREFIX_NAME)) {
            return containStringCaseInsensitive(person.getName().fullName, keyword);
        } else if (prefix.equals(PREFIX_OCCUPATION)) {
            return containStringCaseInsensitive(person.getOccupation().value, keyword);
        } else if (prefix.equals(PREFIX_PHONE)) {
            return containStringCaseInsensitive(person.getPhone().value, keyword);
        } else if (prefix.equals(PREFIX_SALARY)) {
            return containStringCaseInsensitive(person.getSalary().value, keyword);
        } else if (prefix.equals(PREFIX_TAG)) {
            return person.getTags().stream()
                    .anyMatch(tag -> containStringCaseInsensitive(tag.tagName, keyword));
        }

        return false;
    }

    private boolean containStringCaseInsensitive(String first, String second) {
        return first.toLowerCase().contains(second.toLowerCase());
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
        return keywords.equals(otherPersonContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
