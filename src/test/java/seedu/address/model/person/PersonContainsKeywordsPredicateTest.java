package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        Map<Prefix, String> firstPredicateKeywordMap = new HashMap<>();
        firstPredicateKeywordMap.put(PREFIX_NAME, "first");
        Map<Prefix, String> secondPredicateKeywordMap = new HashMap<>();
        secondPredicateKeywordMap.put(PREFIX_NAME, "second");

        Set<Tag> firstTagSet = Collections.singleton(new Tag("friends"));
        Set<Tag> secondTagSet = Collections.singleton(new Tag("colleagues"));

        // Predicates with keywords only
        PersonContainsKeywordsPredicate firstPredicateKeywordsOnly =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordMap, Collections.emptySet());
        PersonContainsKeywordsPredicate secondPredicateKeywordsOnly =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordMap, Collections.emptySet());

        // Predicates with tags only
        PersonContainsKeywordsPredicate firstPredicateTagsOnly =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), firstTagSet);
        PersonContainsKeywordsPredicate secondPredicateTagsOnly =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), secondTagSet);

        // Predicates with both keywords and tags
        Map<Prefix, String> combinedKeywordMap = new HashMap<>(firstPredicateKeywordMap);
        combinedKeywordMap.put(PREFIX_ADDRESS, "street");
        Set<Tag> combinedTagSet = new HashSet<>(firstTagSet);
        combinedTagSet.add(new Tag("family"));
        PersonContainsKeywordsPredicate firstPredicateCombined =
                new PersonContainsKeywordsPredicate(combinedKeywordMap, combinedTagSet);

        // same object -> returns true
        assertTrue(firstPredicateKeywordsOnly.equals(firstPredicateKeywordsOnly));
        assertTrue(firstPredicateTagsOnly.equals(firstPredicateTagsOnly));
        assertTrue(firstPredicateCombined.equals(firstPredicateCombined));

        // same values (keywords only) -> returns true
        PersonContainsKeywordsPredicate firstPredicateKeywordsOnlyCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordMap, Collections.emptySet());
        assertTrue(firstPredicateKeywordsOnly.equals(firstPredicateKeywordsOnlyCopy));

        // same values (tags only) -> returns true
        PersonContainsKeywordsPredicate firstPredicateTagsOnlyCopy =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), firstTagSet);
        assertTrue(firstPredicateTagsOnly.equals(firstPredicateTagsOnlyCopy));

        // same values (combined) -> returns true
        PersonContainsKeywordsPredicate firstPredicateCombinedCopy =
                new PersonContainsKeywordsPredicate(combinedKeywordMap, combinedTagSet);
        assertTrue(firstPredicateCombined.equals(firstPredicateCombinedCopy));

        // different types -> returns false
        assertFalse(firstPredicateKeywordsOnly.equals(1));
        assertFalse(firstPredicateKeywordsOnly.equals(new Object()));

        // null -> returns false
        assertFalse(firstPredicateKeywordsOnly.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicateKeywordsOnly.equals(secondPredicateKeywordsOnly));

        // different tags -> returns false
        assertFalse(firstPredicateTagsOnly.equals(secondPredicateTagsOnly));

        // different keywords and tags -> returns false
        assertFalse(firstPredicateKeywordsOnly.equals(firstPredicateTagsOnly)); // keywords vs tags
        assertFalse(firstPredicateCombined.equals(firstPredicateKeywordsOnly)); // combined vs keywords only
        assertFalse(firstPredicateCombined.equals(firstPredicateTagsOnly)); // combined vs tags only
    }

    @Test
    public void test_emptyKeyword_returnsFalse() {
        // Empty keyword for name
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Empty keyword for address
        keywords = new HashMap<>();
        keywords.put(PREFIX_ADDRESS, "");
        predicate = new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertFalse(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_nameContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_addressContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_ADDRESS, "Main");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_dateOfBirthContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_DATE_OF_BIRTH, "2025-10-10");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withDateOfBirth("2025-10-10").build()));
    }

    @Test
    public void test_dependentsContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_DEPENDENTS, "2");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_emailContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_EMAIL, "alice");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void test_maritalStatusContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_MARITAL_STATUS, "Single");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withMaritalStatus("Single").build()));
    }

    @Test
    public void test_occupationContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_OCCUPATION, "Engineer");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withOccupation("Software Engineer").build()));
    }

    @Test
    public void test_phoneContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_PHONE, "98765432");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withPhone("98765432").build()));
    }

    @Test
    public void test_salaryContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_SALARY, "5000");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withSalary("5000").build()));
    }

    @Test
    public void test_insurancePackageContainsKeyword_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_INSURANCE_PACKAGE, "Gold");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder()
                .withInsurancePackage("Gold", "").build()));
    }

    @Test
    public void test_singleTagMatches_returnsTrue() {
        Set<Tag> tags = Collections.singleton(new Tag("friends"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "colleagues").build()));
    }

    @Test
    public void test_multipleTagsAllMatch_returnsTrue() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("owesMoney"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "owesMoney", "family").build()));
    }

    @Test
    public void test_multipleTagsOneDoesNotMatch_returnsFalse() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("owesMoney"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags);
        assertFalse(predicate.test(new PersonBuilder().withTags("friends", "family").build()));
    }

    @Test
    public void test_keywordsAndTagsAllMatch_returnsTrue() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        Set<Tag> tags = Collections.singleton(new Tag("friends"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords, tags);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("friends").build()));
    }

    @Test
    public void test_keywordsAndTagsKeywordsMismatch_returnsFalse() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Bob"); // Mismatch
        Set<Tag> tags = Collections.singleton(new Tag("friends"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords, tags);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friends").build()));
    }

    @Test
    public void test_keywordsAndTagsTagsMismatch_returnsFalse() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        Set<Tag> tags = Collections.singleton(new Tag("owesMoney")); // Mismatch
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords, tags);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("friends").build()));
    }

    @Test
    public void test_emptyTagSetKeywordsMap_returnsFalse() {
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), Collections.emptySet());
        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));
    }

    @Test
    public void test_multipleKeywords_allMatch() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        keywords.put(PREFIX_ADDRESS, "Main");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_multipleKeywords_oneDoesNotMatch() {
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        keywords.put(PREFIX_ADDRESS, "Jurong");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_unknownPrefix_returnsFalse() {
        // Using a dummy prefix that is not in the list of handled prefixes
        Prefix unknownPrefix = new Prefix("unknown/");
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(unknownPrefix, "someValue");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(keywords, Collections.emptySet());
        assertFalse(predicate.test(new PersonBuilder().build()));
    }
}
