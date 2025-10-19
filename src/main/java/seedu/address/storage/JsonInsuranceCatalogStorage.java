package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyInsuranceCatalog;

/**
 * A class to access InsuranceCatalog data stored as a json file on the hard disk.
 */
public class JsonInsuranceCatalogStorage implements InsuranceCatalogStorage {
    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);

    private Path filePath;

    public JsonInsuranceCatalogStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getInsuranceCatalogFilePath() {
        return this.filePath;
    }

    @Override
    public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog() throws DataLoadingException {
        return readInsuranceCatalog(this.filePath);
    }

    /**
     * Similar to {@link #readInsuranceCatalog()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    @Override
    public Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableInsuranceCatalog> jsonInsuranceCatalog = JsonUtil.readJsonFile(
                filePath, JsonSerializableInsuranceCatalog.class);
        if (!jsonInsuranceCatalog.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonInsuranceCatalog.get().toModelType());
        } catch (IllegalValueException e) {
            logger.info("Illegal values found in " + filePath + ": " + e.getMessage());
            throw new DataLoadingException(e);
        }
    }

    @Override
    public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) throws IOException {
        saveInsuranceCatalog(insuranceCatalog, this.filePath);
    }

    @Override
    public void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog, Path filePath) throws IOException {
        requireNonNull(insuranceCatalog);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableInsuranceCatalog(insuranceCatalog), filePath);
    }
}
