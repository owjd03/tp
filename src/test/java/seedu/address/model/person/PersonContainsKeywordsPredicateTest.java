package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
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

    private static final Function<Person, Double> GET_SALARY_DOUBLE =
            p -> p.getSalary().getNumericValue();
    private static final Function<Person, Double> GET_DEPENDENTS_DOUBLE =
            p -> (double) p.getDependents().getNumericValue();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED =
            p -> p.getSalary().isUnspecified();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

    @Test
    public void equals() throws ParseException {
        // Setup first predicate
        List<FilterPrefixParser> firstParserList = new ArrayList<>();
        FilterContainsPrefixParser firstParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        firstParser.parse("first");
        firstParserList.add(firstParser);
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstParserList);

        // Setup second predicate
        List<FilterPrefixParser> secondParserList = new ArrayList<>();
        FilterContainsPrefixParser secondParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        secondParser.parse("second");
        secondParserList.add(secondParser);
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
        FilterContainsPrefixParser parser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse("Alice");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_descriptiveNameDoesNotContainKeyword_returnsFalse() throws ParseException {
        FilterContainsPrefixParser parser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse("Carol");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_multipleDescriptiveAllMatch_returnsTrue() throws ParseException {
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        FilterContainsPrefixParser addressParser =
                new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Main");
        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, addressParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_multipleDescriptiveOneMismatch_returnsFalse() throws ParseException {
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        FilterContainsPrefixParser addressParser =
                new FilterContainsPrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Jurong"); // Wrong address
        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, addressParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_numericalSalaryEquals_returnsTrue() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse("50000");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalSalaryGreaterThan_returnsTrue() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse(">49999");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalSalaryGreaterThan_returnsFalse() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse(">50000");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalDependentsLessThanOrEqual_returnsTrue() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse("<=2");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(predicate.test(new PersonBuilder().withDependents(1).build()));
    }

    @Test
    public void test_numericalDependentsLessThan_returnsFalse() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse("<2");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_singleTagMatch_returnsTrue() throws ParseException {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        parser.parse("friends");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "colleagues").build()));
    }

    @Test
    public void test_multipleTagsAllMatch_returnsTrue() throws ParseException {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("owesMoney");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "owesMoney", "family").build()));
    }

    @Test
    public void test_multipleTagsOneMismatch_returnsFalse() throws ParseException {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("owesMoney");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withTags("friends", "family").build()));
    }

    @Test
    public void test_mixedFiltersAllMatch_returnsTrue() throws ParseException {
        // Name contains "Alice"
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");

        // Salary is >= 50000
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser.parse(">=50000");

        // Has tag "friends"
        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friends");

        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, salaryParser, tagParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertTrue(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("60000").withTags("friends").build()));
    }

    @Test
    public void test_mixedFiltersOneMismatch_returnsFalse() throws ParseException {
        FilterContainsPrefixParser nameParser =
                new FilterContainsPrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");

        // Wrong salary
        FilterComparisonPrefixParser salaryParser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser.parse(">=50000");

        FilterTagParser tagParser = new FilterTagParser(PREFIX_TAG);
        tagParser.parse("friends");

        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, salaryParser, tagParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertFalse(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("40000").withTags("friends").build()));
    }
}
