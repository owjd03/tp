package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortDirection;
import seedu.address.logic.commands.SortCommand.SortField;

public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsSortCommand() {
        // Test all valid fields
        assertParseSuccess(parser, "name", new SortCommand(SortField.NAME, SortDirection.ASCENDING));
        assertParseSuccess(parser, "phone", new SortCommand(SortField.PHONE, SortDirection.ASCENDING));
        assertParseSuccess(parser, "email", new SortCommand(SortField.EMAIL, SortDirection.ASCENDING));
        assertParseSuccess(parser, "address", new SortCommand(SortField.ADDRESS, SortDirection.ASCENDING));
        assertParseSuccess(parser, "salary",
                new SortCommand(SortField.SALARY, SortDirection.ASCENDING));
        assertParseSuccess(parser, "dateofbirth",
                new SortCommand(SortField.DATEOFBIRTH, SortDirection.ASCENDING));
        assertParseSuccess(parser, "maritalstatus",
                new SortCommand(SortField.MARITALSTATUS, SortDirection.ASCENDING));
        assertParseSuccess(parser, "occupation",
                new SortCommand(SortField.OCCUPATION, SortDirection.ASCENDING));
        assertParseSuccess(parser, "dependent", new SortCommand(SortField.DEPENDENT, SortDirection.ASCENDING));

        // Test ascending
        assertParseSuccess(parser, "name ascending", new SortCommand(SortField.NAME, SortDirection.ASCENDING));
        assertParseSuccess(parser, "phone ascending",
                new SortCommand(SortField.PHONE, SortDirection.ASCENDING));

        // Test descending
        assertParseSuccess(parser, "name descending",
                new SortCommand(SortField.NAME, SortDirection.DESCENDING));
        assertParseSuccess(parser, "phone descending",
                new SortCommand(SortField.PHONE, SortDirection.DESCENDING));

        // Test case insensitive
        assertParseSuccess(parser, "NaMe", new SortCommand(SortField.NAME, SortDirection.ASCENDING));
        assertParseSuccess(parser, "depeNDent ascending",
                new SortCommand(SortField.DEPENDENT, SortDirection.ASCENDING));
        assertParseSuccess(parser, "oCCupatIon descending",
                new SortCommand(SortField.OCCUPATION, SortDirection.DESCENDING));

        // Test ignore extra arguments
        assertParseSuccess(parser, "name 222", new SortCommand(SortField.NAME, SortDirection.ASCENDING));
        assertParseSuccess(parser, "address ascending 234",
                new SortCommand(SortField.ADDRESS, SortDirection.ASCENDING));
        assertParseSuccess(parser, "address descending 234aaa",
                new SortCommand(SortField.ADDRESS, SortDirection.DESCENDING));

        // Test ignore extra space
        assertParseSuccess(parser, "   name  ", new SortCommand(SortField.NAME, SortDirection.ASCENDING));
        assertParseSuccess(parser, "   dependent  ascending",
                new SortCommand(SortField.DEPENDENT, SortDirection.ASCENDING));

        // Test invalid direction text
        assertParseSuccess(parser, "dependent invalidDirection",
                new SortCommand(SortField.DEPENDENT, SortDirection.ASCENDING));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        //Test empty strings
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        //Test invalid fields
        assertParseFailure(parser, "111",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "named",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "addres",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "phon",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
