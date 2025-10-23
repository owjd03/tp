package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyInsuranceCatalog;

/**
 * Represents a storage for {@link seedu.address.model.InsuranceCatalog}.
 */
public interface InsuranceCatalogStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getInsuranceCatalogFilePath();

    /**
     * Returns InsuranceCatalog data as a {@link ReadOnlyInsuranceCatalog}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog() throws DataLoadingException;

    /**
     * @see #readInsuranceCatalog()
     */
    Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyInsuranceCatalog} to the storage.
     * @param insuranceCatalog cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) throws IOException;

    /**
     * @see #saveInsuranceCatalog(ReadOnlyInsuranceCatalog)
     */
    void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog, Path filePath) throws IOException;
}
