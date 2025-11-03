package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's occupation in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidOccupation(String)}
 */
public class Occupation {

    public static final String MESSAGE_CONSTRAINTS = "Occupation must be non-empty or be declared as 'Unspecified' "
            + "(case-insensitive). \n"
            + "If an occupation contains text that is a valid command prefix, enclose the occupation in one set of "
            + "double quotes. \n"
            + "Example: occ/\"Pilot @ S/Airlines\"";

    /*
     * The first character of the occupation must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public static final String UNSPECIFIED_VALUE = "Unspecified";

    private final String value;

    /**
     * Constructs an {@code Occupation}.
     *
     * @param occupation A valid occupation.
     */
    public Occupation(String occupation) {
        requireNonNull(occupation);
        checkArgument(isValidOccupation(occupation), MESSAGE_CONSTRAINTS);

        if (occupation.equalsIgnoreCase(UNSPECIFIED_VALUE)) {
            this.value = UNSPECIFIED_VALUE;
        } else {
            value = occupation;
        }
    }

    /**
     * @return The raw occupation value as a string.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Static factory method for creating the default "Unspecified" Occupation
     * @return An Occupation object with value "Unspecified".
     */
    public static Occupation createUnspecified() {
        return new Occupation(UNSPECIFIED_VALUE);
    }

    /**
     * Returns true if a given string is a valid occupation.
     */
    public static boolean isValidOccupation(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Occupation)) {
            return false;
        }

        Occupation otherOccupation = (Occupation) other;
        return value.equals(otherOccupation.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
