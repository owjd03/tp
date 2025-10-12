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
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEPENDENTS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DOB_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MARITAL_STATUS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OCCUPATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SALARY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
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
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND
                        + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                        + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB
                + DEPENDENTS_DESC_BOB + OCCUPATION_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple salaries
        assertParseFailure(parser, SALARY_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SALARY));

        // multiple dates of birth
        assertParseFailure(parser, DOB_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));

        // multiple marital statuses
        assertParseFailure(parser, MARITAL_STATUS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MARITAL_STATUS));

        // multiple occupations
        assertParseFailure(parser, OCCUPATION_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OCCUPATION));

        // multiple dependents
        assertParseFailure(parser, DEPENDENTS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEPENDENTS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + SALARY_DESC_AMY + DOB_DESC_AMY + MARITAL_STATUS_DESC_AMY + OCCUPATION_DESC_AMY
                        + DEPENDENTS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE,
                        PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS, PREFIX_OCCUPATION,
                        PREFIX_DEPENDENTS));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid salary
        assertParseFailure(parser, INVALID_SALARY_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SALARY));

        // invalid date of birth
        assertParseFailure(parser, INVALID_DOB_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));

        // invalid marital status
        assertParseFailure(parser, INVALID_MARITAL_STATUS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MARITAL_STATUS));

        // invalid occupation
        assertParseFailure(parser, INVALID_OCCUPATION_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OCCUPATION));

        // invalid dependents
        assertParseFailure(parser, INVALID_DEPENDENTS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEPENDENTS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid salary
        assertParseFailure(parser, validExpectedPersonString + INVALID_SALARY_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SALARY));

        // invalid date of birth
        assertParseFailure(parser, validExpectedPersonString + INVALID_DOB_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));

        // invalid marital status
        assertParseFailure(parser, validExpectedPersonString + INVALID_MARITAL_STATUS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MARITAL_STATUS));

        // invalid occupation
        assertParseFailure(parser, validExpectedPersonString + INVALID_OCCUPATION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OCCUPATION));

        // invalid dependents
        assertParseFailure(parser, validExpectedPersonString + INVALID_DEPENDENTS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEPENDENTS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + SALARY_DESC_AMY + DOB_DESC_AMY + MARITAL_STATUS_DESC_AMY + OCCUPATION_DESC_AMY
                        + DEPENDENTS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                        + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing salary prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + VALID_SALARY_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing date of birth prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + VALID_DOB_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);
        // missing marital status prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + VALID_MARITAL_STATUS_BOB + OCCUPATION_DESC_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing occupation prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + VALID_OCCUPATION_BOB
                    + DEPENDENTS_DESC_BOB, expectedMessage);

        // missing dependents prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                    + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                    + VALID_DEPENDENTS_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                    + VALID_SALARY_BOB + VALID_DOB_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid salary
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_SALARY_DESC + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                + DEPENDENTS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Salary.MESSAGE_CONSTRAINTS);

        // invalid date of birth
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + INVALID_DOB_DESC + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                + DEPENDENTS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, DateOfBirth.MESSAGE_CONSTRAINTS);

        // invalid marital status
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + INVALID_MARITAL_STATUS_DESC + OCCUPATION_DESC_BOB
                + DEPENDENTS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, MaritalStatus.MESSAGE_CONSTRAINTS);

        // invalid occupation
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + INVALID_OCCUPATION_DESC
                + DEPENDENTS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Occupation.MESSAGE_CONSTRAINTS);

        // invalid dependents
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                + INVALID_DEPENDENTS_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Dependents.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB + DEPENDENTS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                        + DEPENDENTS_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + SALARY_DESC_BOB + DOB_DESC_BOB + MARITAL_STATUS_DESC_BOB + OCCUPATION_DESC_BOB
                + DEPENDENTS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
