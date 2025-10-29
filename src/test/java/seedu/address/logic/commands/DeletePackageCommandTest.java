package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;
import seedu.address.testutil.InsurancePackageBuilder;
import seedu.address.testutil.PersonBuilder;

public class DeletePackageCommandTest {

    @Test
    public void constructor_nullPackageName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePackageCommand(null));
    }

    @Test
    public void execute_packageAcceptedByModel_deleteSuccessful() throws Exception {
        InsurancePackage packageToDelete = new InsurancePackageBuilder().withName("Custom").build();
        ModelStubAcceptingPackageDeleted modelStub = new ModelStubAcceptingPackageDeleted(packageToDelete);

        CommandResult commandResult = new DeletePackageCommand("Custom").execute(modelStub);

        assertEquals(String.format(DeletePackageCommand.MESSAGE_DELETE_PACKAGE_SUCCESS, "Custom"),
                commandResult.getFeedbackToUser());
        assertEquals(Collections.singletonList(packageToDelete), modelStub.packagesDeleted);
    }

    @Test
    public void execute_nonExistentPackage_throwsCommandException() {
        DeletePackageCommand deletePackageCommand = new DeletePackageCommand("NonExistent");
        ModelStub modelStub = new ModelStubWithInsurancePackage(new InsurancePackageBuilder().build());

        assertThrows(CommandException.class,
                DeletePackageCommand.MESSAGE_PACKAGE_NOT_FOUND, () -> deletePackageCommand.execute(modelStub));
    }

    @Test
    public void execute_standardPackage_throwsCommandException() {
        DeletePackageCommand deleteUndecidedCommand = new DeletePackageCommand("Undecided");
        ModelStub modelStub = new ModelStub(); // Base ModelStub is sufficient as check is before model interaction

        assertThrows(CommandException.class,
                DeletePackageCommand.MESSAGE_CANNOT_DELETE_STANDARD_PACKAGE, () ->
                        deleteUndecidedCommand.execute(modelStub));
    }

    @Test
    public void execute_packageInUse_throwsCommandException() {
        InsurancePackage packageInUse = new InsurancePackageBuilder().withName("InUse").withDescription("Desc").build();
        Person personWithPackage = new PersonBuilder().withName("Alice")
                .withInsurancePackage("InUse", "Desc").build();
        ModelStub modelStub = new ModelStubWithPackageInUse(packageInUse, personWithPackage);
        DeletePackageCommand deletePackageCommand = new DeletePackageCommand("InUse");

        assertThrows(CommandException.class,
                DeletePackageCommand.MESSAGE_PACKAGE_IN_USE, () -> deletePackageCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        DeletePackageCommand deleteFirstCommand = new DeletePackageCommand("first");
        DeletePackageCommand deleteSecondCommand = new DeletePackageCommand("second");

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        DeletePackageCommand deleteFirstCommandCopy = new DeletePackageCommand("first");
        assertEquals(deleteFirstCommand, deleteFirstCommandCopy);

        // null -> returns false
        assertNotEquals(null, deleteFirstCommand);

        // different package -> returns false
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);
    }

    @Test
    public void toStringMethod() {
        DeletePackageCommand deletePackageCommand = new DeletePackageCommand("MyPackage");
        String expected = DeletePackageCommand.class.getCanonicalName() + "{packageName=MyPackage}";
        assertEquals(expected, deletePackageCommand.toString());
    }

    /**
     * A default model stub that has all methods failing.
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
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getInsuranceCatalogFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsuranceCatalogFilePath(Path insuranceCatalogFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyInsuranceCatalog getInsuranceCatalog() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteInsurancePackage(InsurancePackage target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addInsurancePackage(InsurancePackage insurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortInsurancePackageList(Comparator<InsurancePackage> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredInsurancePackageList(Predicate<InsurancePackage> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single insurance package.
     */
    private static class ModelStubWithInsurancePackage extends ModelStub {
        private final InsurancePackage insurancePackage;

        ModelStubWithInsurancePackage(InsurancePackage insurancePackage) {
            this.insurancePackage = insurancePackage;
        }

        @Override
        public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
            return FXCollections.observableArrayList(insurancePackage);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new seedu.address.model.AddressBook();
        }
    }

    /**
     * A Model stub that contains a package in use by a person.
     */
    private static class ModelStubWithPackageInUse extends ModelStub {
        private final InsurancePackage insurancePackage;
        private final Person person;

        ModelStubWithPackageInUse(InsurancePackage insurancePackage, Person person) {
            this.insurancePackage = insurancePackage;
            this.person = person;
        }

        @Override
        public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
            return FXCollections.observableArrayList(insurancePackage);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            seedu.address.model.AddressBook ab = new seedu.address.model.AddressBook();
            ab.addPerson(person);
            return ab;
        }
    }

    /**
     * A Model stub that always accepts the package being deleted.
     */
    private static class ModelStubAcceptingPackageDeleted extends ModelStub {
        final ArrayList<InsurancePackage> packagesDeleted = new ArrayList<>();
        private final InsurancePackage packageToDelete;

        ModelStubAcceptingPackageDeleted(InsurancePackage packageToDelete) {
            this.packageToDelete = packageToDelete;
        }

        @Override
        public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
            return FXCollections.observableArrayList(packageToDelete);
        }

        @Override
        public void deleteInsurancePackage(InsurancePackage target) {
            packagesDeleted.add(target);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new seedu.address.model.AddressBook();
        }
    }
}
