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
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.filter.FilterDescriptivePrefixParser;
import seedu.address.logic.parser.filter.FilterMultiplePrefixParser;
import seedu.address.logic.parser.filter.FilterNumericalPrefixParser;
import seedu.address.logic.parser.filter.FilterPrefixParser;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Prefixes that can only appear once. Only PREFIX_TAG is not in this array.
     */
    private static final Prefix[] SINGLE_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE,
        PREFIX_MARITAL_STATUS, PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY
    };

    /**
     * Prefixes that allow comparison operators (>=, >, <=, <)
     */
    private static final Prefix[] NUMERICAL_PREFIXES = {
        PREFIX_DEPENDENTS, PREFIX_SALARY
    };

    /**
     * Prefixes that do not allow comparison operators
     */
    private static final Prefix[] DESCRIPTIVE_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE, PREFIX_MARITAL_STATUS,
        PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE
    };

    /**
     * All prefixes that can be filtered.
     */
    private static final Prefix[] ALL_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE,
        PREFIX_MARITAL_STATUS, PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY, PREFIX_TAG
    };

    private static final String PARSE_EXCEPTION_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultiMap = ArgumentTokenizer.tokenize(args, ALL_PREFIXES);

        if (!areAnyPrefixesPresent(argMultiMap, ALL_PREFIXES) || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(PARSE_EXCEPTION_MESSAGE);
        }
        argMultiMap.verifyNoDuplicatePrefixesFor(SINGLE_PREFIXES);

        List<FilterPrefixParser> filterPrefixParsers = new ArrayList<>();
        addAllDescriptiveParsersIfPresent(argMultiMap, filterPrefixParsers);
        addAllNumericalParsersIfPresent(argMultiMap, filterPrefixParsers);
        addMultipleParserIfPresent(argMultiMap, PREFIX_TAG, filterPrefixParsers);

        return new FilterCommand(new PersonContainsKeywordsPredicate(filterPrefixParsers));
    }

    /**
     * Returns true if at least one of the prefixes is present in the given
     * {@code ArgumentMultimap}.
     */
    private boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Adds a descriptive prefix parser to the list if the prefix is present in the argMultiMap.
     */
    private void addDescriptiveParserIfPresent(ArgumentMultimap argMultimap,
                                               Prefix prefix,
                                               Function<Person, String> getPersonField,
                                               List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        if (!argMultimap.getValue(prefix).isPresent()) {
            return;
        }
        FilterDescriptivePrefixParser parser = new FilterDescriptivePrefixParser(prefix, getPersonField);
        parser.parse(argMultimap.getValue(prefix).get());
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds a numerical prefix parser to the list if the prefix is present in the argMultiMap.
     * Applies to prefixes Salary and Number of dependents.
     */
    private void addNumericalParserIfPresent(ArgumentMultimap argMultimap,
                                             Prefix prefix,
                                             Function<Person, Double> getPersonField,
                                             List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        if (!argMultimap.getValue(prefix).isPresent()) {
            return;
        }
        FilterNumericalPrefixParser parser = new FilterNumericalPrefixParser(prefix, getPersonField);
        parser.parse(argMultimap.getValue(prefix).get());
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds a multiple prefix parser to the list if the prefix is present in the argMultiMap.
     * Applies to prefix Tags.
     */
    private void addMultipleParserIfPresent(ArgumentMultimap argMultiMap,
                                            Prefix prefix,
                                            List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        List<String> keywords = argMultiMap.getAllValues(prefix);
        if (keywords.isEmpty()) {
            return;
        }

        FilterMultiplePrefixParser parser = new FilterMultiplePrefixParser(prefix);
        for (String keyword : argMultiMap.getAllValues(prefix)) {
            parser.parse(keyword);
        }
        filterPrefixParsers.add(parser);
    }

    /**
     * Adds all descriptive prefix parsers whose prefix are present to the list  in the argMultiMap.
     */
    private void addAllDescriptiveParsersIfPresent(ArgumentMultimap argMultiMap,
                                                   List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_NAME, p -> p.getName().fullName, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_ADDRESS, p -> p.getAddress().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_PHONE, p -> p.getPhone().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_EMAIL, p -> p.getEmail().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_DATE_OF_BIRTH, p ->
                p.getDateOfBirth().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_OCCUPATION, p ->
                p.getOccupation().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_MARITAL_STATUS, p ->
                p.getMaritalStatus().value, filterPrefixParsers);
        addDescriptiveParserIfPresent(argMultiMap, PREFIX_INSURANCE_PACKAGE, p ->
                p.getInsurancePackage().getPackageName(), filterPrefixParsers);
    }

    /**
     * Adds all numerical prefix parsers whose prefix are present to the list  in the argMultiMap.
     */
    private void addAllNumericalParsersIfPresent(ArgumentMultimap argMultiMap,
                                                 List<FilterPrefixParser> filterPrefixParsers) throws ParseException {
        addNumericalParserIfPresent(argMultiMap, PREFIX_SALARY, p ->
                p.getSalary().getNumericValue(), filterPrefixParsers);
        addNumericalParserIfPresent(argMultiMap, PREFIX_DEPENDENTS, p ->
                p.getDependents().getNumericValue(), filterPrefixParsers);
    }
}
