package seedu.address.logic.parser.filter;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;

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
 * Parses prefixes that involve numerical comparison (e.g., salary, dependents) or contains-search.
 * This parser implements a hybrid logic:
 * 1. It checks for an optional operator (>, <, >=, <=, =).
 * 2. If an operator is present, it parses the rest of the string as a number and
 * creates a comparison predicate.
 * 3. If no operator is present, it defaults to a case-insensitive "contains" search.
 * This "contains" search works on the string representation of the number, or
 * on the "unspecified" keyword.
 */
public class FilterNumericalPrefixParser implements FilterPrefixParser {

    public static final String MESSAGE_MISSING_VALUE_AFTER_OPERATOR =
            "Missing number after operator '%s' for prefix %s.";
    public static final String MESSAGE_INVALID_NUMBER_FOR_OPERATOR =
            "Invalid number for comparison: '%s'. A valid number is required after an operator.";
    public static final String MESSAGE_DEPENDENTS_MUST_BE_INTEGER =
            "Dependents value must be a whole number (e.g., '2') and cannot be a decimal (e.g., '2.5').";
    public static final String MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY =
            "Invalid number format for salary: '%s'.\n"
                    + "Must have a leading digit and up to two decimal places (e.g., '5000' or '5000.50').";

    private static final String VALIDATION_REGEX = "^(([<>]=?)|=)?\\s*(.*)$";
    private static final Pattern PATTERN = Pattern.compile(VALIDATION_REGEX);

    private static final String VALID_NUMBER_REGEX = "^\\d+(\\.\\d{1,2})?$";

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
    }

    @Override
    public Prefix getPrefix() {
        return this.prefix;
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
        Matcher matcher = PATTERN.matcher(this.keyword);

        if (!matcher.matches()) {
            throw new ParseException("Unexpected parser error");
        }

        String operator = matcher.group(1);
        String value = matcher.group(3).trim();

        if (value.isEmpty() && operator != null) {
            String errorMessage = String.format(MESSAGE_MISSING_VALUE_AFTER_OPERATOR, operator, this.prefix);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, errorMessage));
        }

        if (operator != null) {
            this.isContainsLogic = false;
            parseComparisonLogic(operator, value);
        } else {
            this.isContainsLogic = true;
        }
    }

    private void parseComparisonLogic(String operator, String value) throws ParseException {
        double valueToCompare;
        try {
            valueToCompare = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            String errorMessage = String.format(MESSAGE_INVALID_NUMBER_FOR_OPERATOR, value);
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, errorMessage));
        }

        // Ensure that value for dependents prefix is not a decimal number
        if (this.prefix.equals(PREFIX_DEPENDENTS) && !value.matches("^\\d+$")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_DEPENDENTS_MUST_BE_INTEGER));
        }

        // Ensure that the value for salary prefix has a leading digit and up to two decimal places
        if (this.prefix.equals(PREFIX_SALARY) && !value.matches(VALID_NUMBER_REGEX)) {
            String errorMessage = String.format(MESSAGE_INVALID_NUMBER_FORMAT_FOR_SALARY, value);
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, errorMessage));
        }

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
        case "=":
        default:
            this.predicate = personValue -> personValue == valueToCompare;
        }
    }

    @Override
    public boolean test(Person person) {
        if (this.isContainsLogic) {
            return testContainsLogic(person);
        }
        return testComparisonLogic(person);
    }

    private boolean testContainsLogic(Person person) {
        // Perons's field is unspecified
        if (this.isPersonFieldUnspecified.apply(person)) {
            return "unspecified".contains(this.keyword);
        }

        Double personValue = this.getPersonField.apply(person);
        if (personValue == null) {
            return false;
        }
        return personValue.toString().contains(this.keyword);
    }

    private boolean testComparisonLogic(Person person) {
        // User is not filtering for "unspecified", so Person whose field is "unspecified" returns false
        if (this.isPersonFieldUnspecified.apply(person)) {
            return false;
        }

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
}
