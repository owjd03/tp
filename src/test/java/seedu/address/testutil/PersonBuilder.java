package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

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
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_SALARY = "1000";
    public static final String DEFAULT_DATE_OF_BIRTH = "2000-01-01";
    public static final String DEFAULT_MARITAL_STATUS = "Single";
    public static final String DEFAULT_OCCUPATION = "Unemployed";
    public static final int DEFAULT_DEPENDENTS = 0;

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Salary salary;
    private DateOfBirth dateOfBirth;
    private MaritalStatus maritalStatus;
    private Occupation occupation;
    private Dependents dependents;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        salary = new Salary(DEFAULT_SALARY);
        dateOfBirth = new DateOfBirth(DEFAULT_DATE_OF_BIRTH);
        maritalStatus = new MaritalStatus(DEFAULT_MARITAL_STATUS);
        occupation = new Occupation(DEFAULT_OCCUPATION);
        dependents = new Dependents(DEFAULT_DEPENDENTS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        salary = personToCopy.getSalary();
        dateOfBirth = personToCopy.getDateOfBirth();
        maritalStatus = personToCopy.getMaritalStatus();
        occupation = personToCopy.getOccupation();
        dependents = personToCopy.getDependents();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Salary} of the {@code Person} that we are building.
     */
    public PersonBuilder withSalary(String salary) {
        this.salary = new Salary(salary);
        return this;
    }

    /**
     * Sets the {@code DateOfBirth} of the {@code Person} that we are building.
     */
    public PersonBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = new DateOfBirth(dateOfBirth);
        return this;
    }

    /**
     * Sets the {@code MaritalStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withMaritalStatus(String maritalStatus) {
        this.maritalStatus = new MaritalStatus(maritalStatus);
        return this;
    }

    /**
     * Sets the {@code Occupation} of the {@code Person} that we are building.
     */
    public PersonBuilder withOccupation(String occupation) {
        this.occupation = new Occupation(occupation);
        return this;
    }

    /**
     * Sets the {@code Dependents} of the {@code Person} that we are building.
     */
    public PersonBuilder withDependents(int dependents) {
        this.dependents = new Dependents(dependents);
        return this;
    }

    /**
     * Builds the Person object.
     *
     * @return The constructed Person object.
     */
    public Person build() {
        return new Person(name, phone, email, address, salary, dateOfBirth, maritalStatus, occupation, dependents,
                tags);
    }

}
