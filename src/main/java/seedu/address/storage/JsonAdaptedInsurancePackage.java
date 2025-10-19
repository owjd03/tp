package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.insurance.InsurancePackageEnum;

/**
 * Jackson-friendly version of {@link InsurancePackage}.
 */
public class JsonAdaptedInsurancePackage {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "InsurancePackage's %s field is missing!";

    private final String packageName;
    private final String packageDescription;

    /**
     * Constructs a {@code JsonAdaptedInsurancePackage} with the given insurance package details.
     */
    @JsonCreator
    public JsonAdaptedInsurancePackage(@JsonProperty("packageName") String packageName,
                                       @JsonProperty("packageDescription") String packageDescription) {
        this.packageName = packageName;
        this.packageDescription = packageDescription;
    }

    /**
     * Converts a given {@code InsurancePackage} into this class for Jackson use.
     */
    public JsonAdaptedInsurancePackage(InsurancePackage source) {
        this.packageName = source.packageName;
        this.packageDescription = source.packageDescription;
    }

    /**
     * Converts this Jackson-friendly adapted insurance package object into the model's {@code InsurancePackage} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted package.
     */
    public InsurancePackage toModelType() throws IllegalValueException {
        if (this.packageName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "packageName"));
        }
        if (!InsurancePackageEnum.isValidInsurancePackage(this.packageName)) {
            throw new IllegalValueException(InsurancePackage.MESSAGE_CONSTRAINTS);
        }
        final String modelPackageName = this.packageName;

        if (this.packageDescription == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "packageDescription"));
        }
        final String modelPackageDescription = this.packageDescription;

        return new InsurancePackage(modelPackageName, modelPackageDescription);
    }
}
