package seedu.address.testutil;

import seedu.address.model.InsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;

/**
 * A utility class to help with building InsuranceCatalog objects.
 */
public class InsuranceCatalogBuilder {

    private InsuranceCatalog insuranceCatalog;

    public InsuranceCatalogBuilder() {
        this.insuranceCatalog = new InsuranceCatalog();
    }

    public InsuranceCatalogBuilder(InsuranceCatalog insuranceCatalog) {
        this.insuranceCatalog = insuranceCatalog;
    }

    /**
     * Adds a new {@code InsurancePackage} to the {@code InsuranceCatalog} that we are building.
     */
    public InsuranceCatalogBuilder withInsurancePackage(InsurancePackage insurancePackage) {
        this.insuranceCatalog.addInsurancePackage(insurancePackage);
        return this;
    }

    /**
     * Builds the insurance catalog.
     * @return The built insurance catalog.
     */
    public InsuranceCatalog build() {
        return this.insuranceCatalog;
    }
}
