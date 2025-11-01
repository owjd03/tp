package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.Storage;

/**
 * Tests the initialisation logic in MainApp,
 * and how it handles missing or corrupt data files.
 */
public class MainAppTest {

    private InMemoryStorageStub storage;
    private MainApp mainApp;
    private ReadOnlyAddressBook sampleAddressBook;
    private ReadOnlyInsuranceCatalog sampleCatalog;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryStorageStub();
        mainApp = new MainApp();
        sampleAddressBook = SampleDataUtil.getSampleAddressBook();
        sampleCatalog = SampleDataUtil.getSampleInsuranceCatalog();
    }

    /**
     * Tests that {@code initInsuranceCatalog} returns a sample catalog and attempts to save it
     * when the storage returns an empty optional,
     * simulating a missing insurance catalog file.
     */
    @Test
    public void initInsuranceCatalog_missingCatalog_createsAndSavesSample() {
        storage.setCatalogToReturn(Optional.empty());

        ReadOnlyInsuranceCatalog result = mainApp.initInsuranceCatalog(storage);

        assertEquals(sampleCatalog, result);
        assertTrue(storage.isSaveInsuranceCatalogCalled());
    }

    /**
     * Tests that {@code initInsuranceCatalog} returns a sample catalog and attempts to save it
     * when the storage throws a {@code DataLoadingException},
     * simulating a corrupt insurance catalog file.
     */
    @Test
    public void initInsuranceCatalog_corruptCatalog_createsAndSavesSample() {
        storage.setThrowCatalogException(true);

        ReadOnlyInsuranceCatalog result = mainApp.initInsuranceCatalog(storage);

        assertEquals(sampleCatalog, result);
        assertTrue(storage.isSaveInsuranceCatalogCalled());
    }

    /**
     * Tests that {@code createAndSaveSampleInsuranceCatalog} still returns the sample catalog
     * even when the storage throws an {@code IOException} during save,
     * simulating a disk-full error.
     */
    @Test
    public void createAndSaveSampleInsuranceCatalog_saveFails_stillReturnsSampleCatalog() {
        storage.setThrowSaveCatalogException(true);

        ReadOnlyInsuranceCatalog result = mainApp.createAndSaveSampleInsuranceCatalog(storage);

        assertEquals(sampleCatalog, result);
    }

    /**
     * Tests that {@code initAddressBook} returns a sample address book
     * when the storage returns an empty optional,
     * simulating a missing address book file.
     */
    @Test
    public void initAddressBook_missingAddressBook_returnsSampleAddressBook() {
        storage.setAddressBookToReturn(Optional.empty());

        ReadOnlyAddressBook result = mainApp.initAddressBook(storage, sampleCatalog);

        assertEquals(sampleAddressBook, result);
    }

    /**
     * Tests that {@code initAddressBook} returns an empty address book
     * when the storage throws a {@code DataLoadingException},
     * simulating a corrupt address book file.
     */
    @Test
    public void initAddressBook_corruptAddressBook_returnsEmptyAddressBook() {
        storage.setThrowAddressBookException(true);

        ReadOnlyAddressBook result = mainApp.initAddressBook(storage, sampleCatalog);

        assertNotNull(result);
        assertEquals(new AddressBook(), result);
    }


    /**
     * A stub implementation of {@code Storage}
     */
    private static class InMemoryStorageStub implements Storage {

        private Optional<ReadOnlyInsuranceCatalog> catalogToReturn = Optional.empty();
        private Optional<ReadOnlyAddressBook> addressBookToReturn = Optional.empty();
        private boolean throwCatalogException = false;
        private boolean throwAddressBookException = false;
        private boolean throwSaveCatalogException = false;
        private boolean saveInsuranceCatalogCalled = false;

        public void setCatalogToReturn(Optional<ReadOnlyInsuranceCatalog> catalog) {
            this.catalogToReturn = catalog;
        }

        public void setAddressBookToReturn(Optional<ReadOnlyAddressBook> ab) {
            this.addressBookToReturn = ab;
        }

        public void setThrowCatalogException(boolean shouldThrow) {
            this.throwCatalogException = shouldThrow;
        }

        public void setThrowAddressBookException(boolean shouldThrow) {
            this.throwAddressBookException = shouldThrow;
        }

        public void setThrowSaveCatalogException(boolean shouldThrow) {
            this.throwSaveCatalogException = shouldThrow;
        }

        public boolean isSaveInsuranceCatalogCalled() {
            return saveInsuranceCatalogCalled;
        }

        // Implementation of Storage interface

        @Override
        public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog() throws DataLoadingException {
            if (throwCatalogException) {
                throw new DataLoadingException(new Exception("Simulated catalog loading error"));
            }
            return catalogToReturn;
        }

        @Override
        public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog(Path filePath) throws DataLoadingException {
            return readInsuranceCatalog();
        }

        @Override
        public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) throws IOException {
            saveInsuranceCatalogCalled = true;
            if (throwSaveCatalogException) {
                throw new IOException("Simulated catalog saving error");
            }
        }

        @Override
        public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog, Path filePath) throws IOException {
            saveInsuranceCatalog(insuranceCatalog);
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(ReadOnlyInsuranceCatalog catalog)
                throws DataLoadingException {
            if (throwAddressBookException) {
                throw new DataLoadingException(new Exception("Simulated address book loading error"));
            }
            return addressBookToReturn;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath, ReadOnlyInsuranceCatalog catalog)
                throws DataLoadingException {
            return readAddressBook(catalog);
        }

        // Other unused Storage methods

        @Override
        public Path getAddressBookFilePath() {
            return Paths.get("dummy/path/addressbook.json");
        }

        @Override
        public Path getInsuranceCatalogFilePath() {
            return Paths.get("dummy/path/insurancecatalog.json");
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getUserPrefsFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
