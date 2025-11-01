package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;
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

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_MARITAL_STATUS = "Engaged";
    private static final String INVALID_DOB_FORMAT = "31-12-1990";
    private static final String INVALID_DOB_FUTURE = "3000-01-01";
    private static final String INVALID_SALARY = "-100";
    private static final String INVALID_OCCUPATION = " ";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_DEPENDENTS_NEGATIVE = "-1";
    private static final String INVALID_DEPENDENTS_NON_INTEGER = "1.5";
    private static final String INVALID_IP = "";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_MARITAL_STATUS = "Married";
    private static final String VALID_MARITAL_STATUS_CASING = "dIvOrCeD";
    private static final String VALID_SALARY_INTEGER = "5000";
    private static final String VALID_SALARY_DECIMAL = "12345.67";
    private static final String VALID_OCCUPATION = "Engineer";
    private static final String VALID_DOB = "1990-12-31";
    private static final String VALID_DEPENDENTS_ZERO = "0";
    private static final String VALID_DEPENDENTS_FIVE = "5";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_IP = "Undecided";
    private static final String VALID_IP_DESCRIPTION = "No package selected";

    private static final String WHITESPACE = " \t\r\n";

    @BeforeEach
    public void setUpCatalog() {
        InsurancePackage validPackage = new InsurancePackage(VALID_IP, VALID_IP_DESCRIPTION);
        InsuranceCatalog catalog = new InsuranceCatalog();
        catalog.setInsurancePackages(List.of(validPackage));
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseSalary_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSalary(INVALID_SALARY));
    }

    @Test
    public void parseSalary_validInteger_returnsSalary() throws Exception {
        Salary expectedSalary = new Salary(VALID_SALARY_INTEGER);
        assertEquals(expectedSalary, ParserUtil.parseSalary(VALID_SALARY_INTEGER));
    }

    @Test
    public void parseSalary_validDecimal_returnsSalary() throws Exception {
        Salary expectedSalary = new Salary(VALID_SALARY_DECIMAL);
        assertEquals(expectedSalary, ParserUtil.parseSalary(VALID_SALARY_DECIMAL));
    }

    @Test
    public void parseMaritalStatus_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, MaritalStatus.MESSAGE_CONSTRAINTS, () -> ParserUtil
                .parseMaritalStatus(INVALID_MARITAL_STATUS));
    }

    @Test
    public void parseMaritalStatus_emptyValue_throwsParseException() {
        assertThrows(ParseException.class, MaritalStatus.MESSAGE_CONSTRAINTS, () -> ParserUtil
                .parseMaritalStatus(""));
    }

    @Test
    public void parseMaritalStatus_whitespaceOnly_throwsParseException() {
        assertThrows(ParseException.class, MaritalStatus.MESSAGE_CONSTRAINTS, () -> ParserUtil
                .parseMaritalStatus(WHITESPACE));
    }

    @Test
    public void parseMaritalStatus_validValueWithoutWhitespace_returnsMaritalStatus() throws Exception {
        MaritalStatus expectedStatus = new MaritalStatus(VALID_MARITAL_STATUS);
        assertEquals(expectedStatus, ParserUtil.parseMaritalStatus(VALID_MARITAL_STATUS));
    }

    @Test
    public void parseMaritalStatus_validValueWithWhitespaceAndCasing_returnsNormalizedMaritalStatus() throws Exception {
        MaritalStatus expectedStatus = new MaritalStatus("Divorced");
        String statusWithWhitespace = WHITESPACE + VALID_MARITAL_STATUS_CASING + WHITESPACE;
        assertEquals(expectedStatus, ParserUtil.parseMaritalStatus(statusWithWhitespace));
    }

    @Test
    public void parseDateOfBirth_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateOfBirth(INVALID_DOB_FORMAT));
    }

    @Test
    public void parseDateOfBirth_invalidFutureDate_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateOfBirth(INVALID_DOB_FUTURE));
    }

    @Test
    public void parseDateOfBirth_validValue_returnsDateOfBirth() throws Exception {
        DateOfBirth expectedDob = new DateOfBirth(VALID_DOB);
        assertEquals(expectedDob, ParserUtil.parseDateOfBirth(VALID_DOB));
    }

    @Test
    public void parseOccupation_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseOccupation(INVALID_OCCUPATION));
    }

    @Test
    public void parseOccupation_validValue_returnsOccupation() throws Exception {
        Occupation expectedOccupation = new Occupation(VALID_OCCUPATION);
        assertEquals(expectedOccupation, ParserUtil.parseOccupation(VALID_OCCUPATION));
    }

    @Test
    public void parseDependents_invalidNegativeValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDependents(INVALID_DEPENDENTS_NEGATIVE));
    }

    @Test
    public void parseDependents_invalidNonInteger_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDependents(INVALID_DEPENDENTS_NON_INTEGER));
    }

    @Test
    public void parseDependents_validZero_returnsDependents() throws Exception {
        Dependents expectedDep = new Dependents(Integer.parseInt(VALID_DEPENDENTS_ZERO));
        assertEquals(expectedDep, ParserUtil.parseDependents(VALID_DEPENDENTS_ZERO));
    }

    @Test
    public void parseDependents_validFive_returnsDependents() throws Exception {
        Dependents expectedDep = new Dependents(Integer.parseInt(VALID_DEPENDENTS_FIVE));
        assertEquals(expectedDep, ParserUtil.parseDependents(VALID_DEPENDENTS_FIVE));
    }

    @Test
    public void parseInsurancePackage_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseInsurancePackage(INVALID_IP));
    }

    @Test
    public void parseInsurancePackage_validValue_returnsInsurancePackage() throws Exception {
        InsurancePackage expectedIp = new InsurancePackage(VALID_IP, VALID_IP_DESCRIPTION);
        assertEquals(expectedIp, ParserUtil.parseInsurancePackage(VALID_IP));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
