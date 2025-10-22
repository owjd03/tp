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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Prefix[] SINGLE_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE,
        PREFIX_MARITAL_STATUS, PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY
    };

    private static final Prefix[] ALL_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE,
        PREFIX_MARITAL_STATUS, PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY, PREFIX_TAG
    };

    private final String parseExceptionMessage =
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
            throw new ParseException(parseExceptionMessage);
        }

        argMultiMap.verifyNoDuplicatePrefixesFor(SINGLE_PREFIXES);

        Map<Prefix, String> keywords = new HashMap<>();
        for (Prefix prefix : SINGLE_PREFIXES) {
            Optional<String> optValue = argMultiMap.getValue(prefix);
            if (optValue.isEmpty()) {
                continue;
            }
            String keyword = optValue.get();
            // Do not allow empty keywords, i.e. filter n/ a/kent ridge
            if (keyword == null || keyword.isEmpty()) {
                throw new ParseException(parseExceptionMessage);
            }
            keywords.put(prefix, keyword);
        }

        // Set<Tag> will be empty if user did not filter for tags
        Set<Tag> tags = parseTagsForEdit(argMultiMap.getAllValues(PREFIX_TAG)).orElse(Collections.emptySet());

        if (keywords.isEmpty() && tags.isEmpty()) {
            throw new ParseException(parseExceptionMessage);
        }

        return new FilterCommand(new PersonContainsKeywordsPredicate(keywords, tags));
    }

    /**
     * Returns true if at least one of the prefixes is present in the given
     * {@code ArgumentMultimap}.
     */
    private boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        // Do not allow empty tags, i.e. filter t/
        if (tags.contains("")) {
            throw new ParseException(parseExceptionMessage);
        }
        return Optional.of(ParserUtil.parseTags(tags));
    }
}
