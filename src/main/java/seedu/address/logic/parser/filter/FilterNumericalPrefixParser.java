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
 * These prefixes support comparison operators (>, <, >=, <=, =).
 */
public class FilterNumericalPrefixParser implements FilterPrefixParser {

    public static final String MESSAGE_INVALID_NUMERICAL_FORMAT =
        "Numerical filter values must be non-negative numbers, "
            + "optionally preceded by an operator (>, <, >=, <=).\n"
            + "No operator means filtering for a Person whose salary = your input salary"
            + "dep/ takes in integer values only while s/ allows decimal values.\n"
            + "Example: s/>=50000.50 or dep/<5";

    /**
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     * https://www.geeksforgeeks.org/dsa/write-regular-expressions/
     * ^ to indicate start of pattern, $ to indicate end of pattern
     * Group 1: ([<>]=?)? MEANS (< OR >) AND = (optional), whole thing is optional
     * Accepted: >=, >, <, <=, NOTHING
     * \s* MEANS whitespace, extra \ to escape character in string
     * Group 2: (\d+(\.\d{1,2})?) MEANS any number of digits,
     *      followed by '.' and up to 2 digits, rejects 0 digits. This '.' character and beyond are optional
     * Accepted 00000.12, 123.0, 123.12, 123
     */
    private static final String VALIDATION_REGEX = "^([<>]=?)?\\s*(\\d+(\\.\\d{1,2})?)$";
    private static final Pattern PATTERN = Pattern.compile(VALIDATION_REGEX);

    private final Prefix prefix;
    private final Function<Person, Double> getPersonField;
    private String filterString; // To compare FilterNumericalPrefixParser since Predicate does not have an equal method
    private Predicate<Double> predicate;

    /**
     * Constructs a {@code FilterNumericalPrefixParser}.
     *
     * @param prefix The numerical prefix to handle.
     * @param getPersonField A function to get the relevant Double attribute from a Person.
     */
    public FilterNumericalPrefixParser(Prefix prefix, Function<Person, Double> getPersonField) {
        requireAllNonNull(prefix, getPersonField);
        this.prefix = prefix;
        this.getPersonField = getPersonField;
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

        this.filterString = args;
        Matcher matcher = PATTERN.matcher(args);
        if (!matcher.matches()) {
            throw new ParseException(MESSAGE_INVALID_NUMERICAL_FORMAT);
        }

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

    @Override
    public boolean test(Person person) {
        Double personValue = this.getPersonField.apply(person);
        if (personValue == null) {
            return false;
        }
        return this.predicate.test(personValue);
    }

    @Override
    public String toString() {
        // Can't use toString on predicate
        return new ToStringBuilder(this)
                .add("prefix", this.prefix)
                .add("user input", this.filterString)
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
                && this.filterString.equals(otherParser.filterString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.filterString);
    }
}
