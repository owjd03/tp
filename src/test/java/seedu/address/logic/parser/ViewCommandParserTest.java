package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewCommand;

public class ViewCommandParserTest {
    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_validIndexSpecified_returnsViewCommand() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = "i/" + targetIndex.getOneBased() + "";
        ViewCommand expectedCommand = new ViewCommand(INDEX_FIRST_PERSON);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_invalidIndexSpecified_throwsParseException() {
        ViewCommandParser viewCom = new ViewCommandParser();
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE);
        assertParseFailure(viewCom, "i/-1", expectedMessage);
    }

    @Test
    public void parse_validKeywordSpecified_returnsViewCommand() {
        String userInput = "alice";
        ViewCommand expectedCommand = new ViewCommand("alice");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, " ", expectedMessage);
    }
}
