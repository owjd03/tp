package seedu.address.logic.parser.filter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;


public class FilterContainsPrefixParserTest {

    private static final Function<Person, String> GET_NAME_STRING = p -> p.getName().fullName;
    private static final Function<Person, String> GET_ADDRESS_STRING = p -> p.getAddress().value;

    @Test
    public void constructor_nullPrefix_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterContainsPrefixParser(null, GET_NAME_STRING));
    }

    @Test
    public void constructor_nullFunction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterContainsPrefixParser(PREFIX_NAME, null));
    }

    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        assertEquals(PREFIX_NAME, parser.getPrefix());
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validKeyword_success() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser.parse("alice");
        assertTrue(parser.test(ALICE));
    }

    @Test
    public void parse_keywordWithMixedCase_storedAsLowerCase() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser.parse("ALiCe");
        assertTrue(parser.test(ALICE));
    }

    @Test
    public void parse_whitespaceArgs_success() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser.parse("   ");

        String expected = "seedu.address.logic.parser.filter.FilterContainsPrefixParser{prefix=n/, keyword=   }";

        assertEquals(expected, parser.toString());
        assertFalse(parser.test(ALICE));
    }

    @Test
    public void test_nameFilter() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);

        // Exact match (case-insensitive contains)
        parser.parse("alice pauline");
        assertTrue(parser.test(ALICE));
        assertFalse(parser.test(BENSON));

        // Partial match
        parser.parse("alice");
        assertTrue(parser.test(ALICE));
        assertFalse(parser.test(BENSON));

        // Case-insensitive match
        parser.parse("ALICE");
        assertTrue(parser.test(ALICE));

        // Non-matching keyword
        parser.parse("charlie");
        assertFalse(parser.test(ALICE));
        assertFalse(parser.test(BENSON));

        // Keyword present in another person
        parser.parse("benson");
        assertFalse(parser.test(ALICE));
        assertTrue(parser.test(BENSON));
    }

    @Test
    public void test_addressFilter() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_ADDRESS, GET_ADDRESS_STRING);

        parser.parse("jurong");
        assertTrue(parser.test(ALICE));
        assertFalse(parser.test(BENSON));

        parser.parse("clementi");
        assertFalse(parser.test(ALICE));
        assertTrue(parser.test(BENSON));

        parser.parse("ave 6");
        assertTrue(parser.test(ALICE));
        assertFalse(parser.test(BENSON));
    }

    @Test
    public void test_personFieldIsNull_returnsFalse() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, p -> null);
        parser.parse("anykeyword");
        assertFalse(parser.test(ALICE));
    }

    @Test
    public void test_calledBeforeParse_throwsNullPointerException() {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        assertThrows(NullPointerException.class, () -> parser.test(ALICE));
    }

    @Test
    public void equals() throws ParseException {
        FilterContainsPrefixParser parser1 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser1.parse("alice");
        FilterContainsPrefixParser parser2 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser2.parse("alice");
        FilterContainsPrefixParser parser3 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser3.parse("benson");
        FilterContainsPrefixParser addressParser = new FilterContainsPrefixParser(PREFIX_ADDRESS,
                GET_ADDRESS_STRING);
        addressParser.parse("jurong");

        // same object -> returns true
        assertTrue(parser1.equals(parser1));

        // same values -> returns true
        assertTrue(parser1.equals(parser2));

        // different values -> returns false
        assertFalse(parser1.equals(parser3));

        // different prefix -> returns false
        assertFalse(parser1.equals(addressParser));

        // different type -> returns false
        assertFalse(parser1.equals(1));

        // null -> returns false
        assertFalse(parser1.equals(null));
    }

    @Test
    public void equals_oneParsedOneUnparsed_returnsFalse() throws ParseException {
        FilterContainsPrefixParser parsedParser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parsedParser.parse("alice");
        FilterContainsPrefixParser unparsedParser = new FilterContainsPrefixParser(PREFIX_NAME,
                GET_NAME_STRING);
        assertFalse(parsedParser.equals(unparsedParser));
    }

    @Test
    public void hashCode_unparsedParser_success() {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);

        assertDoesNotThrow(() -> parser.hashCode());
    }


    @Test
    public void hashCode_consistentWithEquals() throws ParseException {
        FilterContainsPrefixParser parser1 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser1.parse("alice");
        FilterContainsPrefixParser parser2 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser2.parse("alice");
        FilterContainsPrefixParser parser3 = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser3.parse("benson");

        assertEquals(parser1.hashCode(), parser2.hashCode());
        assertNotEquals(parser1.hashCode(), parser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(PREFIX_NAME, GET_NAME_STRING);
        parser.parse("alice");
        String expected = "seedu.address.logic.parser.filter.FilterContainsPrefixParser{prefix=n/, keyword=alice}";
        assertEquals(expected, parser.toString());
    }
}
