package seedu.address.testutil;

import seedu.address.model.insurance.InsurancePackage;

/**
 * A utility class to help with building InsurancePackage objects.
 */
public class InsurancePackageBuilder {

    public static final String DEFAULT_PACKAGE_NAME = "Gold";

    private String packageName;

    /**
     * Creates a {@code InsurancePackageBuilder} with the default details.
     */
    public InsurancePackageBuilder() {
        packageName = DEFAULT_PACKAGE_NAME;
    }

    /**
     * Initializes the InsurancePackageBuilder with the data of {@code insurancePackageToCopy}.
     */
    public InsurancePackageBuilder(InsurancePackage insurancePackageToCopy) {
        packageName = insurancePackageToCopy.packageName;
    }

    /**
     * Sets the {@code Name} of the {@code InsurancePackage} that we are building.
     */
    public InsurancePackageBuilder withName(String name) {
        this.packageName = name;
        return this;
    }

    public InsurancePackage build() {
        return new InsurancePackage(packageName);
    }
}
