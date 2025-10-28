package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_GOLD;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_SILVER;
import static seedu.address.logic.commands.CommandTestUtil.INSURANCE_NAME_DESC_GOLD;
import static seedu.address.logic.commands.CommandTestUtil.INSURANCE_NAME_DESC_SILVER;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INSURANCE_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPackageCommand;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.testutil.InsurancePackageBuilder;

public class AddPackageCommandParserTest {
    private AddPackageCommandParser parser = new AddPackageCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        InsurancePackage expectedInsurancePackage = new InsurancePackageBuilder()
                .withName(VALID_INSURANCE_PACKAGE_NAME_BOB)
                .withDescription(VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER,
                new AddPackageCommand(expectedInsurancePackage));

        // normal input
        assertParseSuccess(parser, INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER,
                new AddPackageCommand(expectedInsurancePackage));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedInsurancePackageString = INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER;

        // multiple insurance names
        assertParseFailure(parser, INSURANCE_NAME_DESC_GOLD + validExpectedInsurancePackageString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE));

        // multiple descriptions
        assertParseFailure(parser, DESCRIPTION_DESC_GOLD + validExpectedInsurancePackageString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedInsurancePackageString + INSURANCE_NAME_DESC_GOLD + DESCRIPTION_DESC_GOLD
                        + validExpectedInsurancePackageString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION));

        // invalid value followed by valid value

        // invalid insurance name
        assertParseFailure(parser, INVALID_INSURANCE_NAME_DESC + validExpectedInsurancePackageString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE));

        // invalid description
        assertParseFailure(parser, INVALID_DESCRIPTION_DESC + validExpectedInsurancePackageString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // valid value followed by invalid value

        // invalid insurance name
        assertParseFailure(parser, validExpectedInsurancePackageString + INVALID_INSURANCE_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE));

        // invalid description
        assertParseFailure(parser, validExpectedInsurancePackageString + INVALID_DESCRIPTION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPackageCommand.MESSAGE_USAGE);

        // missing insurance name prefix
        assertParseFailure(parser, VALID_INSURANCE_PACKAGE_NAME_BOB + DESCRIPTION_DESC_SILVER, expectedMessage);

        // missing description prefix
        assertParseFailure(parser, INSURANCE_NAME_DESC_SILVER + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_INSURANCE_PACKAGE_NAME_BOB + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, INVALID_INSURANCE_NAME_DESC + DESCRIPTION_DESC_SILVER,
                InsurancePackage.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, PREAMBLE_NON_EMPTY + INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPackageCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyDescription_success() {
        InsurancePackage expectedInsurancePackage = new InsurancePackageBuilder()
                .withName(VALID_INSURANCE_PACKAGE_NAME_AMY)
                .withDescription("").build();

        assertParseSuccess(parser, INSURANCE_NAME_DESC_GOLD + " " + PREFIX_DESCRIPTION,
                new AddPackageCommand(expectedInsurancePackage));
    }

}
