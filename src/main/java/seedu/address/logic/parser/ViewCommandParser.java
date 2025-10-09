package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

import java.util.Arrays;

/**
 * Parses the given {@code String} of arguments in the context of the ViewCommand
 * and returns a ViewCommand object for execution.
 * @throws ParseException if the user input does not conform the expected format
 */
public class ViewCommandParser implements Parser<ViewCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code ViewCommand}
     * and returns a {@code ViewCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        try {
            Integer integer = Integer.parseInt(trimmedArgs);

            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            return new ViewCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE), ive);
        } catch (NumberFormatException e) {
            String[] nameKeywords = trimmedArgs.split("\\s+");

            return new ViewCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }
    }
}
