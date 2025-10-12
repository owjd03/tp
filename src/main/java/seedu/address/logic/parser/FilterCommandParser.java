package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Prefix[] PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_MARITAL_STATUS,
        PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY, PREFIX_TAG
    };

    private final String parseExceptionMessage =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultiMap = ArgumentTokenizer.tokenize(args, PREFIXES);

        if (!areAnyPrefixesPresent(argMultiMap, PREFIXES) || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(parseExceptionMessage);
        }

        Map<Prefix, String> keywords = new HashMap<>();
        for (Prefix prefix : PREFIXES) {
            // If there is any duplicate categories, the value of the last category will be grabbed
            // i.e. filter a/Clementi a/Changi, this will grab Changi
            Optional<String> optValue = argMultiMap.getValue(prefix);
            if (optValue.isEmpty()) {
                continue;
            }
            String keyword = optValue.get();
            // Do not allow empty keywords, i.e. filter n/ a/kent ridge
            if (keyword.isEmpty()) {
                throw new ParseException(parseExceptionMessage);
            }
            keywords.put(prefix, keyword);
        }

        if (keywords.isEmpty()) {
            throw new ParseException(parseExceptionMessage);
        }

        return new FilterCommand(new PersonContainsKeywordsPredicate(keywords));
    }

    /**
     * Returns true if at least one of the prefixes is present in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
