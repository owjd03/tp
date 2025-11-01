package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Represents a Person's date of birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    public static final String MESSAGE_CONSTRAINTS = "Date of birth must be a valid date in the format yyyy-MM-dd, "
            + "be a past or present date, "
            + "or be declared as 'Unspecified' (case-insensitive).";

    public static final String UNSPECIFIED_VALUE = "Unspecified";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private final String value;

    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth in yyyy-MM-dd format.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDateOfBirth(dateOfBirth), MESSAGE_CONSTRAINTS);

        if (dateOfBirth.equalsIgnoreCase(UNSPECIFIED_VALUE)) {
            this.value = UNSPECIFIED_VALUE;
        } else {
            value = dateOfBirth;
        }
    }

    /**
     * @return The raw date of birth value as a string.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Static factory method for creating the default "Unspecified" DateOfBirth
     * @return A DateOfBirth object with value "Unspecified".
     */
    public static DateOfBirth createUnspecified() {
        return new DateOfBirth(UNSPECIFIED_VALUE);
    }

    /**
     * Parses the string to a LocalDate and checks if it is an invalid format or is in the future.
     * Returns true if a given string is a valid date of birth.
     */
    public static boolean isValidDateOfBirth(String test) {
        if (test.equalsIgnoreCase(UNSPECIFIED_VALUE)) {
            return true;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(test, FORMATTER);
            return !(parsedDate.isAfter(LocalDate.now()));
        } catch (Exception e) {
            return false;
        }
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
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDateOfBirth = (DateOfBirth) other;
        return value.equals(otherDateOfBirth.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
