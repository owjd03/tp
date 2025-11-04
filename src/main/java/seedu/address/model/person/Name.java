package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names can contain letters, numbers, spaces, the special characters -'/)( and should not be blank. \n"
                    + "If a name contains text that is a valid command prefix, enclose the name in one set of double "
                    + "quotes. \n"
                    + "Example: n/\"Peter s/o John\"";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    private static final String VALIDATION_REGEX = "[\\p{L}\\p{M}\\p{N}\\s'\\-\\.\\(\\)/]+";
    private static final String DELIMITERS = " -'/.()";
    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String trimmedName = name.trim();

        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);

        this.fullName = standardizeName(trimmedName);
    }

    /**
     * Standardizes the name by capitalizing the first letter of each word
     * and converting the rest of the letters to lowercase.
     *
     * @param name The name to be standardized.
     * @return The standardized name.
     */
    private static String standardizeName(String name) {
        if (name.isEmpty()) {
            return "";
        }

        StringBuilder standardizedName = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : name.toLowerCase().toCharArray()) {
            if (DELIMITERS.indexOf(c) != -1) {
                capitalizeNext = true;
                standardizedName.append(c);
            } else if (capitalizeNext && Character.isLetter(c)) {
                standardizedName.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                standardizedName.append(c);
            }
        }

        return standardizedName.toString().trim();
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
