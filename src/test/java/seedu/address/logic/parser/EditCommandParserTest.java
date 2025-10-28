package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEPENDENTS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DOB_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INSURANCE_PACKAGE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEPENDENTS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DOB_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MARITAL_STATUS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_OCCUPATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SALARY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MARITAL_STATUS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.OCCUPATION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.SALARY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEPENDENTS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DOB_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MARITAL_STATUS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OCCUPATION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SALARY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        String expectedError = String.format("%s\n\n%s",
                MESSAGE_INVALID_INDEX, EditCommand.MESSAGE_USAGE);

        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, expectedError);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", expectedError);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        String expectedError = String.format("%s\n\n%s",
                MESSAGE_INVALID_INDEX, EditCommand.MESSAGE_USAGE);

        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, expectedError);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, expectedError);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", expectedError);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", expectedError);
    }

    @Test
    public void parse_invalidCompulsoryFields_failure() {
        // invalid name
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidOptionalFields_failure() {
        // invalid salary
        assertParseFailure(parser, "1" + INVALID_SALARY_DESC, Salary.MESSAGE_CONSTRAINTS);

        // invalid date of birth
        assertParseFailure(parser, "1" + INVALID_DOB_DESC, DateOfBirth.MESSAGE_CONSTRAINTS);

        // invalid marital status
        assertParseFailure(parser, "1" + INVALID_MARITAL_STATUS_DESC, MaritalStatus.MESSAGE_CONSTRAINTS);

        // invalid dependents
        assertParseFailure(parser, "1" + INVALID_DEPENDENTS_DESC, Dependents.MESSAGE_CONSTRAINTS);

        // invalid occupation
        assertParseFailure(parser, "1" + INVALID_OCCUPATION_DESC, Occupation.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_failure() {
        // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidFieldFollowedByValidField_failure() {
        // invalid compulsory followed by valid compulsory
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // invalid optional followed by valid compulsory
        assertParseFailure(parser, "1" + INVALID_SALARY_DESC + EMAIL_DESC_AMY, Salary.MESSAGE_CONSTRAINTS);

        // invalid compulsory followed by valid optional
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC + DOB_DESC_AMY, Address.MESSAGE_CONSTRAINTS);

        // invalid optional followed by valid optional
        assertParseFailure(parser, "1" + INVALID_DEPENDENTS_DESC + OCCUPATION_DESC_AMY, Dependents.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyTagMixedWithValidTags_failure() {
        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInvalidValues_reportsFirstFailure() {
        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);

        // multiple invalid optional values, first one captured
        assertParseFailure(parser, "1" + INVALID_SALARY_DESC + INVALID_OCCUPATION_DESC + VALID_MARITAL_STATUS_AMY,
                Salary.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + SALARY_DESC_AMY + DOB_DESC_AMY
                + MARITAL_STATUS_DESC_AMY + OCCUPATION_DESC_AMY + DEPENDENTS_DESC_AMY + INSURANCE_PACKAGE_DESC_AMY
                + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withSalary(VALID_SALARY_AMY).withDateOfBirth(VALID_DOB_AMY).withMaritalStatus(VALID_MARITAL_STATUS_AMY)
                .withDependents(VALID_DEPENDENTS_AMY).withOccupation(VALID_OCCUPATION_AMY)
                .withInsurancePackage(VALID_INSURANCE_PACKAGE_NAME_AMY, VALID_INSURANCE_PACKAGE_DESCRIPTION_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // compulsory fields
        assertParseOneFieldSuccess(NAME_DESC_AMY,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build());
        assertParseOneFieldSuccess(PHONE_DESC_AMY,
                new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build());
        assertParseOneFieldSuccess(EMAIL_DESC_AMY,
                new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build());
        assertParseOneFieldSuccess(ADDRESS_DESC_AMY,
                new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build());
        assertParseOneFieldSuccess(INSURANCE_PACKAGE_DESC_AMY,
                new EditPersonDescriptorBuilder()
                        .withInsurancePackage(VALID_INSURANCE_PACKAGE_NAME_AMY, VALID_INSURANCE_PACKAGE_DESCRIPTION_AMY)
                        .build());

        // optional fields
        assertParseOneFieldSuccess(SALARY_DESC_AMY,
                new EditPersonDescriptorBuilder().withSalary(VALID_SALARY_AMY).build());
        assertParseOneFieldSuccess(DOB_DESC_AMY,
                new EditPersonDescriptorBuilder().withDateOfBirth(VALID_DOB_AMY).build());
        assertParseOneFieldSuccess(MARITAL_STATUS_DESC_AMY,
                new EditPersonDescriptorBuilder().withMaritalStatus(VALID_MARITAL_STATUS_AMY).build());
        assertParseOneFieldSuccess(DEPENDENTS_DESC_AMY,
                new EditPersonDescriptorBuilder().withDependents(VALID_DEPENDENTS_AMY).build());
        assertParseOneFieldSuccess(OCCUPATION_DESC_AMY,
                new EditPersonDescriptorBuilder().withOccupation(VALID_OCCUPATION_AMY).build());
        assertParseOneFieldSuccess(TAG_DESC_FRIEND,
                new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build());
    }

    /**
     * Helper method for parsing a single-field edit.
     *
     * @param fieldDescriptor The string for the field (e.g., " n/Amy")
     * @param descriptor The expected EditPersonDescriptor object
     */
    private void assertParseOneFieldSuccess(String fieldDescriptor, EditPersonDescriptor descriptor) {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + fieldDescriptor;
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_singleRepeatedField_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // For compulsory fields
        // valid followed by invalid
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // For optional fields
        // valid followed by invalid
        userInput = targetIndex.getOneBased() + SALARY_DESC_AMY + INVALID_SALARY_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SALARY));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + INVALID_SALARY_DESC + SALARY_DESC_AMY;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SALARY));
    }

    @Test
    public void parse_multipleRepeatedValidFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // Multiple valid fields repeated (compulsory and optional).
        // Tags are allowed to repeat.
        String userInput = targetIndex.getOneBased()
                + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + SALARY_DESC_AMY + OCCUPATION_DESC_AMY + TAG_DESC_FRIEND
                + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + SALARY_DESC_AMY + OCCUPATION_DESC_AMY + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_SALARY, PREFIX_OCCUPATION));
    }

    @Test
    public void parse_multipleRepeatedInvalidFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // multiple invalid values (compulsory and optional)
        String userInput = targetIndex.getOneBased()
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_SALARY_DESC + INVALID_DEPENDENTS_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_SALARY_DESC + INVALID_DEPENDENTS_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_SALARY, PREFIX_DEPENDENTS));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
