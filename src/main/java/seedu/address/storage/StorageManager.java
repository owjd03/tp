package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook and InsuranceCatalog data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private InsuranceCatalogStorage insuranceCatalogStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code AddressBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(AddressBookStorage addressBookStorage,
                          InsuranceCatalogStorage insuranceCatalogStorage,
                          UserPrefsStorage userPrefsStorage) {
        this.addressBookStorage = addressBookStorage;
        this.insuranceCatalogStorage = insuranceCatalogStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath, ReadOnlyInsuranceCatalog catalog)
            throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);

        return addressBookStorage.readAddressBook(filePath, catalog);
    }
    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(ReadOnlyInsuranceCatalog catalog) throws DataLoadingException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath(), catalog);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    // ================ InsuranceCatalog methods ===========================
    @Override
    public Path getInsuranceCatalogFilePath() {
        return this.insuranceCatalogStorage.getInsuranceCatalogFilePath();
    }

    @Override
    public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog() throws DataLoadingException {
        return readInsuranceCatalog(this.insuranceCatalogStorage.getInsuranceCatalogFilePath());
    }

    @Override
    public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return this.insuranceCatalogStorage.readInsuranceCatalog(filePath);
    }

    @Override
    public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) throws IOException {
        saveInsuranceCatalog(insuranceCatalog, this.insuranceCatalogStorage.getInsuranceCatalogFilePath());
    }

    @Override
    public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        this.insuranceCatalogStorage.saveInsuranceCatalog(insuranceCatalog, filePath);
    }
}
