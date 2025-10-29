package seedu.address.logic.parser.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FilterComparisonPrefixParserTest {

    private static final Function<Person, Double> GET_SALARY_DOUBLE =
            p -> p.getSalary().getNumericValue();
    private static final Function<Person, Double> GET_DEPENDENTS_DOUBLE =
            p -> (double) p.getDependents().getNumericValue();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED =
            p -> p.getSalary().isUnspecified();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

    @Test
    public void constructor_nullPrefix_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(null, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED));
    }

    @Test
    public void constructor_nullFunction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(PREFIX_SALARY, null, IS_SALARY_UNSPECIFIED));
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, null));
    }

    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        assertEquals(PREFIX_SALARY, parser.getPrefix());
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validSalaryInput_success() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);

        // Equals
        parser.parse("50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50001").build()));

        // Greater than
        parser.parse(">50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000.01").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000").build()));

        // Greater than or equals
        parser.parse(">=50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000").build()));
        assertTrue(parser.test(new PersonBuilder().withSalary("50000.01").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("49999.99").build()));

        // Less than
        parser.parse("<50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("49999.99").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000").build()));

        // Less than or equals
        parser.parse("<=50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000").build()));
        assertTrue(parser.test(new PersonBuilder().withSalary("49999.99").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000.01").build()));

        // With decimal
        parser.parse(">=50000.50");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000.50").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000.49").build()));

        // With space
        parser.parse(">= 50000");
        assertTrue(parser.test(new PersonBuilder().withSalary("50000").build()));

        // Unspecified salary
        parser.parse("unspecified");
        assertTrue(parser.test(new PersonBuilder().withSalary("Unspecified").build()));

        // Unspecified salary partial match
        parser.parse("uns");
        assertTrue(parser.test(new PersonBuilder().withSalary("Unspecified").build()));
    }

    @Test
    public void parse_validDependentsInput_success() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);

        // Contains
        parser.parse("2");
        assertTrue(parser.test(new PersonBuilder().withDependents(2).build()));
        assertFalse(parser.test(new PersonBuilder().withDependents(3).build()));

        // Greater than
        parser.parse(">2");
        assertFalse(parser.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(parser.test(new PersonBuilder().withDependents(3).build()));

        // Greater than or equals
        parser.parse(">=2");
        assertTrue(parser.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(parser.test(new PersonBuilder().withDependents(3).build()));
        assertFalse(parser.test(new PersonBuilder().withDependents(1).build()));

        // Less than
        parser.parse("<2");
        assertTrue(parser.test(new PersonBuilder().withDependents(1).build()));
        assertFalse(parser.test(new PersonBuilder().withDependents(2).build()));

        // Less than or equals
        parser.parse("<=2");
        assertTrue(parser.test(new PersonBuilder().withDependents(2).build()));
        assertTrue(parser.test(new PersonBuilder().withDependents(1).build()));
        assertFalse(parser.test(new PersonBuilder().withDependents(3).build()));

        // Zero dependents
        parser.parse("=0");
        assertTrue(parser.test(new PersonBuilder().withDependents(0).build()));
        assertFalse(parser.test(new PersonBuilder().withDependents(1).build()));

        // Unspecified dependents
        parser.parse("unspecified");
        assertTrue(parser.test(new PersonBuilder().withDependents(-1).build()));

        // Unspecified dependents partial match
        parser.parse("uns");
        assertTrue(parser.test(new PersonBuilder().withDependents(-1).build()));
    }

    @Test
    public void parse_invalidNumericalFormat_throwsParseException() {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        String integerErrorMessage = FilterComparisonPrefixParser.MESSAGE_DEPENDENTS_MUST_BE_INTEGER;

        // Negative numbers
        assertThrows(ParseException.class, () -> parser.parse("=-100"), integerErrorMessage);
        assertThrows(ParseException.class, () -> parser.parse(">-100"), integerErrorMessage);

        // More than two decimal places
        assertThrows(ParseException.class, () -> parser.parse(">100.123"), integerErrorMessage);

        // Trailing/ leading dot
        assertThrows(ParseException.class, () -> parser.parse("<100."), integerErrorMessage);
        assertThrows(ParseException.class, () -> parser.parse("=.10"), integerErrorMessage);

        // Missing value after operator
        assertThrows(ParseException.class, () ->
                parser.parse("< "),
                String.format(FilterComparisonPrefixParser.MESSAGE_MISSING_VALUE_AFTER_OPERATOR, PREFIX_SALARY, "<"));
    }

    @Test
    public void parse_dependentsWithDecimal_throwsParseException() {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        String expectedMessage = "Dependents value must be an integer and cannot be a decimal.";

        assertThrows(ParseException.class, () -> parser.parse("=2.5"), expectedMessage);
        assertThrows(ParseException.class, () -> parser.parse(">=1.0"), expectedMessage);
    }

    @Test
    public void parse_whitespaceArgs_parsesAsContainsLogic() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);

        parser.parse("   ");

        assertFalse(parser.test(new PersonBuilder().withSalary("Unspecified").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000").build()));

        String expected = "seedu.address.logic.parser.filter.FilterComparisonPrefixParser{prefix=s/, "
                + "user input=   }";
        assertEquals(expected, parser.toString());
    }

    @Test
    public void parse_pureTextKeyword_parsesAsContainsLogic() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);

        parser.parse("abc");

        assertFalse(parser.test(new PersonBuilder().withSalary("Unspecified").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("50000").build()));
    }

    @Test
    public void parse_invalidMixedInput_throwsParseException() {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        String invalidNumberSalaryMessage = FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY;
        String invalidNumberMessage = FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FOR_OPERATOR;

        // Multiple decimal points
        assertThrows(ParseException.class, () -> parser.parse("=50.00.00"),
                String.format(invalidNumberSalaryMessage, "50.00.00"));

        // Text after operator
        assertThrows(ParseException.class, () -> parser.parse(">= abc"),
                String.format(invalidNumberSalaryMessage, "abc"));
        // Invalid operator
        assertThrows(ParseException.class, () -> parser.parse(">>50000"),
                String.format(invalidNumberMessage, ">50000"));
    }

    @Test
    public void parse_validRegexWithExtraWhitespace_success() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);

        parser.parse(">=   50000");

        assertTrue(parser.test(new PersonBuilder().withSalary("50000").build()));
        assertTrue(parser.test(new PersonBuilder().withSalary("60000").build()));
        assertFalse(parser.test(new PersonBuilder().withSalary("40000").build()));
    }

    @Test
    public void test_personFieldIsNull_returnsFalse() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, p -> null, IS_SALARY_UNSPECIFIED);
        parser.parse(">=1000");
        assertFalse(parser.test(ALICE));

        parser.parse("1000");
        assertFalse(parser.test(ALICE));
    }

    @Test
    public void test_unspecifiedSearch_matchesUnspecifiedField() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse("spec");

        assertTrue(parser.test(new PersonBuilder().withDependents(-1).build())); // -1 is considered unspecified
        assertFalse(parser.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_numericalSearch_doesNotMatchUnspecifiedField() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        parser.parse(">=0");

        assertFalse(parser.test(new PersonBuilder().withDependents(-1).build())); // -1 is considered unspecified
        assertTrue(parser.test(new PersonBuilder().withDependents(0).build()));
        assertTrue(parser.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void equals() throws ParseException {
        FilterComparisonPrefixParser salaryParser1 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser1.parse(">=50000");
        FilterComparisonPrefixParser salaryParser2 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser2.parse(">=50000");
        FilterComparisonPrefixParser salaryParser3 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser3.parse("<50000");
        FilterComparisonPrefixParser dependentsParser =
                new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        dependentsParser.parse(">=2");

        // same object -> returns true
        assertTrue(salaryParser1.equals(salaryParser1));

        // same values -> returns true
        assertTrue(salaryParser1.equals(salaryParser2));

        // different values -> returns false
        assertFalse(salaryParser1.equals(salaryParser3));

        // different prefix -> returns false
        assertFalse(salaryParser1.equals(dependentsParser));

        // different type -> returns false
        assertFalse(salaryParser1.equals(1));

        // null -> returns false
        assertFalse(salaryParser1.equals(null));
    }

    @Test
    public void hashCode_consistentWithEquals() throws ParseException {
        FilterComparisonPrefixParser salaryParser1 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser1.parse(">=50000");
        FilterComparisonPrefixParser salaryParser2 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser2.parse(">=50000");
        FilterComparisonPrefixParser salaryParser3 =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser3.parse("<50000");

        assertEquals(salaryParser1.hashCode(), salaryParser2.hashCode());
        assertNotEquals(salaryParser1.hashCode(), salaryParser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse(">=50000");
        String expected = "seedu.address.logic.parser.filter.FilterComparisonPrefixParser{prefix=s/, "
                + "user input=>=50000}";
        assertEquals(expected, parser.toString());
    }
}
