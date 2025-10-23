package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.InsuranceCatalog;
import seedu.address.testutil.TypicalInsurancePackages;

public class JsonSerializableInsuranceCatalogTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonSerializableInsuranceCatalogTest");
    private static final Path TYPICAL_INSURANCE_PACKAGES_FILE =
            TEST_DATA_FOLDER.resolve("typicalInsurancePackagesInsuranceCatalog.json");
    private static final Path INVALID_INSURANCE_PACKAGE_FILE =
            TEST_DATA_FOLDER.resolve("invalidInsurancePackageInsuranceCatalog.json");
    private static final Path DUPLICATE_INSURANCE_PACKAGE_FILE =
            TEST_DATA_FOLDER.resolve("duplicateInsurancePackageInsuranceCatalog.json");

    @Test
    public void toModelType_typicalInsurancePackagesFile_success() throws Exception {
        JsonSerializableInsuranceCatalog dataFromFile = JsonUtil.readJsonFile(TYPICAL_INSURANCE_PACKAGES_FILE,
                JsonSerializableInsuranceCatalog.class).get();
        InsuranceCatalog insuranceCatalogFromFile = dataFromFile.toModelType();
        InsuranceCatalog typicalInsuranceCatalog =
                TypicalInsurancePackages.getTypicalInsuranceCatalog();
        assertEquals(insuranceCatalogFromFile, typicalInsuranceCatalog);
    }

    @Test
    public void toModelType_invalidSInsurancePackageFile_throwsIllegalValueException() throws Exception {
        JsonSerializableInsuranceCatalog dataFromFile = JsonUtil.readJsonFile(INVALID_INSURANCE_PACKAGE_FILE,
                JsonSerializableInsuranceCatalog.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateInsurancePackage_throwsIllegalValueException() throws Exception {
        JsonSerializableInsuranceCatalog dataFromFile = JsonUtil.readJsonFile(DUPLICATE_INSURANCE_PACKAGE_FILE,
                JsonSerializableInsuranceCatalog.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableInsuranceCatalog.MESSAGE_DUPLICATE_PACKAGE,
                dataFromFile::toModelType);
    }
}
