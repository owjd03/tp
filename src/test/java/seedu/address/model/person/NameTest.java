package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName_nullName_throwsNullPointerException() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));
    }

    @Test
    public void isValidName_emptyOrBlank_returnsFalse() {
        assertFalse(Name.isValidName(""));
        assertTrue(Name.isValidName(" "));
    }

    @Test
    public void isValidName_validNames_returnsTrue() {
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
        assertTrue(Name.isValidName("peter (peter)")); // brackets
    }

    @Test
    public void isValidName_invalidNames_returnsFalse() {
        assertFalse(Name.isValidName("^*!")); // contains only invalid delimiters
        assertFalse(Name.isValidName("peter*")); // contains alphanumeric characters and invalid delimiters
    }

    @Test
    public void isValidName_validDelimiters_returnsTrue() {
        assertTrue(Name.isValidName("Anne-Marie")); // hyphen
        assertTrue(Name.isValidName("D'Angelo")); // apostrophe
        assertTrue(Name.isValidName("Dr. Strange")); // dot
        assertTrue(Name.isValidName("Anne-Marie D'Angelo Jr.")); // multiple delimiters
    }

    @Test
    public void isValidName_validSlashPatterns_returnsTrue() {
        assertTrue(Name.isValidName("john s/o doe"));
        assertTrue(Name.isValidName("JOHN S/O DOE")); // all caps
        assertTrue(Name.isValidName("muthu A/L ganesan")); // mixed case
        assertTrue(Name.isValidName("peter z/ Boss"));
        assertTrue(Name.isValidName("john s/o/p doe"));
        assertTrue(Name.isValidName("john s / o doe"));
    }

    @Test
    public void isValidName_validUnicode_returnsTrue() {
        assertTrue(Name.isValidName("李小龙")); // chinese
        assertTrue(Name.isValidName("박서준")); // korean
        assertTrue(Name.isValidName("José García")); // accented
        assertTrue(Name.isValidName("李小龙 2nd-Gen.")); // mixed Unicode and delimiters
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
