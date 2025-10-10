package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsFindCommand() {
        assertParseSuccess(parser, "name", new SortCommand());

        assertParseSuccess(parser, "name 222", new SortCommand());
    }
}
