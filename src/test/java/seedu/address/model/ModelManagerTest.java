package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_INSURANCE_PACKAGES;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalInsurancePackages.BRONZE;
import static seedu.address.testutil.TypicalInsurancePackages.GOLD;
import static seedu.address.testutil.TypicalInsurancePackages.SILVER;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
        assertEquals(new InsuranceCatalog(), new InsuranceCatalog(modelManager.getInsuranceCatalog()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setInsuranceCatalogFilePath(Paths.get("insurance/catalog/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());

        userPrefs.setInsuranceCatalogFilePath(Paths.get("new/insurance/catalog/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setInsuranceCatalogFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setInsuranceCatalogFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void setInsuranceCatalogFilePath_validPath_setsInsuranceCatalogFilePath() {
        Path path = Paths.get("insurance/catalog/file/path");
        modelManager.setInsuranceCatalogFilePath(path);
        assertEquals(path, modelManager.getInsuranceCatalogFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasInsurancePackage_nullPackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasInsurancePackage(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasInsurancePackage_packageNotInCatalog_returnsFalse() {
        assertFalse(modelManager.hasInsurancePackage(GOLD));
        modelManager.addInsurancePackage(GOLD);
        assertFalse(modelManager.hasInsurancePackage(SILVER));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasInsurancePackage_packageInCatalog_returnsTrue() {
        modelManager.addInsurancePackage(SILVER);
        assertTrue(modelManager.hasInsurancePackage(SILVER));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredInsurancePackageList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                modelManager.getFilteredInsurancePackageList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        InsuranceCatalog insuranceCatalog = new InsuranceCatalog();
        InsuranceCatalog differentInsuranceCatalog = new InsuranceCatalog();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, insuranceCatalog, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, insuranceCatalog, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, insuranceCatalog, userPrefs)));

        // different insuranceCatalog -> returns false
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentInsuranceCatalog, userPrefs)));

        // different filteredPersonList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, insuranceCatalog, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different filteredInsurancePackageList -> returns false
        // Currently, there is no Predicate class for insurance packages
        // Thus, we can get a different filtered list by adding an extra insurance package
        ModelManager modelManagerWithExtraPackage = new ModelManager(addressBook, insuranceCatalog, userPrefs);
        modelManagerWithExtraPackage.addInsurancePackage(BRONZE);
        assertFalse(modelManager.equals(new ModelManager(addressBook, insuranceCatalog, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredInsurancePackageList(PREDICATE_SHOW_ALL_INSURANCE_PACKAGES);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, insuranceCatalog, differentUserPrefs)));
    }
}
