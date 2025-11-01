package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.HARRY;
import static seedu.address.testutil.TypicalPersons.MARRY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand.SortDirection;
import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());
    private Model expectedModel =
            new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());

    /**
     * Creates a model with persons that have "Unspecified" or -1 fields
     * Contains 2 new persons with unspecified fields: HARRY and MARRY
     * HARRY (unspecified salary)
     * MARRY (unspecified dateOfBirth, occupation, dependents)
     * @return Model including person with "Unspecified" or -1 fields
     */
    private Model createModelWithUnspecifiedPersons() {
        Model testModel = new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());
        testModel.addPerson(HARRY);
        testModel.addPerson(MARRY);
        return testModel;
    }



    @Test
    public void execute_sortByNameAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.NAME + " in ascending order";
        SortCommand command = new SortCommand(SortField.NAME, SortDirection.ASCENDING);
        new SortCommand(SortField.NAME, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByNameDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.NAME + " in descending order";
        SortCommand command = new SortCommand(SortField.NAME, SortDirection.DESCENDING);
        new SortCommand(SortField.NAME, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(GEORGE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByPhoneAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.PHONE + " in ascending order";
        SortCommand command = new SortCommand(SortField.PHONE, SortDirection.ASCENDING);
        new SortCommand(SortField.PHONE, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByPhoneDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.PHONE + " in descending order";
        SortCommand command = new SortCommand(SortField.PHONE, SortDirection.DESCENDING);
        new SortCommand(SortField.PHONE, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(BENSON, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByEmailAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.EMAIL + " in ascending order";
        SortCommand command = new SortCommand(SortField.EMAIL, SortDirection.ASCENDING);
        new SortCommand(SortField.EMAIL, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByEmailDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.EMAIL + " in descending order";
        SortCommand command = new SortCommand(SortField.EMAIL, SortDirection.DESCENDING);
        new SortCommand(SortField.EMAIL, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ELLE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByAddressAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.ADDRESS + " in ascending order";
        SortCommand command = new SortCommand(SortField.ADDRESS, SortDirection.ASCENDING);
        new SortCommand(SortField.ADDRESS, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByAddressDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.ADDRESS + " in descending order";
        SortCommand command = new SortCommand(SortField.ADDRESS, SortDirection.DESCENDING);
        new SortCommand(SortField.ADDRESS, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(CARL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortBySalaryAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.SALARY + " in ascending order";
        SortCommand command = new SortCommand(SortField.SALARY, SortDirection.ASCENDING);
        new SortCommand(SortField.SALARY, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortBySalaryDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.SALARY + " in descending order";
        SortCommand command = new SortCommand(SortField.SALARY, SortDirection.DESCENDING);
        new SortCommand(SortField.SALARY, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(GEORGE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDateOfBirthAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH + " in ascending order";
        SortCommand command = new SortCommand(SortField.DATEOFBIRTH, SortDirection.ASCENDING);
        new SortCommand(SortField.DATEOFBIRTH, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(GEORGE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDateOfBirthDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH + " in descending order";
        SortCommand command = new SortCommand(SortField.DATEOFBIRTH, SortDirection.DESCENDING);
        new SortCommand(SortField.DATEOFBIRTH, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByMaritalStatusAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.MARITALSTATUS + " in ascending order";
        SortCommand command = new SortCommand(SortField.MARITALSTATUS, SortDirection.ASCENDING);
        new SortCommand(SortField.MARITALSTATUS, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(DANIEL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByMaritalStatusDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.MARITALSTATUS + " in descending order";
        SortCommand command = new SortCommand(SortField.MARITALSTATUS, SortDirection.DESCENDING);
        new SortCommand(SortField.MARITALSTATUS, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByOccupationAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION + " in ascending order";
        SortCommand command = new SortCommand(SortField.OCCUPATION, SortDirection.ASCENDING);
        new SortCommand(SortField.OCCUPATION, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(CARL, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByOccupationDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION + " in descending order";
        SortCommand command = new SortCommand(SortField.OCCUPATION, SortDirection.DESCENDING);
        new SortCommand(SortField.OCCUPATION, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(BENSON, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDependentAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENTS + " in ascending order";
        SortCommand command = new SortCommand(SortField.DEPENDENTS, SortDirection.ASCENDING);
        new SortCommand(SortField.DEPENDENTS, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_sortByDependentDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENTS + " in descending order";
        SortCommand command = new SortCommand(SortField.DEPENDENTS, SortDirection.DESCENDING);
        new SortCommand(SortField.DEPENDENTS, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(FIONA, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_emptyAddressBook_success() {
        Model emptyModel = new ModelManager();
        Model expectedEmptyModel = new ModelManager();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.NAME + " in ascending order";
        SortCommand command = new SortCommand(SortField.NAME, SortDirection.ASCENDING);

        assertCommandSuccess(command, emptyModel, expectedMessage, expectedEmptyModel);
        assertEquals(0, emptyModel.getFilteredPersonList().size());
    }

    @Test
    public void execute_sortByInsurancePackageAscending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.INSURANCEPACKAGE + " in ascending order";
        SortCommand command = new SortCommand(SortField.INSURANCEPACKAGE, SortDirection.ASCENDING);
        new SortCommand(SortField.INSURANCEPACKAGE, SortDirection.ASCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(CARL, model.getFilteredPersonList().get(0));
        assertEquals(FIONA, model.getFilteredPersonList().get(1));
    }

    @Test
    public void execute_sortByInsurancePackageDescending_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.INSURANCEPACKAGE + " in descending order";
        SortCommand command = new SortCommand(SortField.INSURANCEPACKAGE, SortDirection.DESCENDING);
        new SortCommand(SortField.INSURANCEPACKAGE, SortDirection.DESCENDING).execute(expectedModel);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(GEORGE, model.getFilteredPersonList().get(0));
    }

    //Tests for test model with unspecified persons

    @Test
    public void execute_sortBySalaryAscending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.SALARY + " in ascending order";
        SortCommand command = new SortCommand(SortField.SALARY, SortDirection.ASCENDING);
        new SortCommand(SortField.SALARY, SortDirection.ASCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(HARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortBySalaryDescending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.SALARY + " in descending order";
        SortCommand command = new SortCommand(SortField.SALARY, SortDirection.DESCENDING);
        new SortCommand(SortField.SALARY, SortDirection.DESCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(HARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByOccupationAscending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION + " in ascending order";
        SortCommand command = new SortCommand(SortField.OCCUPATION, SortDirection.ASCENDING);
        new SortCommand(SortField.OCCUPATION, SortDirection.ASCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByOccupationDescending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.OCCUPATION + " in descending order";
        SortCommand command = new SortCommand(SortField.OCCUPATION, SortDirection.DESCENDING);
        new SortCommand(SortField.OCCUPATION, SortDirection.DESCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByDateOfBirthAscending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH + " in ascending order";
        SortCommand command = new SortCommand(SortField.DATEOFBIRTH, SortDirection.ASCENDING);
        new SortCommand(SortField.DATEOFBIRTH, SortDirection.ASCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByDateOfBirthDescending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DATEOFBIRTH + " in descending order";
        SortCommand command = new SortCommand(SortField.DATEOFBIRTH, SortDirection.DESCENDING);
        new SortCommand(SortField.DATEOFBIRTH, SortDirection.DESCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByDependentAscending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENTS + " in ascending order";
        SortCommand command = new SortCommand(SortField.DEPENDENTS, SortDirection.ASCENDING);
        new SortCommand(SortField.DEPENDENTS, SortDirection.ASCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void execute_sortByDependentDescending_unspecifiedAtBottom() {
        Model testModel = createModelWithUnspecifiedPersons();
        Model expectedModel = createModelWithUnspecifiedPersons();

        String expectedMessage = SortCommand.MESSAGE_SUCCESS + SortField.DEPENDENTS + " in descending order";
        SortCommand command = new SortCommand(SortField.DEPENDENTS, SortDirection.DESCENDING);
        new SortCommand(SortField.DEPENDENTS, SortDirection.DESCENDING).execute(expectedModel);

        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        List<Person> sortedList = testModel.getFilteredPersonList();
        assertEquals(MARRY, sortedList.get(sortedList.size() - 1));
    }

    @Test
    public void equals() {
        SortCommand sortNameCommand = new SortCommand(SortField.NAME, SortDirection.ASCENDING);
        SortCommand sortPhoneCommand = new SortCommand(SortField.PHONE, SortDirection.ASCENDING);

        // same object -> returns true
        assertTrue(sortNameCommand.equals(sortNameCommand));

        // same values -> returns true
        SortCommand commandWithSameSortFieldValues = new SortCommand(SortField.NAME, SortDirection.ASCENDING);
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
        SortCommand sortCommand = new SortCommand(SortField.NAME, SortDirection.ASCENDING);
        String expected = SortCommand.class.getCanonicalName() + "{sortField=" + SortField.NAME
                + ", sortDirection=" + SortDirection.ASCENDING + "}";
        assertEquals(expected, sortCommand.toString());
    }
}
