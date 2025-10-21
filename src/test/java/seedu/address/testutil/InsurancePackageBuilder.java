package seedu.address.testutil;

import seedu.address.model.insurance.InsurancePackage;

/**
 * A utility class to help with building InsurancePackage objects.
 */
public class InsurancePackageBuilder {

    public static final String DEFAULT_PACKAGE_NAME = "Undecided";
    public static final String DEFAULT_PACKAGE_DESCRIPTION = "No insurance package selected.";

    private String packageName;
    private String packageDescription;

    /**
     * Creates a {@code InsurancePackageBuilder} with the default details.
     */
    public InsurancePackageBuilder() {
        packageName = DEFAULT_PACKAGE_NAME;
        packageDescription = DEFAULT_PACKAGE_DESCRIPTION;
    }

    /**
     * Initializes the InsurancePackageBuilder with the data of {@code insurancePackageToCopy}.
     */
    public InsurancePackageBuilder(InsurancePackage insurancePackageToCopy) {
        packageName = insurancePackageToCopy.getPackageName();
        packageDescription = insurancePackageToCopy.getPackageDescription();
    }

    /**
     * Sets the {@code Name} of the {@code InsurancePackage} that we are building.
     */
    public InsurancePackageBuilder withName(String name) {
        this.packageName = name;
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code InsurancePackage} that we are building.
     */
    public InsurancePackageBuilder withDescription(String description) {
        this.packageDescription = description;
        return this;
    }

    public InsurancePackage build() {
        return new InsurancePackage(packageName, packageDescription);
    }
}
