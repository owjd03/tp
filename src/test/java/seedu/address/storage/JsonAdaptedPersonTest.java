package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.util.SampleDataUtil;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_SALARY = "-5000";
    private static final String INVALID_DATE_OF_BIRTH = "32-13-2020";
    private static final String INVALID_MARITAL_STATUS = "Complicated";
    private static final String INVALID_OCCUPATION = " ";
    private static final int INVALID_DEPENDENTS = -2;
    private static final String INVALID_INSURANCE_PACKAGE = "Diamond";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_SALARY = BENSON.getSalary().toString();
    private static final String VALID_DATE_OF_BIRTH = BENSON.getDateOfBirth().toString();
    private static final String VALID_MARITAL_STATUS = BENSON.getMaritalStatus().toString();
    private static final String VALID_OCCUPATION = BENSON.getOccupation().toString();
    private static final int VALID_DEPENDENTS = BENSON.getDependents().toInt();
    private static final String VALID_INSURANCE_PACKAGE = BENSON.getInsurancePackage().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final ReadOnlyInsuranceCatalog VALID_CATALOG = SampleDataUtil.getSampleInsuranceCatalog();

    /**
     * Populates the static lists in InsuranceCatalog before any tests are run.
     */
    @BeforeAll
    public static void setupInsuranceCatalog() {
        new InsuranceCatalog(SampleDataUtil.getSampleInsuranceCatalog());
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullSalary_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "Salary");
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidSalary_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                INVALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = "Salaries can take any non-negative values, should not be blank, "
                + "and should have up to 2 decimal places";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullDateOfBirth_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, null, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "DateOfBirth");
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidDateOfBirth_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        INVALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = "Date of birth must be a valid date in the format yyyy-MM-dd and not in the future";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullMaritalStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, null, VALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "MaritalStatus");
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidMaritalStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, INVALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = "Marital status must be one of the predefined constants: Single, Married, Divorced,"
                + " Widowed";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullOccupation_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, null, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "Occupation");
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidOccupation_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, INVALID_OCCUPATION, VALID_DEPENDENTS,
                VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = "Occupation cannot be left empty";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidDependents_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION,
                        INVALID_DEPENDENTS, VALID_INSURANCE_PACKAGE, VALID_TAGS);
        String expectedMessage = "Number of dependents must be a non-negative integer";
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullInsurancePackage_throwsIllegalValueException() throws IllegalValueException {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SALARY, VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                null, VALID_TAGS);

        InsurancePackage expectedPackage = VALID_CATALOG.getUndecidedPackage();

        Person modelPerson = person.toModelType(VALID_CATALOG);

        assertEquals(expectedPackage, modelPerson.getInsurancePackage());
    }

    @Test
    public void toModelType_invalidInsurancePackage_throwsIllegalValueException() throws IllegalValueException {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        INVALID_INSURANCE_PACKAGE, VALID_TAGS);

        InsurancePackage expectedPackage = VALID_CATALOG.getUndecidedPackage();

        Person modelPerson = person.toModelType(VALID_CATALOG);

        assertEquals(expectedPackage, modelPerson.getInsurancePackage());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_SALARY,
                        VALID_DATE_OF_BIRTH, VALID_MARITAL_STATUS, VALID_OCCUPATION, VALID_DEPENDENTS,
                        VALID_INSURANCE_PACKAGE, invalidTags);
        assertThrows(IllegalValueException.class, () -> person.toModelType(VALID_CATALOG));
    }

}
