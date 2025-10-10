package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class ViewCommandParserTest {
    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_validIndexSpecified_returnsViewCommand() {
        // have index
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + "";
        ViewCommand expectedCommand = new ViewCommand(INDEX_FIRST_PERSON);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_invalidIndexSpecified_success() {
        // have index
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize("-1");
            Index targetIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
            assertEquals(2, 3);
        } catch (ParseException e) {
            assertEquals(2, 2);
        }
    }

    @Test
    public void parse_validNameSpecified_returnsViewCommand() {
        // have index
        ViewCommand expectedViewCommand =
                new ViewCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedViewCommand);
    }

    @Test
    public void parse_missingCompulsoryField_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, " ", expectedMessage);
    }
}
