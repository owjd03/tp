package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeletePackageCommand;
import seedu.address.model.insurance.InsurancePackage;

public class DeletePackageCommandParserTest {

    private final DeletePackageCommandParser parser = new DeletePackageCommandParser();

    @Test
    public void parse_validArgs_returnsDeletePackageCommand() {
        // no leading and trailing whitespaces
        assertParseSuccess(parser, " " + PREFIX_INSURANCE_NAME + "Gold",
                new DeletePackageCommand("Gold"));

        // leading and trailing whitespaces for package name
        assertParseSuccess(parser, " " + PREFIX_INSURANCE_NAME + "  Silver  ",
                new DeletePackageCommand("Silver"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // no prefix
        assertParseFailure(parser, "Gold",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePackageCommand.MESSAGE_USAGE));

        // empty command
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePackageCommand.MESSAGE_USAGE));

        // preamble
        assertParseFailure(parser, "preamble " + PREFIX_INSURANCE_NAME + "Gold",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePackageCommand.MESSAGE_USAGE));

        // empty package name after prefix
        assertParseFailure(parser, " " + PREFIX_INSURANCE_NAME + " ",
                InsurancePackage.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_INSURANCE_NAME + "Gold " + PREFIX_INSURANCE_NAME + "Silver",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_NAME));
    }
}
