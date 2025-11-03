package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterComparisonPrefixParser;
import seedu.address.logic.parser.filter.FilterContainsPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.logic.parser.filter.FilterTagParser;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    private static final Function<Person, String> GET_SALARY =
            p -> p.getSalary().toString();
    private static final Function<Person, String> GET_DEPENDENTS =
            p -> p.getDependents().toString();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED =
            p -> p.getSalary().isUnspecified();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

    @Test
    public void equals() throws ParseException {
        List<FilterPrefixParser> firstParserList = Collections.singletonList(createNameContainsParser("first"));
        List<FilterPrefixParser> secondParserList = Collections.singletonList(createNameContainsParser("second"));

        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(firstParserList);
        PersonContainsKeywordsPredicate secondPredicate = new PersonContainsKeywordsPredicate(secondParserList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy = new PersonContainsKeywordsPredicate(firstParserList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emptyFilterList_returnsTrue() {
        // A predicate with no filters should always match (vacuously true)
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_descriptiveNameContainsKeyword_returnsTrue() throws ParseException {
        List<FilterPrefixParser> parsers = Collections.singletonList(createNameContainsParser("Alice"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_descriptiveNameDoesNotContainKeyword_returnsFalse() throws ParseException {
        List<FilterPrefixParser> parsers = Collections.singletonList(createNameContainsParser("Carol"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_multipleDescriptiveAllMatch_returnsTrue() throws ParseException {
        List<FilterPrefixParser> parsers = Arrays.asList(
                createNameContainsParser("Alice"),
                createAddressContainsParser("Main")
        );
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_multipleDescriptiveOneMismatch_returnsFalse() throws ParseException {
        List<FilterPrefixParser> parsers = Arrays.asList(
                createNameContainsParser("Alice"),
                createAddressContainsParser("Jurong") // Wrong address
        );
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_numericalSalaryComparison_returnsCorrectly() throws ParseException {
        // Equals
        List<FilterPrefixParser> parsersEq = Collections.singletonList(createSalaryComparisonParser("50000"));
        PersonContainsKeywordsPredicate predEq = new PersonContainsKeywordsPredicate(parsersEq);
        assertTrue(predEq.test(new PersonBuilder().withSalary("50000").build()));

        // Greater than (true)
        List<FilterPrefixParser> parsersGt = Collections.singletonList(createSalaryComparisonParser(">49999"));
        PersonContainsKeywordsPredicate predGt = new PersonContainsKeywordsPredicate(parsersGt);
        assertTrue(predGt.test(new PersonBuilder().withSalary("50000").build()));

        // Greater than (false)
        List<FilterPrefixParser> parsersGtFalse = Collections.singletonList(createSalaryComparisonParser(">50000"));
        PersonContainsKeywordsPredicate predGtFalse = new PersonContainsKeywordsPredicate(parsersGtFalse);
        assertFalse(predGtFalse.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalDependentsComparison_returnsCorrectly() throws ParseException {
        // Less than or equal (true)
        List<FilterPrefixParser> parsersLte = Collections.singletonList(createDependentsComparisonParser("<=2"));
        PersonContainsKeywordsPredicate predLte = new PersonContainsKeywordsPredicate(parsersLte);
        assertTrue(predLte.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(predLte.test(new PersonBuilder().withDependents(1).build()));

        // Less than (false)
        List<FilterPrefixParser> parsersLt = Collections.singletonList(createDependentsComparisonParser("<2"));
        PersonContainsKeywordsPredicate predLt = new PersonContainsKeywordsPredicate(parsersLt);
        assertFalse(predLt.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_tagFilter_returnsCorrectly() throws ParseException {
        // Single tag match
        List<FilterPrefixParser> parsersSingle = Collections.singletonList(createTagParser("friends"));
        PersonContainsKeywordsPredicate predSingle = new PersonContainsKeywordsPredicate(parsersSingle);
        assertTrue(predSingle.test(new PersonBuilder().withTags("friends", "colleagues").build()));

        // Multiple tags all match
        List<FilterPrefixParser> parsersMultiMatch = Collections.singletonList(createTagParser("friends", "owesMoney"));
        PersonContainsKeywordsPredicate predMultiMatch = new PersonContainsKeywordsPredicate(parsersMultiMatch);
        assertTrue(predMultiMatch.test(new PersonBuilder().withTags("friends", "owesMoney", "family").build()));

        // Multiple tags one mismatch
        List<FilterPrefixParser> parsersMultiMismatch =
                Collections.singletonList(createTagParser("friends", "owesMoney"));
        PersonContainsKeywordsPredicate predMultiMismatch = new PersonContainsKeywordsPredicate(parsersMultiMismatch);
        assertFalse(predMultiMismatch.test(new PersonBuilder().withTags("friends", "family").build()));
    }

    @Test
    public void test_mixedFiltersAllMatch_returnsTrue() throws ParseException {
        List<FilterPrefixParser> parsers = Arrays.asList(
                createNameContainsParser("Alice"), // Name contains "Alice"
                createSalaryComparisonParser(">=50000"), // Salary is >= 50000
                createTagParser("friends") // Has tag "friends"
        );
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertTrue(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("60000").withTags("friends").build()));
    }

    @Test
    public void test_mixedFiltersOneMismatch_returnsFalse() throws ParseException {
        List<FilterPrefixParser> parsers = Arrays.asList(
                createNameContainsParser("Alice"),
                createSalaryComparisonParser(">=50000"), // Wrong salary
                createTagParser("friends")
        );
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertFalse(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("40000").withTags("friends").build()));
    }

    //----- Helper Methods -----

    private FilterContainsPrefixParser createNameContainsParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse(keyword);
        return parser;
    }

    private FilterContainsPrefixParser createAddressContainsParser(String keyword) throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        parser.parse(keyword);
        return parser;
    }

    private FilterComparisonPrefixParser createSalaryComparisonParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(
                PREFIX_SALARY, GET_SALARY, IS_SALARY_UNSPECIFIED);
        parser.parse(keyword);
        return parser;
    }

    private FilterComparisonPrefixParser createDependentsComparisonParser(String keyword) throws ParseException {
        FilterComparisonPrefixParser parser = new FilterComparisonPrefixParser(
                PREFIX_DEPENDENTS, GET_DEPENDENTS, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse(keyword);
        return parser;
    }

    private FilterTagParser createTagParser(String... keywords) throws ParseException {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        for (String keyword : keywords) {
            parser.parse(keyword);
        }
        return parser;
    }
}
