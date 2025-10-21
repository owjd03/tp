package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalInsurancePackages.GOLD;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;
import seedu.address.testutil.InsurancePackageBuilder;

public class AddPackageCommandTest {
    @Test
    public void constructor_nullInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPackageCommand(null));
    }

    @Test
    public void execute_insurancePackageAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingInsurancePackageAdded modelStub = new ModelStubAcceptingInsurancePackageAdded();
        InsurancePackage validInsurancePackage = new InsurancePackageBuilder().build();

        CommandResult commandResult = new AddPackageCommand(validInsurancePackage).execute(modelStub);

        assertEquals(String.format(AddPackageCommand.MESSAGE_SUCCESS, Messages.format(validInsurancePackage)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validInsurancePackage), modelStub.insurancePackagesAdded);
    }

    @Test
    public void execute_duplicateInsurancePackage_throwsCommandException() {
        InsurancePackage validInsurancePackage = new InsurancePackageBuilder().build();
        AddPackageCommand addPackageCommand = new AddPackageCommand(validInsurancePackage);
        ModelStub modelStub = new ModelStubWithInsurancePackage(validInsurancePackage);

        assertThrows(CommandException.class, AddPackageCommand.MESSAGE_DUPLICATE_PACKAGE,
                () -> addPackageCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateInsurancePackageCaseInsensitive_throwsCommandException() {
        InsurancePackage validInsurancePackage = new InsurancePackageBuilder().withName("Gold").build();
        InsurancePackage duplicateInsurancePackage = new InsurancePackageBuilder().withName("GOLD").build();
        AddPackageCommand addPackageCommand = new AddPackageCommand(duplicateInsurancePackage);
        ModelStub modelStub = new ModelStubWithInsurancePackage(validInsurancePackage);

        assertThrows(CommandException.class, AddPackageCommand.MESSAGE_DUPLICATE_PACKAGE,
                () -> addPackageCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        InsurancePackage gold = new InsurancePackageBuilder().withName("Gold").build();
        InsurancePackage silver = new InsurancePackageBuilder().withName("Silver").build();
        AddPackageCommand addGoldCommand = new AddPackageCommand(gold);
        AddPackageCommand addSilverCommand = new AddPackageCommand(silver);

        // same object -> returns true
        assertTrue(addGoldCommand.equals(addGoldCommand));

        // same values -> returns true
        AddPackageCommand addGoldCommandCopy = new AddPackageCommand(gold);
        assertTrue(addGoldCommand.equals(addGoldCommandCopy));

        // different types -> returns false
        assertFalse(addGoldCommand.equals(1));

        // null -> returns false
        assertFalse(addGoldCommand.equals(null));

        // different insurance package -> returns false
        assertFalse(addGoldCommand.equals(addSilverCommand));
    }

    @Test
    public void toStringMethod() {
        AddPackageCommand addPackageCommand = new AddPackageCommand(GOLD);
        String expected = AddPackageCommand.class.getCanonicalName() + "{toAdd=" + GOLD + "}";
        assertEquals(expected, addPackageCommand.toString());
    }

    /**
     * A default model stub that have all the methods failing.
     */
    private class ModelStub implements Model {
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
     * A Model stub that contains a single insurance package.
     */
    private class ModelStubWithInsurancePackage extends ModelStub {
        private final InsurancePackage insurancePackage;

        ModelStubWithInsurancePackage(InsurancePackage insurancePackage) {
            requireNonNull(insurancePackage);
            this.insurancePackage = insurancePackage;
        }

        @Override
        public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
            requireNonNull(insurancePackage);
            return this.insurancePackage.isSameInsurancePackage(insurancePackage);
        }
    }

    /**
     * A Model stub that always accept the insurance package being added.
     */
    private class ModelStubAcceptingInsurancePackageAdded extends ModelStub {
        final ArrayList<InsurancePackage> insurancePackagesAdded = new ArrayList<>();

        @Override
        public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
            requireNonNull(insurancePackage);
            return insurancePackagesAdded.stream().anyMatch(insurancePackage::isSameInsurancePackage);
        }

        @Override
        public void addInsurancePackage(InsurancePackage insurancePackage) {
            requireNonNull(insurancePackage);
            insurancePackagesAdded.add(insurancePackage);
        }

        @Override
        public ReadOnlyInsuranceCatalog getInsuranceCatalog() {
            return new InsuranceCatalog();
        }
    }
}
