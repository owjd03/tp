package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_sortByName_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.NAME;
        SortCommand command = new SortCommand(SortField.NAME);
        new SortCommand(SortField.NAME).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByPhone_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.PHONE;
        SortCommand command = new SortCommand(SortField.PHONE);
        new SortCommand(SortField.PHONE).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByEmail_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.EMAIL;
        SortCommand command = new SortCommand(SortField.EMAIL);
        new SortCommand(SortField.EMAIL).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByAddress_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.ADDRESS;
        SortCommand command = new SortCommand(SortField.ADDRESS);
        new SortCommand(SortField.ADDRESS).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortBySalary_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.SALARY;
        SortCommand command = new SortCommand(SortField.SALARY);
        new SortCommand(SortField.SALARY).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDateOfBirth_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH;
        SortCommand command = new SortCommand(SortField.DATEOFBIRTH);
        new SortCommand(SortField.DATEOFBIRTH).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(GEORGE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByMaritalStatus_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.MARITALSTATUS;
        SortCommand command = new SortCommand(SortField.MARITALSTATUS);
        new SortCommand(SortField.MARITALSTATUS).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByOccupation_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION;
        SortCommand command = new SortCommand(SortField.OCCUPATION);
        new SortCommand(SortField.OCCUPATION).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(CARL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDependent_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENT;
        SortCommand command = new SortCommand(SortField.DEPENDENT);
        new SortCommand(SortField.DEPENDENT).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(FIONA, model.getFilteredPersonList().get(model.getFilteredPersonList().size() - 1));
    }

    @Test
    public void execute_emptyAddressBook_success() {
        Model emptyModel = new ModelManager();
        Model expectedEmptyModel = new ModelManager();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.NAME;
        SortCommand command = new SortCommand(SortField.NAME);

        assertCommandSuccess(command, emptyModel, expectedMessage, expectedEmptyModel);
        assertEquals(0, emptyModel.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        SortCommand sortNameCommand = new SortCommand(SortField.NAME);
        SortCommand sortPhoneCommand = new SortCommand(SortField.PHONE);

        // same object -> returns true
        assertTrue(sortNameCommand.equals(sortNameCommand));

        // same values -> returns true
        SortCommand commandWithSameSortFieldValues = new SortCommand(SortField.NAME);
        assertTrue(sortNameCommand.equals(commandWithSameSortFieldValues));

        // different types -> returns false
        assertFalse(sortNameCommand.equals(1));

        // null -> returns false
        assertFalse(sortNameCommand.equals(null));

        // different sort field -> returns false
        assertFalse(sortNameCommand.equals(sortPhoneCommand));
    }

    @Test
    public void toStringMethod() {
        SortCommand sortCommand = new SortCommand(SortField.NAME);
        String expected = SortCommand.class.getCanonicalName() + "{sortField=" + SortField.NAME + "}";
        assertEquals(expected, sortCommand.toString());
    }
}
