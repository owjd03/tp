package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

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

        // Split each word by one or more spaces
        String[] words = name.split("\\s+");
        StringBuilder standardizedName = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            standardizedName.append(Character.toUpperCase(word.charAt(0)));
            standardizedName.append(word.substring(1).toLowerCase());
            standardizedName.append(" ");
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
