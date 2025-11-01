package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPackageCommand;
import seedu.address.logic.commands.DeletePackageCommand;
import seedu.address.logic.commands.EditPackageCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.testutil.InsurancePackageBuilder;

public class PackageCommandParserTest {

    private static final String DESC_DESC_NEW = " " + PREFIX_DESCRIPTION + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB;
    private static final String MESSAGE_INVALID_FORMAT_EDIT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            EditPackageCommand.MESSAGE_USAGE);
    private static final String NAME_DESC_GOLD = " " + PREFIX_INSURANCE_PACKAGE + VALID_INSURANCE_PACKAGE_NAME_BOB;

    // Add package tests

    @Test
    public void parseAdd_allFieldsPresent_success() {
        AddPackageCommand expectedCommand = new AddPackageCommand(new InsurancePackageBuilder()
                .withName(VALID_INSURANCE_PACKAGE_NAME_BOB)
                .withDescription(VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB).build());

        // whitespace only preamble
        assertDoesNotThrow(() -> {
            AddPackageCommand result = PackageCommandParser.parseAddPackage(
                    PREAMBLE_WHITESPACE + INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER);
            assertEquals(expectedCommand, result);
        });

        // normal input
        assertDoesNotThrow(() -> {
            AddPackageCommand result = PackageCommandParser.parseAddPackage(
                    INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER);
            assertEquals(expectedCommand, result);
        });
    }

    @Test
    public void parseAdd_repeatedNonTagValue_failure() {
        String validExpectedInsurancePackageString = INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER;

        // multiple insurance names
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(INSURANCE_NAME_DESC_GOLD + validExpectedInsurancePackageString)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE), e1.getMessage());

        // multiple descriptions
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(DESCRIPTION_DESC_GOLD + validExpectedInsurancePackageString)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION), e2.getMessage());

        // multiple fields repeated
        ParseException e3 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(
                        validExpectedInsurancePackageString + INSURANCE_NAME_DESC_GOLD + DESCRIPTION_DESC_GOLD
                                + validExpectedInsurancePackageString)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION),
                e3.getMessage());

        // invalid insurance name
        ParseException e4 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(INVALID_INSURANCE_NAME_DESC + validExpectedInsurancePackageString)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE), e4.getMessage());

        // invalid description
        ParseException e5 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(INVALID_DESCRIPTION_DESC + validExpectedInsurancePackageString)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION), e5.getMessage());
    }

    @Test
    public void parseAdd_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPackageCommand.MESSAGE_USAGE);

        // missing insurance name prefix
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(VALID_INSURANCE_PACKAGE_NAME_BOB + DESCRIPTION_DESC_SILVER)
        );
        assertEquals(expectedMessage, e1.getMessage());

        // missing description prefix
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(
                        INSURANCE_NAME_DESC_SILVER + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB)
        );
        assertEquals(expectedMessage, e2.getMessage());

        // all prefixes missing
        ParseException e3 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(
                        VALID_INSURANCE_PACKAGE_NAME_BOB + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB)
        );
        assertEquals(expectedMessage, e3.getMessage());
    }

    @Test
    public void parseAdd_invalidValue_failure() {
        // Invalid name
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(INVALID_INSURANCE_NAME_DESC + DESCRIPTION_DESC_SILVER)
        );
        assertEquals(InsurancePackage.MESSAGE_CONSTRAINTS, e1.getMessage());

        // Non-empty preamble
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseAddPackage(
                        PREAMBLE_NON_EMPTY + INSURANCE_NAME_DESC_SILVER + DESCRIPTION_DESC_SILVER)
        );
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPackageCommand.MESSAGE_USAGE), e2.getMessage());
    }

    @Test
    public void parseAdd_emptyDescription_success() {
        AddPackageCommand expectedCommand = new AddPackageCommand(new InsurancePackageBuilder()
                .withName(VALID_INSURANCE_PACKAGE_NAME_AMY)
                .withDescription("").build());

        assertDoesNotThrow(() -> {
            AddPackageCommand result = PackageCommandParser.parseAddPackage(
                    INSURANCE_NAME_DESC_GOLD + " " + PREFIX_DESCRIPTION);
            assertEquals(expectedCommand, result);
        });
    }

    // Edit package tests

    @Test
    public void parseEdit_allFieldsPresent_success() {
        EditPackageCommand expectedCommand = new EditPackageCommand(VALID_INSURANCE_PACKAGE_NAME_BOB,
                VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB);

        // whitespace only preamble
        assertDoesNotThrow(() -> {
            EditPackageCommand result = PackageCommandParser.parseEditPackage(
                    "   " + NAME_DESC_GOLD + DESC_DESC_NEW);
            assertEquals(expectedCommand, result);
        });

        // normal input
        assertDoesNotThrow(() -> {
            EditPackageCommand result = PackageCommandParser.parseEditPackage(
                    NAME_DESC_GOLD + DESC_DESC_NEW);
            assertEquals(expectedCommand, result);
        });

        // fields in different order
        assertDoesNotThrow(() -> {
            EditPackageCommand result = PackageCommandParser.parseEditPackage(
                    DESC_DESC_NEW + NAME_DESC_GOLD);
            assertEquals(expectedCommand, result);
        });
    }

    @Test
    public void parseEdit_preambleNonEmpty_failure() {
        // non-empty preamble
        ParseException e = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage("some random text" + NAME_DESC_GOLD + DESC_DESC_NEW)
        );
        assertEquals(MESSAGE_INVALID_FORMAT_EDIT, e.getMessage());
    }

    @Test
    public void parseEdit_compulsoryFieldMissing_failure() {
        // missing insurance name prefix
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(VALID_INSURANCE_PACKAGE_NAME_BOB + DESC_DESC_NEW)
        );
        assertEquals(MESSAGE_INVALID_FORMAT_EDIT, e1.getMessage());

        // missing description prefix
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(NAME_DESC_GOLD + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB)
        );
        assertEquals(MESSAGE_INVALID_FORMAT_EDIT, e2.getMessage());

        // all prefixes missing
        ParseException e3 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(
                        VALID_INSURANCE_PACKAGE_NAME_BOB + VALID_INSURANCE_PACKAGE_DESCRIPTION_BOB)
        );
        assertEquals(MESSAGE_INVALID_FORMAT_EDIT, e3.getMessage());
    }

    @Test
    public void parseEdit_duplicatePrefixes_failure() {
        // multiple insurance names
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(NAME_DESC_GOLD + NAME_DESC_GOLD + DESC_DESC_NEW)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE), e1.getMessage());

        // multiple descriptions
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(NAME_DESC_GOLD + DESC_DESC_NEW + DESC_DESC_NEW)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION), e2.getMessage());

        // multiple fields repeated
        ParseException e3 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(NAME_DESC_GOLD + DESC_DESC_NEW + NAME_DESC_GOLD + DESC_DESC_NEW)
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE, PREFIX_DESCRIPTION),
                e3.getMessage());
    }

    @Test
    public void parseEdit_emptyValues_failure() {
        // empty name
        // This parser should fail, as per the check added in PackageCommandParser.java
        ParseException e = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseEditPackage(" " + PREFIX_INSURANCE_PACKAGE + " " + DESC_DESC_NEW)
        );
        assertEquals(InsurancePackage.MESSAGE_CONSTRAINTS, e.getMessage());
    }

    @Test
    public void parseEdit_emptyDescription_success() {
        // empty description
        EditPackageCommand expectedCommand = new EditPackageCommand(VALID_INSURANCE_PACKAGE_NAME_BOB, "");
        assertDoesNotThrow(() -> {
            EditPackageCommand result = PackageCommandParser.parseEditPackage(
                    NAME_DESC_GOLD + " " + PREFIX_DESCRIPTION);
            assertEquals(expectedCommand, result);
        });
    }

    // Delete package tests

    @Test
    public void parseDelete_validArgs_returnsDeletePackageCommand() {
        // no leading and trailing whitespaces
        DeletePackageCommand expectedCommand1 = new DeletePackageCommand("Gold");
        assertDoesNotThrow(() -> {
            DeletePackageCommand result = PackageCommandParser.parseDeletePackage(
                    " " + PREFIX_INSURANCE_PACKAGE + "Gold");
            assertEquals(expectedCommand1, result);
        });

        // leading and trailing whitespaces for package name
        DeletePackageCommand expectedCommand2 = new DeletePackageCommand("Silver");
        assertDoesNotThrow(() -> {
            DeletePackageCommand result = PackageCommandParser.parseDeletePackage(
                    " " + PREFIX_INSURANCE_PACKAGE + "  Silver  ");
            assertEquals(expectedCommand2, result);
        });
    }

    @Test
    public void parseDelete_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePackageCommand.MESSAGE_USAGE);

        // no prefix
        ParseException e1 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseDeletePackage("Gold")
        );
        assertEquals(expectedMessage, e1.getMessage());

        // empty command
        ParseException e2 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseDeletePackage("")
        );
        assertEquals(expectedMessage, e2.getMessage());

        // preamble
        ParseException e3 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseDeletePackage("preamble " + PREFIX_INSURANCE_PACKAGE + "Gold")
        );
        assertEquals(expectedMessage, e3.getMessage());

        // empty package name after prefix
        ParseException e4 = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseDeletePackage(" " + PREFIX_INSURANCE_PACKAGE + " ")
        );
        assertEquals(InsurancePackage.MESSAGE_CONSTRAINTS, e4.getMessage());
    }

    @Test
    public void parseDelete_duplicatePrefixes_throwsParseException() {
        ParseException e = assertThrows(ParseException.class, () ->
                PackageCommandParser.parseDeletePackage(
                        " " + PREFIX_INSURANCE_PACKAGE + "Gold " + PREFIX_INSURANCE_PACKAGE + "Silver")
        );
        assertEquals(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INSURANCE_PACKAGE), e.getMessage());
    }
}
