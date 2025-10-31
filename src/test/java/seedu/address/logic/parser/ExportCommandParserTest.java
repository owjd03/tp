package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

/**
 * Contains unit tests for {@code ExportCommandParser}.
 */
public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();
    private final String expectedParseFailureMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            ExportCommand.MESSAGE_USAGE);

    @Test
    public void parse_emptyOrWhitespaceArgs_returnsDefaultExportCommand() {
        // No arguments
        assertParseSuccess(parser, "", new ExportCommand());
        // Whitespace only
        assertParseSuccess(parser, "   ", new ExportCommand());
    }

    @Test
    public void parse_validArgs_returnsExportCommandWithFilePath() {
        // Windows path
        Path path1 = Paths.get("C:\\Users\\User\\Documents\\data.csv");
        assertParseSuccess(parser, "C:\\Users\\User\\Documents\\data.csv", new ExportCommand(path1));

        // macOS/Linux path
        Path path2 = Paths.get("/Users/User/Documents/data.csv");
        assertParseSuccess(parser, "/Users/User/Documents/data.csv", new ExportCommand(path2));

        // Home directory expansion
        String home = System.getProperty("user.home");
        Path path3 = Paths.get(home, "data.csv");
        assertParseSuccess(parser, "~/data.csv", new ExportCommand(path3));
    }

    @Test
    public void parse_invalidPathSyntax_throwsParseException() {
        // Invalid null character in path
        assertParseFailure(parser, "test\0.csv", expectedParseFailureMessage);
    }

    @Test
    public void parse_pathNotEndingInCsv_throwsParseException() {
        // Does not end with .csv
        assertParseFailure(parser, "test.txt", expectedParseFailureMessage);
        assertParseFailure(parser, "test", expectedParseFailureMessage);
    }
}
