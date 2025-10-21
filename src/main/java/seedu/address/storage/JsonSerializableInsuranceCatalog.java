package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.ReadOnlyInsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.insurance.exceptions.DuplicateInsurancePackageException;

/**
 * An Immutable InsuranceCatalog that is serializable to JSON format.
 */
@JsonRootName(value = "insurancecatalog")
public class JsonSerializableInsuranceCatalog {

    public static final String MESSAGE_DUPLICATE_PACKAGE = "Insurance package list contains duplicate package(s).";

    private final List<JsonAdaptedInsurancePackage> insurancePackages = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableInsuranceCatalog} with the given insurance packages.
     */
    @JsonCreator
    public JsonSerializableInsuranceCatalog(
            @JsonProperty("insurancePackages") List<JsonAdaptedInsurancePackage> insurancePackages) {
        this.insurancePackages.addAll(insurancePackages);
    }

    /**
     * Converts a given {@code ReadOnlyInsuranceCatalog} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableInsuranceCatalog}.
     */
    public JsonSerializableInsuranceCatalog(ReadOnlyInsuranceCatalog source) {
        this.insurancePackages.addAll(source.getInsurancePackageList()
                .stream()
                .map(JsonAdaptedInsurancePackage::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this insurance catalog into the model's {@code InsuranceCatalog} object.
     *
     * @throws IllegalValueException If there are any data constraints violated.
     */
    public InsuranceCatalog toModelType() throws IllegalValueException {
        InsuranceCatalog insuranceCatalog = new InsuranceCatalog();
        List<InsurancePackage> modelInsurancePackages = new ArrayList<>();
        for (JsonAdaptedInsurancePackage jsonAdaptedInsurancePackage : this.insurancePackages) {
            InsurancePackage insurancePackage = jsonAdaptedInsurancePackage.toModelType();
            modelInsurancePackages.add(insurancePackage);
        }
        try {
            insuranceCatalog.setInsurancePackages(modelInsurancePackages);
        } catch (DuplicateInsurancePackageException e) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_PACKAGE, e);
        }
        return insuranceCatalog;
    }
}
