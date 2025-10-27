package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DEPENDENTS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEPENDENTS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DOB_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DOB_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INSURANCE_PACKAGE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INSURANCE_PACKAGE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEPENDENTS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DOB_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INSURANCE_PACKAGE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MARITAL_STATUS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_OCCUPATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SALARY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MARITAL_STATUS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.MARITAL_STATUS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.OCCUPATION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.OCCUPATION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.SALARY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SALARY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_PACKAGE_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private static final String ALL_VALID_FIELDS_NO_TAGS = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
            + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
            + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB;

    private static final String VALID_TAGS = TAG_DESC_HUSBAND + TAG_DESC_FRIEND;

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND
                        + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + DEPENDENTS_DESC_BOB
                        + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND;

        assertDuplicatePrefixFailure(PREFIX_NAME, NAME_DESC_AMY, INVALID_NAME_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_PHONE, PHONE_DESC_AMY, INVALID_PHONE_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_EMAIL, EMAIL_DESC_AMY, INVALID_EMAIL_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_ADDRESS, ADDRESS_DESC_AMY, INVALID_ADDRESS_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_SALARY, SALARY_DESC_AMY, INVALID_SALARY_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_DATE_OF_BIRTH, DOB_DESC_AMY, INVALID_DOB_DESC, validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_MARITAL_STATUS, MARITAL_STATUS_DESC_AMY, INVALID_MARITAL_STATUS_DESC,
                validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_OCCUPATION, OCCUPATION_DESC_AMY, INVALID_OCCUPATION_DESC,
                validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_DEPENDENTS, DEPENDENTS_DESC_AMY, INVALID_DEPENDENTS_DESC,
                validExpectedPersonString);
        assertDuplicatePrefixFailure(PREFIX_INSURANCE_PACKAGE, INSURANCE_PACKAGE_DESC_AMY,
                INVALID_INSURANCE_PACKAGE_DESC, validExpectedPersonString);

        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + SALARY_DESC_AMY + DOB_DESC_AMY + MARITAL_STATUS_DESC_AMY + OCCUPATION_DESC_AMY
                        + DEPENDENTS_DESC_AMY + INSURANCE_PACKAGE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE,
                        PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS, PREFIX_OCCUPATION,
                        PREFIX_DEPENDENTS, PREFIX_INSURANCE_PACKAGE));
    }

    private void assertDuplicatePrefixFailure(Prefix prefix, String validDuplicateDesc, String invalidDuplicateDesc,
                                              String baseString) {
        String expectedError = Messages.getErrorMessageForDuplicatePrefixes(prefix);

        // valid value followed by valid value
        assertParseFailure(parser, validDuplicateDesc + baseString, expectedError);

        // invalid value followed by valid value
        assertParseFailure(parser, invalidDuplicateDesc + baseString, expectedError);

        // valid value followed by invalid value
        assertParseFailure(parser, baseString + invalidDuplicateDesc, expectedError);
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withSalary(Salary.UNSPECIFIED_VALUE)
                .withDateOfBirth(DateOfBirth.UNSPECIFIED_VALUE)
                .withMaritalStatus(MaritalStatus.UNSPECIFIED_VALUE)
                .withOccupation(Occupation.UNSPECIFIED_VALUE)
                .withDependents(Dependents.UNSPECIFIED_VALUE)
                .withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + INSURANCE_PACKAGE_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INSURANCE_PACKAGE_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INSURANCE_PACKAGE_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                + INSURANCE_PACKAGE_DESC_BOB, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                + INSURANCE_PACKAGE_DESC_BOB, expectedMessage);

        // missing insurance package prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                + VALID_INSURANCE_PACKAGE_NAME_BOB, expectedMessage);
    }

    private void assertInvalidFieldFailure(String validDesc, String invalidDesc, String expectedError) {
        String testInput = ALL_VALID_FIELDS_NO_TAGS.replace(validDesc, invalidDesc) + VALID_TAGS;
        assertParseFailure(parser, testInput, expectedError);
    }

    @Test
    public void parse_invalidCompulsoryField_failure() {
        assertInvalidFieldFailure(NAME_DESC_BOB, INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(PHONE_DESC_BOB, INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(EMAIL_DESC_BOB, INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(ADDRESS_DESC_BOB, INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidOptionalField_failure() {
        assertInvalidFieldFailure(SALARY_DESC_BOB, INVALID_SALARY_DESC, Salary.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(DOB_DESC_BOB, INVALID_DOB_DESC, DateOfBirth.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(MARITAL_STATUS_DESC_BOB, INVALID_MARITAL_STATUS_DESC,
                MaritalStatus.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(OCCUPATION_DESC_BOB, INVALID_OCCUPATION_DESC, Occupation.MESSAGE_CONSTRAINTS);
        assertInvalidFieldFailure(DEPENDENTS_DESC_BOB, INVALID_DEPENDENTS_DESC, Dependents.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, ALL_VALID_FIELDS_NO_TAGS + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInvalidValues_reportsFirstFailure() {
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                + DEPENDENTS_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + ALL_VALID_FIELDS_NO_TAGS + VALID_TAGS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unspecifiedFields_success() {
        // Unspecified salary
        Person expectedPersonUnspecifiedSalary = new PersonBuilder(BOB)
                .withSalary(Salary.UNSPECIFIED_VALUE)
                .withTags(VALID_TAG_FRIEND).build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + " " + PREFIX_SALARY + Salary.UNSPECIFIED_VALUE + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                        + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonUnspecifiedSalary));

        // Unspecified date of birth
        Person expectedPersonUnspecifiedDob = new PersonBuilder(BOB)
                .withDateOfBirth(DateOfBirth.UNSPECIFIED_VALUE)
                .withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + SALARY_DESC_BOB + " " + PREFIX_DATE_OF_BIRTH + DateOfBirth.UNSPECIFIED_VALUE
                        + MARITAL_STATUS_DESC_BOB
                        + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonUnspecifiedDob));

        // Both unspecified
        Person expectedPersonBothUnspecified = new PersonBuilder(BOB)
                .withSalary(Salary.UNSPECIFIED_VALUE)
                .withDateOfBirth(DateOfBirth.UNSPECIFIED_VALUE)
                .withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + " " + PREFIX_SALARY + Salary.UNSPECIFIED_VALUE
                        + " " + PREFIX_DATE_OF_BIRTH + DateOfBirth.UNSPECIFIED_VALUE
                        + MARITAL_STATUS_DESC_BOB
                        + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + INSURANCE_PACKAGE_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonBothUnspecified));
    }
}
