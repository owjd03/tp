package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.util.SampleDataUtil;

public class JsonAdaptedPersonTest {
    /**
     * A builder for {@code JsonAdaptedPerson} to be used in tests.
     */
    private static class JsonAdaptedPersonBuilder {
        private static final String DEFAULT_NAME = BENSON.getName().toString();
        private static final String DEFAULT_PHONE = BENSON.getPhone().toString();
        private static final String DEFAULT_EMAIL = BENSON.getEmail().toString();
        private static final String DEFAULT_ADDRESS = BENSON.getAddress().toString();
        private static final String DEFAULT_SALARY = BENSON.getSalary().toString();
        private static final String DEFAULT_DATE_OF_BIRTH = BENSON.getDateOfBirth().toString();
        private static final String DEFAULT_MARITAL_STATUS = BENSON.getMaritalStatus().toString();
        private static final String DEFAULT_OCCUPATION = BENSON.getOccupation().toString();
        private static final int DEFAULT_DEPENDENTS = BENSON.getDependents().getValue();
        private static final String DEFAULT_INSURANCE_PACKAGE = BENSON.getInsurancePackage().toString();
        private static final List<JsonAdaptedTag> DEFAULT_TAGS = BENSON.getTags().stream().map(JsonAdaptedTag::new)
                .collect(Collectors.toList());

        private String name;
        private String phone;
        private String email;
        private String address;
        private String salary;
        private String dateOfBirth;
        private String maritalStatus;
        private String occupation;
        private int dependents;
        private String insurancePackage;
        private List<JsonAdaptedTag> tags;

        /**
         * Creates a {@code JsonAdaptedPersonBuilder} with default valid details.
         */
        public JsonAdaptedPersonBuilder() {
            name = DEFAULT_NAME;
            phone = DEFAULT_PHONE;
            email = DEFAULT_EMAIL;
            address = DEFAULT_ADDRESS;
            salary = DEFAULT_SALARY;
            dateOfBirth = DEFAULT_DATE_OF_BIRTH;
            maritalStatus = DEFAULT_MARITAL_STATUS;
            occupation = DEFAULT_OCCUPATION;
            dependents = DEFAULT_DEPENDENTS;
            insurancePackage = DEFAULT_INSURANCE_PACKAGE;
            tags = DEFAULT_TAGS;
        }

        /**
         * Sets the {@code name} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code phone} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Sets the {@code email} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the {@code address} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the {@code salary} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withSalary(String salary) {
            this.salary = salary;
            return this;
        }

        /**
         * Sets the {@code dateOfBirth} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        /**
         * Sets the {@code maritalStatus} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
            return this;
        }

        /**
         * Sets the {@code occupation} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withOccupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        /**
         * Sets the {@code dependents} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withDependents(int dependents) {
            this.dependents = dependents;
            return this;
        }

        /**
         * Sets the {@code insurancePackage} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withInsurancePackage(String insurancePackage) {
            this.insurancePackage = insurancePackage;
            return this;
        }

        /**
         * Sets the {@code tags} of the {@code JsonAdaptedPerson}
         */
        public JsonAdaptedPersonBuilder withTags(List<JsonAdaptedTag> tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Builds the JsonAdaptedPerson
         */
        public JsonAdaptedPerson build() {
            return new JsonAdaptedPerson(name, phone, email, address, salary, dateOfBirth, maritalStatus, occupation,
                    dependents, insurancePackage, tags);
        }
    }

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

    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream().map(JsonAdaptedTag::new).toList();
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
    public void toModelType_invalidFields_throwsIllegalValueException() {
        JsonAdaptedPerson person1 = new JsonAdaptedPersonBuilder().withName(INVALID_NAME).build();
        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, () -> person1.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person2 = new JsonAdaptedPersonBuilder().withPhone(INVALID_PHONE).build();
        assertThrows(IllegalValueException.class, Phone.MESSAGE_CONSTRAINTS, () -> person2.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person3 = new JsonAdaptedPersonBuilder().withEmail(INVALID_EMAIL).build();
        assertThrows(IllegalValueException.class, Email.MESSAGE_CONSTRAINTS, () -> person3.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person4 = new JsonAdaptedPersonBuilder().withAddress(INVALID_ADDRESS).build();
        assertThrows(IllegalValueException.class, Address.MESSAGE_CONSTRAINTS, () -> person4
                .toModelType(VALID_CATALOG));

        JsonAdaptedPerson person5 = new JsonAdaptedPersonBuilder().withSalary(INVALID_SALARY).build();
        assertThrows(IllegalValueException.class, Salary.MESSAGE_CONSTRAINTS, () -> person5.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person6 = new JsonAdaptedPersonBuilder().withMaritalStatus(INVALID_MARITAL_STATUS).build();
        assertThrows(IllegalValueException.class, MaritalStatus.MESSAGE_CONSTRAINTS, () -> person6
                .toModelType(VALID_CATALOG));

        JsonAdaptedPerson person7 = new JsonAdaptedPersonBuilder().withOccupation(INVALID_OCCUPATION).build();
        assertThrows(IllegalValueException.class, Occupation.MESSAGE_CONSTRAINTS, () -> person7
                .toModelType(VALID_CATALOG));

        JsonAdaptedPerson person8 = new JsonAdaptedPersonBuilder().withDependents(INVALID_DEPENDENTS).build();
        assertThrows(IllegalValueException.class, Dependents.MESSAGE_CONSTRAINTS, () -> person8
                .toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_invalidDateOfBirth_throwsIllegalValueException() {
        // Invalid format
        JsonAdaptedPerson person1 = new JsonAdaptedPersonBuilder().withDateOfBirth(INVALID_DATE_OF_BIRTH).build();
        assertThrows(IllegalValueException.class, DateOfBirth.MESSAGE_CONSTRAINTS, () -> person1
                .toModelType(VALID_CATALOG));

        // Non-existent date
        JsonAdaptedPerson person2 = new JsonAdaptedPersonBuilder().withDateOfBirth("2025-09-31").build();
        assertThrows(IllegalValueException.class, DateOfBirth.MESSAGE_CONSTRAINTS, () -> person2
                .toModelType(VALID_CATALOG));

        // Non-leap year
        JsonAdaptedPerson person3 = new JsonAdaptedPersonBuilder().withDateOfBirth("2023-02-29").build();
        assertThrows(IllegalValueException.class, DateOfBirth.MESSAGE_CONSTRAINTS, () -> person3
                .toModelType(VALID_CATALOG));

        // Future date
        String futureDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        JsonAdaptedPerson person4 = new JsonAdaptedPersonBuilder().withDateOfBirth(futureDate).build();
        assertThrows(IllegalValueException.class, DateOfBirth.MESSAGE_CONSTRAINTS, () -> person4
                .toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullFields_throwsIllegalValueException() {
        JsonAdaptedPerson person1 = new JsonAdaptedPersonBuilder().withName(null).build();
        String expectedMsg1 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg1, () -> person1.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person2 = new JsonAdaptedPersonBuilder().withPhone(null).build();
        String expectedMsg2 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg2, () -> person2.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person3 = new JsonAdaptedPersonBuilder().withEmail(null).build();
        String expectedMsg3 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg3, () -> person3.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person4 = new JsonAdaptedPersonBuilder().withAddress(null).build();
        String expectedMsg4 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg4, () -> person4.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person5 = new JsonAdaptedPersonBuilder().withSalary(null).build();
        String expectedMsg5 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Salary.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg5, () -> person5.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person6 = new JsonAdaptedPersonBuilder().withDateOfBirth(null).build();
        String expectedMsg6 = String.format(MISSING_FIELD_MESSAGE_FORMAT, DateOfBirth.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg6, () -> person6.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person7 = new JsonAdaptedPersonBuilder().withMaritalStatus(null).build();
        String expectedMsg7 = String.format(MISSING_FIELD_MESSAGE_FORMAT, MaritalStatus.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg7, () -> person7.toModelType(VALID_CATALOG));

        JsonAdaptedPerson person8 = new JsonAdaptedPersonBuilder().withOccupation(null).build();
        String expectedMsg8 = String.format(MISSING_FIELD_MESSAGE_FORMAT, Occupation.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMsg8, () -> person8.toModelType(VALID_CATALOG));
    }

    @Test
    public void toModelType_nullInsurancePackage_defaultsToUndecided() throws IllegalValueException {
        JsonAdaptedPerson person = new JsonAdaptedPersonBuilder().withInsurancePackage(null).build();
        InsurancePackage expectedPackage = VALID_CATALOG.getUndecidedPackage();
        Person modelPerson = person.toModelType(VALID_CATALOG);
        assertEquals(expectedPackage, modelPerson.getInsurancePackage());
    }

    @Test
    public void toModelType_invalidInsurancePackage_defaultsToUndecided() throws IllegalValueException {
        JsonAdaptedPerson person = new JsonAdaptedPersonBuilder().withInsurancePackage(INVALID_INSURANCE_PACKAGE)
                .build();
        InsurancePackage expectedPackage = VALID_CATALOG.getUndecidedPackage();
        Person modelPerson = person.toModelType(VALID_CATALOG);
        assertEquals(expectedPackage, modelPerson.getInsurancePackage());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPersonBuilder().withTags(invalidTags).build();
        assertThrows(IllegalValueException.class, () -> person.toModelType(VALID_CATALOG));
    }

}
