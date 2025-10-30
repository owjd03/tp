package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;
import seedu.address.testutil.InsurancePackageBuilder;

public class EditPackageCommandTest {

    private static final Logger logger = LogsCenter.getLogger(EditPackageCommandTest.class);

    private static final String NEW_PACKAGE_DESCRIPTION = "New description";
    private static final String OLD_PACKAGE_DESCRIPTION = "Old description";
    private static final String PACKAGE_NAME = "Gold";

    @Test
    public void constructor_nullInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditPackageCommand(null, ""));
        assertThrows(NullPointerException.class, () -> new EditPackageCommand(PACKAGE_NAME, null));
    }

    @Test
    public void execute_packageFound_editSuccessful() throws Exception {
        InsurancePackage originalPackage = new InsurancePackageBuilder().withName(PACKAGE_NAME)
                .withDescription(OLD_PACKAGE_DESCRIPTION).build();
        logger.info("Created originalPackage: " + originalPackage);

        ModelStubWithPackage modelStub = new ModelStubWithPackage(originalPackage);

        InsurancePackage editedPackage = new InsurancePackageBuilder().withName(PACKAGE_NAME)
                .withDescription(NEW_PACKAGE_DESCRIPTION).build();

        CommandResult commandResult = new EditPackageCommand(PACKAGE_NAME, NEW_PACKAGE_DESCRIPTION).execute(modelStub);
        logger.info("Command executed. Result: " + commandResult.getFeedbackToUser());

        assertEquals(String.format(EditPackageCommand.MESSAGE_EDIT_PACKAGE_SUCCESS, Messages.format(editedPackage)),
                commandResult.getFeedbackToUser());

        assertEquals(originalPackage, modelStub.targetPackage);
        assertEquals(editedPackage, modelStub.editedInsurancePackage);
    }

    @Test
    public void equals() {
        EditPackageCommand editGoldPackage = new EditPackageCommand("Gold", "Desc1");
        EditPackageCommand editSilverPackage = new EditPackageCommand("Silver", "Desc2");

        // same object -> returns true
        assertTrue(editGoldPackage.equals(editGoldPackage));

        // same name and same description -> returns true
        EditPackageCommand editGoldPackageSameDesc = new EditPackageCommand("Gold", "Desc1");
        assertTrue(editGoldPackage.equals(editGoldPackageSameDesc));

        // same name but different description -> returns false
        EditPackageCommand editGoldPackageDiffDesc = new EditPackageCommand("Gold", "Desc3");
        assertFalse(editGoldPackage.equals(editGoldPackageDiffDesc));

        // different insurance package -> returns false
        assertFalse(editGoldPackage.equals(editSilverPackage));
    }

    @Test
    public void toStringMethod() {
        EditPackageCommand editPackageCommand = new EditPackageCommand(PACKAGE_NAME, NEW_PACKAGE_DESCRIPTION);
        String expected = EditPackageCommand.class.getCanonicalName()
                + "{packageName=" + PACKAGE_NAME
                + ", newDescription=" + NEW_PACKAGE_DESCRIPTION + "}";
        assertEquals(expected, editPackageCommand.toString());
    }

    /**
     * A default model stub that have all the methods failing.
     */
    private static class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getInsuranceCatalogFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsuranceCatalogFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addInsurancePackage(InsurancePackage insurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsuranceCatalog(ReadOnlyInsuranceCatalog newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyInsuranceCatalog getInsuranceCatalog() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteInsurancePackage(InsurancePackage target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredInsurancePackageList(Predicate<InsurancePackage> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortInsurancePackageList(Comparator<InsurancePackage> comparator) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single insurance package for editing.
     */
    private class ModelStubWithPackage extends ModelStub {
        private final InsurancePackage originalPackage;
        private InsurancePackage targetPackage;
        private InsurancePackage editedInsurancePackage;

        ModelStubWithPackage(InsurancePackage insurancePackage) {
            requireNonNull(insurancePackage);
            this.originalPackage = insurancePackage;
        }

        @Override
        public void setInsurancePackage(InsurancePackage target, InsurancePackage edited) {
            requireNonNull(target);
            requireNonNull(edited);
            this.targetPackage = target;
            this.editedInsurancePackage = edited;
        }

        @Override
        public ReadOnlyInsuranceCatalog getInsuranceCatalog() {
            InsuranceCatalog catalog = new InsuranceCatalog();
            catalog.addInsurancePackage(originalPackage);
            return catalog;
        }
    }
}
