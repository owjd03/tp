package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.ReadOnlyAddressBook;
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
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("3000"), new DateOfBirth("2001-01-01"),
                    new MaritalStatus("Single"), new Occupation("Engineer"), new Dependents(0),
                    getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Salary("4000"),
                    new DateOfBirth("1999-02-23"), new MaritalStatus("Married"), new Occupation("Manager"),
                    new Dependents(2), getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("5000"),
                    new DateOfBirth("1985-10-12"), new MaritalStatus("Married"),
                    new Occupation("Director"), new Dependents(3),
                    getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("6000"),
                    new DateOfBirth("1978-05-30"), new MaritalStatus("Divorced"), new Occupation("Consultant"),
                    new Dependents(1), getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), new Salary("7000"), new DateOfBirth("1990-07-15"),
                    new MaritalStatus("Single"), new Occupation("Analyst"), new Dependents(0),
                    getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("8000"), new DateOfBirth("1988-11-25"),
                    new MaritalStatus("Married"), new Occupation("Architect"), new Dependents(4),
                    getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    public static InsurancePackage[] getSampleInsurancePackages() {
        return new InsurancePackage[]{
            new InsurancePackage("Gold",
                    "The luxurious insurance plan covering all aspects of your life."),
            new InsurancePackage("Silver",
                    "The premium insurance plan covering most aspects of your life."),
            new InsurancePackage("Bronze",
                    "The standard insurance plan covering some aspects of your life"),
            new InsurancePackage("Undecided",
                    "Undecided for now.")
        };
    }

    public static ReadOnlyInsuranceCatalog getSampleInsuranceCatalog() {
        InsuranceCatalog sampleIc = new InsuranceCatalog();
        for (InsurancePackage sampleInsurancePackage : getSampleInsurancePackages()) {
            sampleIc.addInsurancePackage(sampleInsurancePackage);
        }
        return sampleIc;
    }
}
