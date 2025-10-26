package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;

public class StorageManagerTest {
    private static final ReadOnlyInsuranceCatalog VALID_CATALOG = SampleDataUtil.getSampleInsuranceCatalog();

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    /**
     * Populates the static lists in InsuranceCatalog before any tests are run.
     * This mimics the application loading sample data and prevents validation errors.
     */
    @BeforeAll
    public static void setupInsuranceCatalog() {
        new InsuranceCatalog(SampleDataUtil.getSampleInsuranceCatalog());
    }

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonInsuranceCatalogStorage insuranceCatalogStorage =
                new JsonInsuranceCatalogStorage(getTempFilePath("ic"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, insuranceCatalogStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook(VALID_CATALOG).get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void insuranceCatalogReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonInsuranceCatalogStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonInsuranceCatalogStorageTest} class.
         */
        InsuranceCatalog original = getTypicalInsuranceCatalog();
        storageManager.saveInsuranceCatalog(original);
        ReadOnlyInsuranceCatalog retrieved = storageManager.readInsuranceCatalog().get();
        assertEquals(original, new InsuranceCatalog(retrieved));
    }

    @Test
    public void getInsuranceCatalogFilePath() {
        assertNotNull(storageManager.getInsuranceCatalogFilePath());
    }
}
