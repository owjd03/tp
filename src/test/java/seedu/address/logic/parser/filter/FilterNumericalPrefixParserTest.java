package seedu.address.logic.parser.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FilterNumericalPrefixParserTest {

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
                new FilterNumericalPrefixParser(null, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED));
    }

    @Test
    public void constructor_nullFunction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterNumericalPrefixParser(PREFIX_SALARY, null, IS_SALARY_UNSPECIFIED));
        assertThrows(NullPointerException.class, () ->
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, null));
    }

    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        assertEquals(PREFIX_SALARY, parser.getPrefix());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        assertThrows(ParseException.class, () -> parser.parse(""),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + PREFIX_SALARY));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validSalaryInput_success() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);

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
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);

        // Equals
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
        parser.parse("0");
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
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        String expectedMessage = FilterNumericalPrefixParser.MESSAGE_INVALID_NUMERICAL_FORMAT;

        // Invalid characters
        assertThrows(ParseException.class, () -> parser.parse("50k"), expectedMessage);

        // Negative numbers
        assertThrows(ParseException.class, () -> parser.parse("-100"), expectedMessage);
        assertThrows(ParseException.class, () -> parser.parse(">-100"), expectedMessage);

        // More than two decimal places
        assertThrows(ParseException.class, () -> parser.parse("100.123"), expectedMessage);

        // Trailing/ leading dot
        assertThrows(ParseException.class, () -> parser.parse("100."), expectedMessage);
        assertThrows(ParseException.class, () -> parser.parse(".10"), expectedMessage);
    }

    @Test
    public void parse_dependentsWithDecimal_throwsParseException() {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
        String expectedMessage = "Dependents value must be an integer and cannot be a decimal.";

        assertThrows(ParseException.class, () -> parser.parse("2.5"), expectedMessage);
        assertThrows(ParseException.class, () -> parser.parse(">=1.0"), expectedMessage);
    }

    @Test
    public void test_personFieldIsNull_returnsFalse() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, p -> null, IS_SALARY_UNSPECIFIED);
        parser.parse(">=1000");
        assertFalse(parser.test(ALICE));
    }

    @Test
    public void equals() throws ParseException {
        FilterNumericalPrefixParser salaryParser1 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser1.parse(">=50000");
        FilterNumericalPrefixParser salaryParser2 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser2.parse(">=50000");
        FilterNumericalPrefixParser salaryParser3 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser3.parse("<50000");
        FilterNumericalPrefixParser dependentsParser =
                new FilterNumericalPrefixParser(PREFIX_DEPENDENTS, GET_DEPENDENTS_DOUBLE, IS_DEPENDENTS_UNSPECIFIED);
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
        FilterNumericalPrefixParser salaryParser1 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser1.parse(">=50000");
        FilterNumericalPrefixParser salaryParser2 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser2.parse(">=50000");
        FilterNumericalPrefixParser salaryParser3 =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        salaryParser3.parse("<50000");

        assertEquals(salaryParser1.hashCode(), salaryParser2.hashCode());
        assertNotEquals(salaryParser1.hashCode(), salaryParser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterNumericalPrefixParser parser =
                new FilterNumericalPrefixParser(PREFIX_SALARY, GET_SALARY_DOUBLE, IS_SALARY_UNSPECIFIED);
        parser.parse(">=50000");
        String expected = "seedu.address.logic.parser.filter.FilterNumericalPrefixParser{prefix=s/, "
                + "user input=>=50000}";
        assertEquals(expected, parser.toString());
    }
}
