package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Compulsory data fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final InsurancePackage insurancePackage;

    // Optional data fields
    private final Salary salary;
    private final DateOfBirth dateOfBirth;
    private final Set<Tag> tags = new HashSet<>();
    private final Occupation occupation;
    private final MaritalStatus maritalStatus;
    private final Dependents dependents;

    /**
     * Every compulsory field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Salary salary, DateOfBirth dateOfBirth,
                  MaritalStatus maritalStatus, Occupation occupation, Dependents dependents,
                  InsurancePackage insurancePackage, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags, salary, dateOfBirth, maritalStatus, occupation, dependents,
                insurancePackage);

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
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Salary getSalary() {
        return salary;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public Dependents getDependents() {
        return dependents;
    }

    public InsurancePackage getInsurancePackage() {
        return insurancePackage;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and compulsory data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && dateOfBirth.equals(otherPerson.dateOfBirth)
                && salary.equals(otherPerson.salary)
                && maritalStatus.equals(otherPerson.maritalStatus)
                && occupation.equals(otherPerson.occupation)
                && dependents.equals(otherPerson.dependents)
                && insurancePackage.equals(otherPerson.insurancePackage);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, salary, dateOfBirth, maritalStatus, occupation, dependents,
                insurancePackage, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("salary", salary)
                .add("dateOfBirth", dateOfBirth)
                .add("maritalStatus", maritalStatus)
                .add("occupation", occupation)
                .add("dependents", dependents)
                .add("insurancePackage", insurancePackage)
                .add("tags", tags)
                .toString();
    }

}
