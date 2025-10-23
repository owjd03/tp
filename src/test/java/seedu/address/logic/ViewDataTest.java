package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.util.SampleDataUtil.getTagSet;

import org.junit.jupiter.api.Test;

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

public class ViewDataTest {
    private static final Person testPerson = new Person(new Name("Alex Yeoh"), new Phone("87438807"),
            new Email("alexyeoh@example.com"), new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("3000"),
            new DateOfBirth("2001-01-01"), new MaritalStatus("Single"), new Occupation("Engineer"),
            new Dependents(0), new InsurancePackage("Gold",
                    "Our premium, all-inclusive plan offering maximum benefits and total peace of mind."),
            getTagSet("friends"));

    //Different name
    private static final Person dummyPerson = new Person(new Name("Alex Yang"), new Phone("87438807"),
            new Email("alexyeoh@example.com"), new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("3000"),
            new DateOfBirth("2001-01-01"), new MaritalStatus("Single"), new Occupation("Engineer"),
            new Dependents(0), new InsurancePackage("Gold",
                    "Our premium, all-inclusive plan offering maximum benefits and total peace of mind."),
            getTagSet("friends"));

    private static final ViewData expectedTrueViewData = new ViewData(true, testPerson);
    private static final ViewData expectedTrueNoViewData = new ViewData(false, null);
    @Test
    public void equals() {
        final ViewData standardViewData = new ViewData(true, testPerson);
        final ViewData standardNoViewData = new ViewData(false, null);

        // same values -> returns true
        assertTrue(standardViewData.equals(expectedTrueViewData));
        assertTrue(standardNoViewData.equals(expectedTrueNoViewData));

        // same object -> returns true
        assertTrue(standardViewData.equals(standardViewData));
        assertTrue(standardNoViewData.equals(standardNoViewData));

        // null -> returns false
        assertFalse(standardViewData.equals(null));
        assertFalse(standardNoViewData.equals(null));

        // different types -> returns false
        assertFalse(standardViewData.equals(new ViewData(false, null)));
        assertFalse(standardNoViewData.equals(new ViewData(true, dummyPerson)));

        // different boolean -> returns false
        assertFalse(standardViewData.equals(new ViewData(false, testPerson)));
        assertFalse(standardNoViewData.equals(new ViewData(true, null)));

        // different person -> returns false
        assertFalse(standardViewData.equals(new ViewData(true, dummyPerson)));
        assertFalse(standardNoViewData.equals(new ViewData(false, dummyPerson)));

    }

}
