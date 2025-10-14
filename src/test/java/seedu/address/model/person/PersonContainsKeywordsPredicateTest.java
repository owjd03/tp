package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        Map<Prefix, String> firstPredicateKeywordMap = new HashMap<>();
        firstPredicateKeywordMap.put(PREFIX_NAME, "first");
        Map<Prefix, String> secondPredicateKeywordMap = new HashMap<>();
        secondPredicateKeywordMap.put(PREFIX_NAME, "second");

        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(firstPredicateKeywordMap);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordMap);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordMap);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(new Object()));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emptyKeyword_returnsFalse() {
        // Empty keyword for name
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Empty keyword for address
        keywords = new HashMap<>();
        keywords.put(PREFIX_ADDRESS, "");
        predicate = new PersonContainsKeywordsPredicate(keywords);
        assertFalse(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_nameContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_addressContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_ADDRESS, "Main");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_dateOfBirthContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_DATE_OF_BIRTH, "2025-10-10");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth("2025-10-10").build()));
    }

    @Test
    public void test_dependentsContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_DEPENDENTS, "2");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_emailContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_EMAIL, "alice");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void test_maritalStatusContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_MARITAL_STATUS, "Single");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withMaritalStatus("Single").build()));
    }

    @Test
    public void test_occupationContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_OCCUPATION, "Engineer");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withOccupation("Software Engineer").build()));
    }

    @Test
    public void test_phoneContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_PHONE, "98765432");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withPhone("98765432").build()));
    }

    @Test
    public void test_salaryContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_SALARY, "5000");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withSalary("5000").build()));
    }

    @Test
    public void test_tagContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_TAG, "friend");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "colleague").build()));
    }

    @Test
    public void test_multipleKeywords_allMatch() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        keywords.put(PREFIX_ADDRESS, "Main");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_multipleKeywords_oneDoesNotMatch() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        keywords.put(PREFIX_ADDRESS, "Jurong");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_unknownPrefix_returnsFalse() {
        // Using a dummy prefix that is not in the list of handled prefixes
        Prefix unknownPrefix = new Prefix("unknown/");
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(unknownPrefix, "someValue");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        assertFalse(predicate.test(new PersonBuilder().build()));
    }
}
