package seedu.address.logic.parser.filter;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Parses numerical prefixes (e.g., salary, dependents) for the filter command.
 * This parser implements a hybrid logic:
 * 1. It first attempts to parse the input as a number.
 *    - If an operator (>, <, >=, <=) is present, it creates a comparison predicate.
 *    - If no operator is present, it defaults to an exact equality check.
 * 2. If the input cannot be parsed as a number (e.g., it contains text),
 *    it checks if the input contains a number (e.g. it is a mixture of text and numbers such as 50k).
 *    An exception is thrown in this case.
 * 3. If not case 1 or 2, it falls back to a case-insensitive keyword search. This is primarily
 *    used to find persons with "unspecified" values for these fields.
 */
public class FilterNumericalPrefixParser implements FilterPrefixParser {

    public static final String MESSAGE_INVALID_NUMERICAL_FORMAT =
            "For numerical fields, provide a number with an optional comparison operator (>, <, >=, <=).\n"
            + "If no operator is used, it will search for an exact match (e.g., 's/5000').\n"
            + "To find contacts with an unspecified value, use the keyword 'unspecified'.\n"
            + "dep/ only accepts whole numbers. s/ accepts up to two decimal places.\n"
            + "Example: s/>=50000 or dep/<3 or s/unspecified";

    /**
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     * https://www.geeksforgeeks.org/dsa/write-regular-expressions/
     * ^ to indicate start of pattern, $ to indicate end of pattern
     * Group 1: ([<>]=?) MEANS (< OR >) AND = (optional), Group 1 is optional.
     * Accepted: >=, >, <, <=, NOTHING
     * \s* MEANS whitespace, extra \ to escape character in string
     * Group 2: (\d+(\.\d{1,2})?) MEANS any number of digits,
     *      followed by '.' and up to 2 digits, rejects 0 digits. This '.' character and beyond are optional
     * Accepted 00000.12, 123.0, 123.12, 123
     */
    private static final String VALIDATION_REGEX = "^([<>]=?)?\\s*(\\d+(\\.\\d{1,2})?)$";
    private static final String CONTAINS_DIGIT_REGEX = "^.*\\d+.*$";
    private static final Pattern PATTERN = Pattern.compile(VALIDATION_REGEX);

    private final Prefix prefix;
    private final Function<Person, Double> getPersonField;
    private final Function<Person, Boolean> isPersonFieldUnspecified;
    private String keyword;
    private Predicate<Double> predicate;
    private boolean isContainsLogic;

    /**
     * Constructs a {@code FilterNumericalPrefixParser}.
     *
     * @param prefix The numerical prefix to handle.
     * @param getPersonField A function to get the relevant Double attribute from a Person.
     */
    public FilterNumericalPrefixParser(Prefix prefix,
                                       Function<Person, Double> getPersonField,
                                       Function<Person, Boolean> isPersonFieldUnspecified) {
        requireAllNonNull(prefix, getPersonField, isPersonFieldUnspecified);
        this.prefix = prefix;
        this.getPersonField = getPersonField;
        this.isPersonFieldUnspecified = isPersonFieldUnspecified;
        this.isContainsLogic = true;
    }

    @Override
    public Prefix getPrefix() {
        return prefix;
    }

    @Override
    public void parse(String args) throws ParseException {
        requireNonNull(args);
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Empty keyword for: " + this.prefix)
            );
        }
        this.keyword = args.toLowerCase();

        Matcher matcher = PATTERN.matcher(args);
        if (matcher.matches()) {
            this.isContainsLogic = false;
            parseComparisonLogic(matcher);
            return;
        }

        // The user might input s/50k and think it is perfectly valid.
        // So an error should be thrown as long as the input contains a digit.
        boolean containsDigit = args.matches(CONTAINS_DIGIT_REGEX);
        if (containsDigit) {
            throw new ParseException(MESSAGE_INVALID_NUMERICAL_FORMAT);
        }
        this.isContainsLogic = true;
    }

    @Override
    public boolean test(Person person) {
        if (this.isContainsLogic) {
            return this.isPersonFieldUnspecified.apply(person)
                    && "unspecified".contains(this.keyword.toLowerCase());
        }
        Double personValue = this.getPersonField.apply(person);
        if (personValue == null) {
            return false;
        }

        /*
        There is a need to ensure that the peron's field is unspecified here again
        as the Dependents field stores a value of -1 if unspecified.
         */
        return !this.isPersonFieldUnspecified.apply(person) && this.predicate.test(personValue);
    }

    @Override
    public String toString() {
        // Can't use toString on predicate
        return new ToStringBuilder(this)
                .add("prefix", this.prefix)
                .add("user input", this.keyword)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterNumericalPrefixParser)) {
            return false;
        }

        FilterNumericalPrefixParser otherParser = (FilterNumericalPrefixParser) other;

        // Can't check if 2 predicates are equal
        return this.prefix.equals(otherParser.prefix)
                && this.keyword.equals(otherParser.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.keyword);
    }

    private void parseComparisonLogic(Matcher matcher) throws ParseException {
        String operator = matcher.group(1) == null ? "" : matcher.group(1);
        String value = matcher.group(2);

        // Ensure that value for dependents prefix is not a decimal number
        if (this.prefix.equals(PREFIX_DEPENDENTS) && value.contains(".")) {
            throw new ParseException("Dependents value must be an integer and cannot be a decimal.");
        }
        double valueToCompare = Double.parseDouble(value);

        switch (operator) {
        case ">":
            this.predicate = personValue -> personValue > valueToCompare;
            break;
        case ">=":
            this.predicate = personValue -> personValue >= valueToCompare;
            break;
        case "<":
            this.predicate = personValue -> personValue < valueToCompare;
            break;
        case "<=":
            this.predicate = personValue -> personValue <= valueToCompare;
            break;
        default:
            this.predicate = personValue -> personValue == valueToCompare;
        }
    }
}
