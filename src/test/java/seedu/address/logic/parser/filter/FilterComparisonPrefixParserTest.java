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

    private static final Function<Person, String> GET_SALARY =
            p -> p.getSalary().toString();
    private static final Function<Person, String> GET_DEPENDENTS =
            p -> p.getDependents().toString();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED =
            p -> p.getSalary().isUnspecified();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

    //----- Constructor Tests -----
    @Test
    public void constructor_nullPrefix_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(null, GET_SALARY, IS_SALARY_UNSPECIFIED));
    }

    @Test
    public void constructor_nullFunction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(PREFIX_SALARY, null, IS_SALARY_UNSPECIFIED));
        assertThrows(NullPointerException.class, () ->
                new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY, null));
    }

    //----- GetPrefix Tests -----
    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        assertEquals(PREFIX_SALARY, parser.getPrefix());
    }

    //----- Parse and Test Logic (Valid Inputs) -----
    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validSalaryInput_success() throws ParseException {
        // Equals
        assertSalaryComparison("=50000", "50000", true);
        assertSalaryComparison("=50000", "50001", false);

        // Greater than
        assertSalaryComparison(">50000", "50000.01", true);
        assertSalaryComparison(">50000", "50000", false);

        // Greater than or equals
        assertSalaryComparison(">=50000", "50000", true);
        assertSalaryComparison(">=50000", "50000.01", true);
        assertSalaryComparison(">=50000", "49999.99", false);

        // Less than
        assertSalaryComparison("<50000", "49999.99", true);
        assertSalaryComparison("<50000", "50000", false);

        // Less than or equals
        assertSalaryComparison("<=50000", "50000", true);
        assertSalaryComparison("<=50000", "49999.99", true);
        assertSalaryComparison("<=50000", "50000.01", false);

        // With decimal
        assertSalaryComparison(">=50000.50", "50000.50", true);
        assertSalaryComparison(">=50000.50", "50000.49", false);

        // With space
        assertSalaryComparison(">= 50000", "50000", true);

        // Unspecified salary
        assertSalaryContains("unspecified", "Unspecified", true);

        // Unspecified salary partial match
        assertSalaryContains("uns", "Unspecified", true);
    }

    @Test
    public void parse_validDependentsInput_success() throws ParseException {
        // Contains
        assertDependentsComparison("=2", 2, true);
        assertDependentsComparison("=2", 3, false);

        // Greater than
        assertDependentsComparison(">2", 2, false);
        assertDependentsComparison(">2", 3, true);

        // Greater than or equals
        assertDependentsComparison(">=2", 2, true);
        assertDependentsComparison(">=2", 3, true);
        assertDependentsComparison(">=2", 1, false);

        // Less than
        assertDependentsComparison("<2", 1, true);
        assertDependentsComparison("<2", 2, false);

        // Less than or equals
        assertDependentsComparison("<=2", 2, true);
        assertDependentsComparison("<=2", 1, true);
        assertDependentsComparison("<=2", 3, false);

        // Zero dependents
        assertDependentsComparison("=0", 0, true);
        assertDependentsComparison("=0", 1, false);

        // Unspecified dependents
        assertDependentsContains("unspecified", -1, true);

        // Unspecified dependents partial match
        assertDependentsContains("uns", -1, true);
    }

    //----- Parse Logic (Invalid Inputs) -----
    @Test
    public void parse_missingValueAfterOperator_throwsParseException() {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        String expectedMessage = String.format(FilterComparisonPrefixParser.MESSAGE_MISSING_VALUE_AFTER_OPERATOR,
                "<", PREFIX_SALARY);
        assertThrows(ParseException.class, () -> parser.parse("< "), expectedMessage);
    }

    @Test
    public void parse_invalidNumberForOperator_throwsParseException() {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        String expectedMessage = String.format(FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FOR_OPERATOR, "abc");
        assertThrows(ParseException.class, () -> parser.parse(">= abc"), expectedMessage);
    }

    @Test
    public void parse_invalidSalaryFormat_throwsParseException() {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        String invalidNumberSalaryMessage = FilterComparisonPrefixParser.MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY;

        assertThrows(ParseException.class, () -> parser.parse(">100.123"),
                String.format(invalidNumberSalaryMessage, "100.123")); // More than two decimal places
        assertThrows(ParseException.class, () -> parser.parse("<100."),
                String.format(invalidNumberSalaryMessage, "100.")); // Trailing dot
        assertThrows(ParseException.class, () -> parser.parse("=.10"),
                String.format(invalidNumberSalaryMessage, ".10")); // Leading dot
        assertThrows(ParseException.class, () -> parser.parse("=50.00.00"),
                String.format(invalidNumberSalaryMessage, "50.00.00")); // Multiple decimal points
    }

    @Test
    public void parse_dependentsWithDecimal_throwsParseException() {
        FilterComparisonPrefixParser parser = createDependentsTestParser();
        String expectedMessage = FilterComparisonPrefixParser.MESSAGE_DEPENDENTS_MUST_BE_INTEGER;

        assertThrows(ParseException.class, () -> parser.parse("=2.5"), expectedMessage);
        assertThrows(ParseException.class, () -> parser.parse(">=1.0"), expectedMessage);
    }

    //----- Test Logic Edge Cases -----
    @Test
    public void test_personFieldIsNull_returnsFalse() throws ParseException {
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(PREFIX_SALARY, p -> null, IS_SALARY_UNSPECIFIED);
        // Comparison logic
        parser.parse(">=1000");
        assertFalse(parser.test(ALICE));

        // Contains logic
        parser.parse("1000");
        assertFalse(parser.test(ALICE));
    }

    @Test
    public void test_unspecifiedSearch_matchesUnspecifiedField() throws ParseException {
        FilterComparisonPrefixParser parser = createDependentsTestParser();
        parser.parse("spec");

        assertTrue(parser.test(new PersonBuilder().withDependents(-1).build())); // -1 is considered unspecified
        assertFalse(parser.test(new PersonBuilder().withDependents(2).build()));
    }

    @Test
    public void test_numericalSearch_doesNotMatchUnspecifiedField() throws ParseException {
        FilterComparisonPrefixParser parser = createDependentsTestParser();
        parser.parse(">=0");

        assertFalse(parser.test(new PersonBuilder().withDependents(-1).build())); // -1 is considered unspecified
        assertTrue(parser.test(new PersonBuilder().withDependents(0).build()));
    }

    //----- equals, hashCode, toString Tests -----
    @Test
    public void equals_variousScenarios_correctResults() throws ParseException {
        FilterComparisonPrefixParser salaryParser1 = createSalaryTestParser();
        salaryParser1.parse(">=50000");
        FilterComparisonPrefixParser salaryParser2 = createSalaryTestParser();
        salaryParser2.parse(">=50000");
        FilterComparisonPrefixParser salaryParser3 = createSalaryTestParser();
        salaryParser3.parse("<50000");
        FilterComparisonPrefixParser dependentsParser = createDependentsTestParser();
        dependentsParser.parse(">=50000"); // Same keyword, different prefix

        // same object -> returns true
        assertEquals(salaryParser1, salaryParser1);

        // same values -> returns true
        assertEquals(salaryParser1, salaryParser2);

        // different values -> returns false
        assertNotEquals(salaryParser1, salaryParser3);

        // different prefix -> returns false
        assertNotEquals(salaryParser1, dependentsParser);

        // different type -> returns false
        assertNotEquals(1, salaryParser1);

        // null -> returns false
        assertNotEquals(null, salaryParser1);
    }

    @Test
    public void hashCode_consistentWithEquals() throws ParseException {
        FilterComparisonPrefixParser salaryParser1 = createSalaryTestParser();
        salaryParser1.parse(">=50000");
        FilterComparisonPrefixParser salaryParser2 = createSalaryTestParser();
        salaryParser2.parse(">=50000");
        FilterComparisonPrefixParser salaryParser3 = createSalaryTestParser();
        salaryParser3.parse("<50000");

        assertEquals(salaryParser1.hashCode(), salaryParser2.hashCode());
        assertNotEquals(salaryParser1.hashCode(), salaryParser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        parser.parse(">=50000");
        String expected = "seedu.address.logic.parser.filter.FilterComparisonPrefixParser{prefix=s/, "
                + "user input=>=50000}";
        assertEquals(expected, parser.toString());
    }

    //----- Helper Methods -----
    private FilterComparisonPrefixParser createSalaryTestParser() {
        return new FilterComparisonPrefixParser(PREFIX_SALARY, GET_SALARY, IS_SALARY_UNSPECIFIED);
    }

    private FilterComparisonPrefixParser createDependentsTestParser() {
        return new FilterComparisonPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS, IS_DEPENDENTS_UNSPECIFIED);
    }

    private void assertSalaryComparison(String input, String personSalary, boolean expected) throws ParseException {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        parser.parse(input);
        Person person = new PersonBuilder().withSalary(personSalary).build();
        assertEquals(expected, parser.test(person));
    }

    private void assertSalaryContains(String input, String personSalary, boolean expected) throws ParseException {
        FilterComparisonPrefixParser parser = createSalaryTestParser();
        parser.parse(input);
        Person person = new PersonBuilder().withSalary(personSalary).build();
        assertEquals(expected, parser.test(person));
    }

    private void assertDependentsComparison(String input,
                                            int personDependents,
                                            boolean expected) throws ParseException {
        FilterComparisonPrefixParser parser = createDependentsTestParser();
        parser.parse(input);
        Person person = new PersonBuilder().withDependents(personDependents).build();
        assertEquals(expected, parser.test(person));
    }

    private void assertDependentsContains(String input, int personDependents, boolean expected) throws ParseException {
        FilterComparisonPrefixParser parser = createDependentsTestParser();
        parser.parse(input);
        Person person = new PersonBuilder().withDependents(personDependents).build();
        assertEquals(expected, parser.test(person));
    }
}
