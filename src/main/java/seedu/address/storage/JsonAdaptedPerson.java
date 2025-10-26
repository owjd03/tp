package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        salary = source.getSalary().value;
        dateOfBirth = source.getDateOfBirth().value;
        maritalStatus = source.getMaritalStatus().value;
        occupation = source.getOccupation().value;
        dependents = source.getDependents().value;
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

    /**
     * Validates and parses the {@code name} field.
     */
    private Name validateAndGetName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    /**
     * Validates and parses the {@code phone} field.
     */
    private Phone validateAndGetPhone() throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(phone);
    }

    /**
     * Validates and parses the {@code email} field.
     */
    private Email validateAndGetEmail() throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(email);
    }

    /**
     * Validates and parses the {@code address} field.
     */
    private Address validateAndGetAddress() throws IllegalValueException {
        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(address);
    }

    /**
     * Validates, sanitizes, and parses the {@code salary} field.
     */
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

    /**
     * Validates and parses the {@code dateOfBirth} field.
     */
    private DateOfBirth validateAndGetDateOfBirth() throws IllegalValueException {
        if (dateOfBirth == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    DateOfBirth.class.getSimpleName()));
        }
        if (!DateOfBirth.isValidDateOfBirth(dateOfBirth)) {
            throw new IllegalValueException(DateOfBirth.MESSAGE_CONSTRAINTS);
        }
        return new DateOfBirth(dateOfBirth);
    }

    /**
     * Validates and parses the {@code maritalStatus} field.
     */
    private MaritalStatus validateAndGetMaritalStatus() throws IllegalValueException {
        if (maritalStatus == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MaritalStatus.class.getSimpleName()));
        }
        if (!MaritalStatusEnum.isValidMaritalStatus(maritalStatus)) {
            throw new IllegalValueException(MaritalStatus.MESSAGE_CONSTRAINTS);
        }
        return new MaritalStatus(maritalStatus);
    }

    /**
     * Validates and parses the {@code occupation} field.
     */
    private Occupation validateAndGetOccupation() throws IllegalValueException {
        if (occupation == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Occupation.class.getSimpleName()));
        }
        if (!Occupation.isValidOccupation(occupation)) {
            throw new IllegalValueException(Occupation.MESSAGE_CONSTRAINTS);
        }
        return new Occupation(occupation);
    }

    /**
     * Validates and parses the {@code dependents} field.
     */
    private Dependents validateAndGetDependents() throws IllegalValueException {
        if (!Dependents.isValidDependents(dependents)) {
            throw new IllegalValueException(Dependents.MESSAGE_CONSTRAINTS);
        }
        return new Dependents(dependents);
    }

    /**
     * Validates and parses the {@code insurancePackage} field using the catalog.
     */
    private InsurancePackage validateAndGetInsurancePackage(ReadOnlyInsuranceCatalog catalog) {
        if (insurancePackage == null || insurancePackage.isEmpty()) {
            return catalog.getUndecidedPackage();
        } else {
            return catalog.getPackage(insurancePackage)
                    .orElseGet(catalog::getUndecidedPackage);
        }
    }

    /**
     * Validates and parses the {@code tags} field.
     */
    private List<Tag> validateAndGetTags() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        return personTags;
    }
}
