package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

/**
 * Contains unit tests for {@code ExportCommandParser}.
 */
public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validArgs_returnsExportCommand() {
        // No arguments
        assertParseSuccess(parser, "", new ExportCommand());
        // Whitespace only
        assertParseSuccess(parser, "   ", new ExportCommand());
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);
        // Any non-whitespace argument
        assertParseFailure(parser, "some random string", expectedMessage);
        assertParseFailure(parser, "f/my_file.csv", expectedMessage);
    }
}
