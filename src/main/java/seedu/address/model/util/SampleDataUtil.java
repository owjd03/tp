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
 * Contains utility methods for populating {@code AddressBook} and {@code InsuranceCatalog} with sample data.
 */
public class SampleDataUtil {
    private static final InsurancePackage GOLD_PACKAGE = new InsurancePackage("Gold",
            "Our premium, all-inclusive plan offering maximum benefits and total peace of mind.");
    private static final InsurancePackage SILVER_PACKAGE = new InsurancePackage("Silver",
            "Our most popular plan, offering a balanced blend of coverage and value.");
    private static final InsurancePackage BRONZE_PACKAGE = new InsurancePackage("Bronze",
            "A foundational plan that covers all your core, essential needs.");
    private static final InsurancePackage UNDECIDED = new InsurancePackage("Undecided",
            "No insurance package selected.");

    public static final Person ALEX = new Person(
            new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
            new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("3000"), new DateOfBirth("2001-01-01"),
            new MaritalStatus("Single"), new Occupation("Engineer"), new Dependents(0),
            GOLD_PACKAGE,
            getTagSet("friends"));

    public static final Person BERNICE = new Person(
            new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
            new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Salary("4000"),
            new DateOfBirth("1999-02-23"), new MaritalStatus("Married"), new Occupation("Manager"),
            new Dependents(2), SILVER_PACKAGE,
            getTagSet("colleagues", "friends"));

    public static final Person CHARLOTTE = new Person(
            new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
            new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("5000"),
            new DateOfBirth("1985-10-12"), new MaritalStatus("Married"),
            new Occupation("Director"), new Dependents(3),
            BRONZE_PACKAGE,
            getTagSet("neighbours"));

    public static final Person DAVID = new Person(
            new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
            new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("6000"),
            new DateOfBirth("1978-05-30"), new MaritalStatus("Divorced"), new Occupation("Consultant"),
            new Dependents(1), GOLD_PACKAGE,
            getTagSet("family"));

    public static final Person IRFAN = new Person(
            new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
            new Address("Blk 47 Tampines Street 20, #17-35"), new Salary("7000"), new DateOfBirth("1990-07-15"),
            new MaritalStatus("Single"), new Occupation("Analyst"), new Dependents(0),
            SILVER_PACKAGE,
            getTagSet("classmates"));

    public static final Person ROY = new Person(
            new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
            new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("8000"), new DateOfBirth("1988-11-25"),
            new MaritalStatus("Married"), new Occupation("Architect"), new Dependents(4),
            UNDECIDED,
            getTagSet("colleagues"));

    public static Person[] getSamplePersons() {
        return new Person[] {ALEX, BERNICE, CHARLOTTE, DAVID, IRFAN, ROY};
    }

    public static InsurancePackage[] getSampleInsurancePackages() {
        return new InsurancePackage[] {GOLD_PACKAGE, SILVER_PACKAGE, BRONZE_PACKAGE, UNDECIDED};
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

    public static ReadOnlyInsuranceCatalog getSampleInsuranceCatalog() {
        InsuranceCatalog sampleIc = new InsuranceCatalog();
        for (InsurancePackage sampleInsurancePackage : getSampleInsurancePackages()) {
            sampleIc.addInsurancePackage(sampleInsurancePackage);
        }
        return sampleIc;
    }
}
