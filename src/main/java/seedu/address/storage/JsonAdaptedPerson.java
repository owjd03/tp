package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.MaritalStatusEnum;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String salary;
    private final String dateOfBirth;
    private final String maritalStatus;
    private final String occupation;
    private final int dependents;
    private final String insurancePackage;

    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("salary") String salary, @JsonProperty("dateOfBirth") String dateOfBirth,
                             @JsonProperty("maritalStatus") String maritalStatus,
                             @JsonProperty("occupation") String occupation,
                             @JsonProperty("dependents") int dependents,
                             @JsonProperty("insurancePackage") String insurancePackage,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.dateOfBirth = dateOfBirth;
        this.maritalStatus = maritalStatus;
        this.occupation = occupation;
        this.dependents = dependents;
        this.insurancePackage = insurancePackage;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        salary = source.getSalary().getValue();
        dateOfBirth = source.getDateOfBirth().getValue();
        maritalStatus = source.getMaritalStatus().getValue();
        occupation = source.getOccupation().getValue();
        dependents = source.getDependents().getValue();
        insurancePackage = source.getInsurancePackage().getPackageName();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType(ReadOnlyInsuranceCatalog catalog) throws IllegalValueException {
        final Name modelName = validateAndGetName();
        final Phone modelPhone = validateAndGetPhone();
        final Email modelEmail = validateAndGetEmail();
        final Address modelAddress = validateAndGetAddress();
        final Salary modelSalary = validateAndGetSalary();
        final DateOfBirth modelDateOfBirth = validateAndGetDateOfBirth();
        final MaritalStatus modelMaritalStatus = validateAndGetMaritalStatus();
        final Occupation modelOccupation = validateAndGetOccupation();
        final Dependents modelDependents = validateAndGetDependents();
        final InsurancePackage modelInsurancePackage = validateAndGetInsurancePackage(catalog);

        final List<Tag> personTags = validateAndGetTags();
        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelSalary, modelDateOfBirth,
                modelMaritalStatus, modelOccupation, modelDependents, modelInsurancePackage, modelTags);
    }

    private void validateField(String value, String fieldName, Predicate<String> validator, String constraintMessage)
            throws IllegalValueException {
        if (value == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, fieldName));
        }

        if (!validator.test(value)) {
            throw new IllegalValueException(constraintMessage);
        }
    }

    private Name validateAndGetName() throws IllegalValueException {
        validateField(name, Name.class.getSimpleName(), Name::isValidName, Name.MESSAGE_CONSTRAINTS);
        return new Name(name);
    }

    private Phone validateAndGetPhone() throws IllegalValueException {
        validateField(phone, Phone.class.getSimpleName(), Phone::isValidPhone, Phone.MESSAGE_CONSTRAINTS);
        return new Phone(phone);
    }

    private Email validateAndGetEmail() throws IllegalValueException {
        validateField(email, Email.class.getSimpleName(), Email::isValidEmail, Email.MESSAGE_CONSTRAINTS);
        return new Email(email);
    }

    private Address validateAndGetAddress() throws IllegalValueException {
        validateField(address, Address.class.getSimpleName(), Address::isValidAddress, Address.MESSAGE_CONSTRAINTS);
        return new Address(address);
    }

    private Salary validateAndGetSalary() throws IllegalValueException {
        if (salary == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Salary.class.getSimpleName()));
        }
        String sanitizedSalary = salary.replace("$", "").replace(",", "");
        if (!Salary.isValidSalary(sanitizedSalary)) {
            throw new IllegalValueException(Salary.MESSAGE_CONSTRAINTS);
        }
        return new Salary(sanitizedSalary);
    }

    private DateOfBirth validateAndGetDateOfBirth() throws IllegalValueException {
        validateField(dateOfBirth, DateOfBirth.class.getSimpleName(), DateOfBirth::isValidDateOfBirth,
                DateOfBirth.MESSAGE_CONSTRAINTS);
        return new DateOfBirth(dateOfBirth);
    }

    private MaritalStatus validateAndGetMaritalStatus() throws IllegalValueException {
        validateField(maritalStatus, MaritalStatus.class.getSimpleName(), MaritalStatusEnum::isValidMaritalStatus,
                MaritalStatus.MESSAGE_CONSTRAINTS);
        return new MaritalStatus(maritalStatus);
    }

    private Occupation validateAndGetOccupation() throws IllegalValueException {
        validateField(occupation, Occupation.class.getSimpleName(), Occupation::isValidOccupation,
                Occupation.MESSAGE_CONSTRAINTS);
        return new Occupation(occupation);
    }


    private Dependents validateAndGetDependents() throws IllegalValueException {
        if (!Dependents.isValidDependents(dependents)) {
            throw new IllegalValueException(Dependents.MESSAGE_CONSTRAINTS);
        }
        return new Dependents(dependents);
    }


    private InsurancePackage validateAndGetInsurancePackage(ReadOnlyInsuranceCatalog catalog) {
        if (insurancePackage == null || insurancePackage.isEmpty()) {
            return catalog.getUndecidedPackage();
        } else {
            return catalog.getPackage(insurancePackage)
                    .orElseGet(catalog::getUndecidedPackage);
        }
    }

    private List<Tag> validateAndGetTags() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        return personTags;
    }
}
