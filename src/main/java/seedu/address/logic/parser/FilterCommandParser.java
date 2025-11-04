package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterComparisonPrefixParser;
import seedu.address.logic.parser.filter.FilterContainsPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.logic.parser.filter.FilterTagParser;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    public static final String MESSAGE_MISSING_KEYWORDS = "Missing keyword for the following %s: %s";

    private static final Function<Person, String> GET_ADDRESS = p -> p.getAddress().toString();
    private static final Function<Person, String> GET_DOB = p -> p.getDateOfBirth().toString();
    private static final Function<Person, String> GET_EMAIL = p -> p.getEmail().toString();
    private static final Function<Person, String> GET_INSURANCE_PACKAGE =
            p -> p.getInsurancePackage().getPackageName();
    private static final Function<Person, String> GET_MARITAL_STATUS = p -> p.getMaritalStatus().toString();
    private static final Function<Person, String> GET_NAME = p -> p.getName().toString();
    private static final Function<Person, String> GET_OCCUPATION = p -> p.getOccupation().toString();
    private static final Function<Person, String> GET_PHONE = p -> p.getPhone().toString();

    private static final Function<Person, String> GET_DEPENDENTS = p -> p.getDependents().toString();
    private static final Function<Person, Boolean> IS_DEPENDENTS_UNSPECIFIED =
            p -> p.getDependents().isUnspecified();

    private static final Function<Person, String> GET_SALARY = p -> p.getSalary().toString();
    private static final Function<Person, Boolean> IS_SALARY_UNSPECIFIED = p -> p.getSalary().isUnspecified();

    /**
     * Prefixes that can only appear once in filter. Only PREFIX_TAG is not in this array.
     */
    private static final Prefix[] SINGLE_PREFIXES = {
        PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_SALARY, PREFIX_DATE_OF_BIRTH,
        PREFIX_MARITAL_STATUS, PREFIX_DEPENDENTS, PREFIX_OCCUPATION, PREFIX_INSURANCE_PACKAGE,
    };

    /**
     * All prefixes that can be filtered.
     */
    private static final Prefix[] ALL_PREFIXES = {
        PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_SALARY, PREFIX_DATE_OF_BIRTH,
        PREFIX_MARITAL_STATUS, PREFIX_DEPENDENTS, PREFIX_OCCUPATION, PREFIX_INSURANCE_PACKAGE, PREFIX_TAG
    };

    private static final String PARSE_EXCEPTION_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    private static final Map<Prefix, Function<Person, String>> CONTAINS_PREFIX_MAP = createContainsMap();

    private static final Map<Prefix, Function<Person, String>> createContainsMap() {
        Map<Prefix, Function<Person, String>> map = new LinkedHashMap<>();
        map.put(PREFIX_NAME, GET_NAME);
        map.put(PREFIX_ADDRESS, GET_ADDRESS);
        map.put(PREFIX_PHONE, GET_PHONE);
        map.put(PREFIX_EMAIL, GET_EMAIL);
        map.put(PREFIX_DATE_OF_BIRTH, GET_DOB);
        map.put(PREFIX_OCCUPATION, GET_OCCUPATION);
        map.put(PREFIX_MARITAL_STATUS, GET_MARITAL_STATUS);
        map.put(PREFIX_INSURANCE_PACKAGE, GET_INSURANCE_PACKAGE);

        return map;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultiMap = ArgumentTokenizer.tokenize(args, ALL_PREFIXES);
        validateArgs(argMultiMap);

        List<FilterPrefixParser> filterPrefixParsers = new ArrayList<>();
        addAllContainsPrefixParsersIfPresent(argMultiMap, filterPrefixParsers);
        addAllComparisonPrefixParsersIfPresent(argMultiMap, filterPrefixParsers);
        addTagParserIfPresent(argMultiMap, filterPrefixParsers);

        String argsPrettyString = getArgsPrettyString(filterPrefixParsers);
        return new FilterCommand(new PersonContainsKeywordsPredicate(filterPrefixParsers), argsPrettyString);
    }

    /**
     * Validates the arguments provided by the user, ensuring the following:<br>
     * 1. Prefixes are present.<br>
     * 2. There is no preamble after filter.<br>
     * 3. There are no duplicate prefixes.<br>
     * 4. There are no prefixes with empty keywords.
     */
    private void validateArgs(ArgumentMultimap argMultiMap) throws ParseException {
        if (!areAnyPrefixesPresent(argMultiMap, ALL_PREFIXES) || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(PARSE_EXCEPTION_MESSAGE);
        }
        argMultiMap.verifyNoDuplicatePrefixesFor(SINGLE_PREFIXES);

        List<Prefix> emptyPrefixes = getEmptyPrefixes(argMultiMap, ALL_PREFIXES);
        if (!emptyPrefixes.isEmpty()) {
            String affectedPrefixes =
                    emptyPrefixes.stream().map(Prefix::getPrefix).collect(Collectors.joining(", "));
            String prefixWord = emptyPrefixes.size() == 1 ? "prefix" : "prefixes";
            throw new ParseException(
                    String.format(MESSAGE_MISSING_KEYWORDS, prefixWord, affectedPrefixes)
            );
        }
    }

    /**
     * Returns true if at least one of the prefixes is present in the given
     * {@code ArgumentMultimap}.
     */
    private boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns a list of prefixes that have empty values (i.e. user did not provide a keyword for that prefix).
     */
    private List<Prefix> getEmptyPrefixes(ArgumentMultimap argMultiMap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .filter(prefix -> argMultiMap.getValue(prefix).isPresent())
                .filter(prefix -> {
                    if (prefix.equals(PREFIX_TAG)) {
                        return argMultiMap.getAllValues(prefix).stream().anyMatch(String::isEmpty);
                    }
                    return argMultiMap.getValue(prefix).get().isEmpty();
                })
                .toList();
    }

    /**
     * Joins the argument strings from all filter parsers into a single, space-separated string.
     */
    private String getArgsPrettyString(List<FilterPrefixParser> filterPrefixParsers) {
        return filterPrefixParsers.stream().map(FilterPrefixParser::getArg).collect(Collectors.joining(" "));
    }

    /**
     * Adds a contains prefix parser to the list if the prefix is present in the argMultiMap.<br>
     * These prefixes contain keywords that will be parsed using case-insensitive {@code contains} matching.
     */
    private void addContainsPrefixParserIfPresent(
            ArgumentMultimap argMultimap,
            Prefix prefix,
            Function<Person, String> getPersonField,
            List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        if (!argMultimap.getValue(prefix).isPresent()) {
            return;
        }
        FilterContainsPrefixParser parser = new FilterContainsPrefixParser(prefix, getPersonField);
        parser.parse(argMultimap.getValue(prefix).get());
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds a comparison prefix parser to the list if the prefix is present in the argMultiMap.
     * Applies to prefixes Salary and Number of dependents.<br>
     * These prefixes contain keywords that can be parsed using comparison operators, or using {@code contains}
     * logic is no operators are provided
     */
    private void addComparisonPrefixParserIfPresent(ArgumentMultimap argMultimap,
                                                  Prefix prefix,
                                                  Function<Person, String> getPersonField,
                                                  Function<Person, Boolean> isPersonFieldUnspecified,
                                                  List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        if (!argMultimap.getValue(prefix).isPresent()) {
            return;
        }
        FilterComparisonPrefixParser parser =
                new FilterComparisonPrefixParser(prefix, getPersonField, isPersonFieldUnspecified);
        parser.parse(argMultimap.getValue(prefix).get());
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds a Tag parser to the list if it is present in the argMultiMap.
     * Applies to prefixes Tag only.
     */
    private void addTagParserIfPresent(ArgumentMultimap argMultiMap,
                                            List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        List<String> keywords = argMultiMap.getAllValues(PREFIX_TAG);
        if (keywords.isEmpty()) {
            return;
        }

        FilterTagParser parser = new FilterTagParser(PREFIX_TAG);
        for (String keyword : argMultiMap.getAllValues(PREFIX_TAG)) {
            parser.parse(keyword);
        }
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds all contains prefix parsers whose prefix are present to the list.
     */
    private void addAllContainsPrefixParsersIfPresent(
            ArgumentMultimap argMultiMap, List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        for (Map.Entry<Prefix, Function<Person, String>> entry : CONTAINS_PREFIX_MAP.entrySet()) {
            addContainsPrefixParserIfPresent(argMultiMap, entry.getKey(), entry.getValue(), filterPrefixParsers);
        }
    }

    /**
     * Adds all comparison prefix parsers whose prefix are present to the list.
     */
    private void addAllComparisonPrefixParsersIfPresent(
            ArgumentMultimap argMultiMap, List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        addComparisonPrefixParserIfPresent(argMultiMap,
                PREFIX_SALARY, GET_SALARY, IS_SALARY_UNSPECIFIED, filterPrefixParsers);
        addComparisonPrefixParserIfPresent(
                argMultiMap, PREFIX_DEPENDENTS, GET_DEPENDENTS, IS_DEPENDENTS_UNSPECIFIED, filterPrefixParsers);
    }
}
