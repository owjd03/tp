package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.util.SampleDataUtil.getTagSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ViewData;
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

public class CommandResultTest {
    private final CommandResult commandResult = new CommandResult("feedback");
    private final Person testPerson = new Person(new Name("Alex Yeoh"), new Phone("87438807"),
            new Email("alexyeoh@example.com"), new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("3000"),
            new DateOfBirth("2001-01-01"), new MaritalStatus("Single"), new Occupation("Engineer"),
            new Dependents(0), new InsurancePackage("Gold",
                    "Our premium, all-inclusive plan offering maximum benefits and total peace of mind."),
            getTagSet("friends"));

    @Test
    public void equals() {

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(false, null), false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true,
                new ViewData(false, null), false, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(false, null), false, true)));

        //different viewData value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(true, null), false, false)));
        assertFalse(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(false, testPerson), false, false)));
        assertFalse(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(true, testPerson), false, false)));

        //different showPackage value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false,
                new ViewData(false, null), true, false)));
    }

    @Test
    public void hashcode() {

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true,
                new ViewData(false, null), false, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false,
                new ViewData(false, null), false, true).hashCode());

        // different view value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false,
                new ViewData(true, null), false, false).hashCode());
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false,
                new ViewData(false, testPerson), false, true).hashCode());
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false,
                new ViewData(true, testPerson), false, true).hashCode());

    }

    @Test
    public void toStringMethod() {
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", viewData=" + commandResult.getViewData() + ", showPackage=" + commandResult.isShowPackage()
                + ", exit=" + commandResult.isExit() + "}";
        assertEquals(expected, commandResult.toString());
    }
}
