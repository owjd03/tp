package seedu.address.logic.parser.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class FilterMultiplePrefixParserTest {

    @Test
    public void constructor_nullPrefix_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterMultiplePrefixParser(null));
    }

    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        assertEquals(PREFIX_TAG, parser.getPrefix());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        assertThrows(ParseException.class, () -> parser.parse(""),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + PREFIX_TAG));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validSingleTag_success() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(new Tag("friends"));
        assertTrue(parser.test(new PersonBuilder().withTags("friends").build()));
        assertFalse(parser.test(new PersonBuilder().withTags("colleagues").build()));
    }

    @Test
    public void parse_validMultipleTags_success() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("family");

        // Person with both tags
        assertTrue(parser.test(new PersonBuilder().withTags("friends", "family").build()));
        // Person with only one tag
        assertFalse(parser.test(new PersonBuilder().withTags("friends").build()));
        // Person with no tags
        assertFalse(parser.test(new PersonBuilder().withTags().build()));
    }

    // test() method tests
    @Test
    public void test_matchingTags_returnsTrue() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("owesmoney");

        // Person has all specified tags
        assertTrue(parser.test(BENSON));

        // Person has only one of the specified tags
        assertFalse(parser.test(ALICE));

        // Person has no matching tags
        Person charlie = new PersonBuilder().withTags("colleagues").build();
        assertFalse(parser.test(charlie));

        // Person has no tags
        Person david = new PersonBuilder().withTags().build();
        assertFalse(parser.test(david));
    }

    @Test
    public void test_matchingTagsCaseInsensitive_returnsTrue() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("FRIENDS");
        parser.parse("OwesMoney");

        assertTrue(parser.test(BENSON));
    }

    @Test
    public void test_matchingTagsPartialMatch_returnsTrue() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friend");

        assertTrue(parser.test(ALICE));
    }

    @Test
    public void test_noTagsParsed_returnsTrue() {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        // If no tags are parsed, it should be vacuously true
        assertTrue(parser.test(ALICE));
        assertTrue(parser.test(BENSON));
        assertTrue(parser.test(new PersonBuilder().withTags().build()));
    }

    @Test
    public void equals() throws ParseException {
        FilterMultiplePrefixParser parser1 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser1.parse("friends");
        parser1.parse("family");

        FilterMultiplePrefixParser parser2 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser2.parse("friends");
        parser2.parse("family");

        FilterMultiplePrefixParser parser3 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser3.parse("friends");

        // same object -> returns true
        assertTrue(parser1.equals(parser1));

        // same values -> returns true
        assertTrue(parser1.equals(parser2));

        // different values -> returns false
        assertFalse(parser1.equals(parser3));

        // different type -> returns false
        assertFalse(parser1.equals(1));

        // null -> returns false
        assertFalse(parser1.equals(null));
    }

    @Test
    public void hashCode_consistentWithEquals() throws ParseException {
        FilterMultiplePrefixParser parser1 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser1.parse("friends");
        parser1.parse("family");

        FilterMultiplePrefixParser parser2 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser2.parse("friends");
        parser2.parse("family");

        FilterMultiplePrefixParser parser3 = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser3.parse("friends");

        assertEquals(parser1.hashCode(), parser2.hashCode());
        assertNotEquals(parser1.hashCode(), parser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(PREFIX_TAG);
        parser.parse("friends");
        parser.parse("family");
        String expected = "seedu.address.logic.parser.filter.FilterMultiplePrefixParser{prefix=t/, "
            + "parsedTags=[[family], [friends]]}";
        String actual = parser.toString();
        assertEquals(expected, actual);
    }
}
