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

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterDescriptivePrefixParser;
import seedu.address.logic.parser.filter.FilterMultiplePrefixParser;
import seedu.address.logic.parser.filter.FilterNumericalPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() throws ParseException {
        // Setup first predicate
        List<FilterPrefixParser> firstParserList = new ArrayList<>();
        FilterDescriptivePrefixParser firstParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        firstParser.parse("first");
        firstParserList.add(firstParser);
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstParserList);

        // Setup second predicate
        List<FilterPrefixParser> secondParserList = new ArrayList<>();
        FilterDescriptivePrefixParser secondParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
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
        FilterDescriptivePrefixParser parser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse("Alice");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_descriptiveNameDoesNotContainKeyword_returnsFalse() throws ParseException {
        FilterDescriptivePrefixParser parser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        parser.parse("Carol");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_multipleDescriptiveAllMatch_returnsTrue() throws ParseException {
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        FilterDescriptivePrefixParser addressParser =
                new FilterDescriptivePrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Main");
        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, addressParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_multipleDescriptiveOneMismatch_returnsFalse() throws ParseException {
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");
        FilterDescriptivePrefixParser addressParser =
                new FilterDescriptivePrefixParser(PREFIX_ADDRESS, p -> p.getAddress().value);
        addressParser.parse("Jurong"); // Wrong address
        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, addressParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withAddress("Main Street").build()));
    }

    @Test
    public void test_numericalSalaryEquals_returnsTrue() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        parser.parse("50000");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalSalaryGreaterThan_returnsTrue() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        parser.parse(">49999");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalSalaryGreaterThan_returnsFalse() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        parser.parse(">50000");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void test_numericalDependentsLessThanOrEqual_returnsTrue() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, p -> p.getDependents().getNumericValue());
        parser.parse("<=2");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(predicate.test(new PersonBuilder().withDependents(1).build()));
    }

    @Test
    public void test_numericalDependentsLessThan_returnsFalse() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, p -> p.getDependents().getNumericValue());
        parser.parse("<2");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_singleTagMatch_returnsTrue() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "colleagues").build()));
    }

    @Test
    public void test_multipleTagsAllMatch_returnsTrue() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("owesMoney");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "owesMoney", "family").build()));
    }

    @Test
    public void test_multipleTagsOneMismatch_returnsFalse() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("owesMoney");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList(parser));
        assertFalse(predicate.test(new PersonBuilder().withTags("friends", "family").build()));
    }

    // --- Mixed Filter Tests ---

    @Test
    public void test_mixedFiltersAllMatch_returnsTrue() throws ParseException {
        // Name contains "Alice"
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");

        // Salary is >= 50000
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse(">=50000");

        // Has tag "friends"
        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friends");

        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, salaryParser, tagParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertTrue(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("60000").withTags("friends").build()));
    }

    @Test
    public void test_mixedFiltersOneMismatch_returnsFalse() throws ParseException {
        FilterDescriptivePrefixParser nameParser =
                new FilterDescriptivePrefixParser(PREFIX_NAME, p -> p.getName().fullName);
        nameParser.parse("Alice");

        // Wrong salary
        FilterNumericalPrefixParser salaryParser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> p.getSalary().getNumericValue());
        salaryParser.parse(">=50000");

        FilterMultiplePrefixParser tagParser = new FilterMultiplePrefixParser(PREFIX_TAG);
        tagParser.parse("friends");

        List<FilterPrefixParser> parsers = Arrays.asList(nameParser, salaryParser, tagParser);
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(parsers);

        assertFalse(predicate.test(
                new PersonBuilder().withName("Alice Bob").withSalary("40000").withTags("friends").build()));
    }
}
