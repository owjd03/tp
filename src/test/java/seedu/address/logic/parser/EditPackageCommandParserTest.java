package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPackageCommand;

public class EditPackageCommandParserTest {

    private static final Logger logger = LogsCenter.getLogger(EditPackageCommandParserTest.class);

    private static final String DESC_DESC_NEW = " " + PREFIX_DESCRIPTION + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPackageCommand.MESSAGE_USAGE);
    private static final String NAME_DESC_GOLD = " " + PREFIX_INSURANCE_NAME + VALID_INSURANCE_PACKAGE_NAME_BOB;

    private EditPackageCommandParser parser = new EditPackageCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String expectedName = VALID_INSURANCE_PACKAGE_NAME_BOB;
        String expectedDesc = VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
        EditPackageCommand expectedCommand = new EditPackageCommand(expectedName, expectedDesc);
        logger.info("Expected command: " + expectedCommand);

        // whitespace only preamble
        assertParseSuccess(parser, "   " + NAME_DESC_GOLD + DESC_DESC_NEW,
                new EditPackageCommand(expectedName, expectedDesc));

        // normal input
        assertParseSuccess(parser, NAME_DESC_GOLD + DESC_DESC_NEW,
                new EditPackageCommand(expectedName, expectedDesc));

        // fields in different order
        assertParseSuccess(parser, DESC_DESC_NEW + NAME_DESC_GOLD,
                new EditPackageCommand(expectedName, expectedDesc));
    }

    @Test
    public void parse_preambleNonEmpty_failure() {
        // non-empty preamble
        assertParseFailure(parser, "some random text" + NAME_DESC_GOLD + DESC_DESC_NEW,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing insurance name prefix
        assertParseFailure(parser, VALID_INSURANCE_PACKAGE_NAME_BOB + DESC_DESC_NEW, MESSAGE_INVALID_FORMAT);

        // missing description prefix
        assertParseFailure(parser, NAME_DESC_GOLD + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB, MESSAGE_INVALID_FORMAT);

        // all prefixes missing
        assertParseFailure(parser, VALID_INSURANCE_PACKAGE_NAME_BOB + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // multiple insurance names
        assertParseFailure(parser, NAME_DESC_GOLD + NAME_DESC_GOLD + DESC_DESC_NEW,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_NAME));

        // multiple descriptions
        assertParseFailure(parser, NAME_DESC_GOLD + DESC_DESC_NEW + DESC_DESC_NEW,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // multiple fields repeated
        assertParseFailure(parser, NAME_DESC_GOLD + DESC_DESC_NEW + NAME_DESC_GOLD + DESC_DESC_NEW,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_NAME, PREFIX_DESCRIPTION));
    }

    @Test
    public void parse_emptyValues_success() {
        // empty description
        assertParseSuccess(parser, NAME_DESC_GOLD + " " + PREFIX_DESCRIPTION,
                new EditPackageCommand(VALID_INSURANCE_PACKAGE_NAME_BOB, ""));

        // empty name
        // This parser should succeed, but the command will fail on execution
        assertParseSuccess(parser, " " + PREFIX_INSURANCE_NAME + " " + DESC_DESC_NEW,
                new EditPackageCommand("", VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB));
    }
}
