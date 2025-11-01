package seedu.address.logic.parser.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for {@code FilterTagParser}.
 */
public class FilterTagParserTest {

    @Test
    public void constructor_nullPrefix_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterTagParser(null));
    }

    @Test
    public void getPrefix_returnsCorrectPrefix() {
        FilterTagParser parser = createFilterTagParser();
        assertEquals(PREFIX_TAG, parser.getPrefix());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        FilterTagParser parser = createFilterTagParser();
        assertThrows(ParseException.class, () -> parser.parse(""), Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        FilterTagParser parser = createFilterTagParser();
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_validSingleTag_success() throws ParseException {
        FilterTagParser parser = createFilterTagParser("friends");
        assertTrue(parser.test(new PersonBuilder().withTags("friends").build()));
        assertFalse(parser.test(new PersonBuilder().withTags("colleagues").build()));
    }

    @Test
    public void parse_validMultipleTags_success() throws ParseException {
        FilterTagParser parser = createFilterTagParser("friends", "family");

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
        FilterTagParser parser = createFilterTagParser("friends", "owesmoney");

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
        FilterTagParser parser = createFilterTagParser("FRIENDS", "OwesMoney");

        assertTrue(parser.test(BENSON));
    }

    @Test
    public void test_matchingTagsPartialMatch_returnsTrue() throws ParseException {
        FilterTagParser parser = createFilterTagParser("friend");

        assertTrue(parser.test(ALICE));
    }

    @Test
    public void test_noTagsParsed_returnsTrue() {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        // If no tags are parsed, it should be vacuously true
        assertTrue(parser.test(ALICE));
        assertTrue(parser.test(BENSON));
        assertTrue(parser.test(new PersonBuilder().withTags().build()));
    }

    @Test
    public void equals_variousScenarios_correctResults() throws ParseException {
        FilterTagParser parser1 = createFilterTagParser("friends", "family");
        FilterTagParser parser2 = createFilterTagParser("friends", "family");
        FilterTagParser parser3 = createFilterTagParser("friends");

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
        FilterTagParser parser1 = createFilterTagParser("friends", "family");
        FilterTagParser parser2 = createFilterTagParser("friends", "family");
        FilterTagParser parser3 = createFilterTagParser("friends");

        assertEquals(parser1.hashCode(), parser2.hashCode());
        assertNotEquals(parser1.hashCode(), parser3.hashCode());
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() throws ParseException {
        FilterTagParser parser = createFilterTagParser("friends", "family");
        String actual = parser.toString();

        // Check for presence of prefix and both tags, order of tags in set is not guaranteed
        assertTrue(actual.contains("prefix=t/"));
        assertTrue(actual.contains("parsedTags="));
        assertTrue(actual.contains("friends"));
        assertTrue(actual.contains("family"));
    }

    //----- Helper Methods -----

    /**
     * Creates a FilterTagParser and parses the given keywords into it.
     */
    private FilterTagParser createFilterTagParser(String... keywords) {
        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        try {
            for (String keyword : keywords) {
                parser.parse(keyword);
            }
        } catch (ParseException e) {
            // Should not happen in valid test cases
            throw new RuntimeException(e);
        }
        return parser;
    }
}
