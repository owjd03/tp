package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalInsurancePackages.GOLD;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;

public class JsonInsuranceCatalogStorageTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonInsuranceCatalogStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readInsuranceCatalog_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readInsuranceCatalog(null));
    }

    private java.util.Optional<ReadOnlyInsuranceCatalog> readInsuranceCatalog(String filePath) throws Exception {
        return new JsonInsuranceCatalogStorage(Paths.get(filePath))
                .readInsuranceCatalog(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readInsuranceCatalog("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readInsuranceCatalog("notJsonFormatInsuranceCatalog.json"));
    }

    @Test
    public void readInsuranceCatalog_invalidInsuranceCatalog_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readInsuranceCatalog("invalidInsuranceCatalog.json"));
    }

    @Test
    public void readInsuranceCatalog_invalidAndValidInsuranceCatalog_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () ->
                readInsuranceCatalog("invalidAndValidInsuranceCatalog.json"));
    }

    @Test
    public void readAndSaveInsuranceCatalog_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempInsuranceCatalog.json");
        InsuranceCatalog original = getTypicalInsuranceCatalog();
        JsonInsuranceCatalogStorage jsonInsuranceCatalogStorage = new JsonInsuranceCatalogStorage(filePath);

        // Define new packages just for this test
        InsurancePackage Platinum = new InsurancePackage("Platinum", "A new package");
        InsurancePackage Diamond = new InsurancePackage("Diamond", "Another new package");

        // Save in new file and read back
        jsonInsuranceCatalogStorage.saveInsuranceCatalog(original, filePath);
        ReadOnlyInsuranceCatalog readBack = jsonInsuranceCatalogStorage.readInsuranceCatalog(filePath).get();
        assertEquals(original, new InsuranceCatalog(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addInsurancePackage(Platinum);
        original.removeInsurancePackage(GOLD);
        jsonInsuranceCatalogStorage.saveInsuranceCatalog(original, filePath);
        readBack = jsonInsuranceCatalogStorage.readInsuranceCatalog(filePath).get();
        assertEquals(original, new InsuranceCatalog(readBack));

        // Save and read without specifying file path
        original.addInsurancePackage(Diamond);
        jsonInsuranceCatalogStorage.saveInsuranceCatalog(original); // file path not specified
        readBack = jsonInsuranceCatalogStorage.readInsuranceCatalog().get(); // file path not specified
        assertEquals(original, new InsuranceCatalog(readBack));
    }

    @Test
    public void saveInsuranceCatalog_nullCatalog_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveInsuranceCatalog(null, "SomeFile.json"));
    }

    /**
     * Saves {@code insuranceCatalog} at the specified {@code filePath}.
     */
    private void saveInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog, String filePath) throws IOException {
        new JsonInsuranceCatalogStorage(Paths.get(filePath))
                .saveInsuranceCatalog(insuranceCatalog, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveInsuranceCatalog_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveInsuranceCatalog(new InsuranceCatalog(), null));
    }
}
