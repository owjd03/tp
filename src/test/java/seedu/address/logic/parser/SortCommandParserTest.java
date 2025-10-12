package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortField;

public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsSortCommand() {
        // Test all valid fields
        assertParseSuccess(parser, "name", new SortCommand(SortField.NAME));
        assertParseSuccess(parser, "phone", new SortCommand(SortField.PHONE));
        assertParseSuccess(parser, "email", new SortCommand(SortField.EMAIL));
        assertParseSuccess(parser, "address", new SortCommand(SortField.ADDRESS));
        assertParseSuccess(parser, "salary", new SortCommand(SortField.SALARY));
        assertParseSuccess(parser, "dateofbirth", new SortCommand(SortField.DATEOFBIRTH));
        assertParseSuccess(parser, "maritalstatus", new SortCommand(SortField.MARITALSTATUS));
        assertParseSuccess(parser, "occupation", new SortCommand(SortField.OCCUPATION));
        assertParseSuccess(parser, "dependent", new SortCommand(SortField.DEPENDENT));

        // Test case insensitive
        assertParseSuccess(parser, "NaMe", new SortCommand(SortField.NAME));
        assertParseSuccess(parser, "depeNDent", new SortCommand(SortField.DEPENDENT));

        // Test ignore extra arguments
        assertParseSuccess(parser, "name 222", new SortCommand(SortField.NAME));
        assertParseSuccess(parser, "address 234", new SortCommand(SortField.ADDRESS));

        // Test ignore extra space
        assertParseSuccess(parser, "   name  ", new SortCommand(SortField.NAME));
        assertParseSuccess(parser, "   dependent  ", new SortCommand(SortField.DEPENDENT));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        //Test empty strings
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        //Test invalid fields
        assertParseFailure(parser, "111", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "named", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "addres", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "phon", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
