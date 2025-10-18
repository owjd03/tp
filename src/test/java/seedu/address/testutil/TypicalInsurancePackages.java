package seedu.address.testutil;

import seedu.address.model.insurance.InsurancePackage;

/**
 * A utility class containing a list of {@code InsurancePackage} objects to be used in tests.
 */
public class TypicalInsurancePackages {
    public static final InsurancePackage GOLD = new InsurancePackageBuilder().withName("Gold").build();
    public static final InsurancePackage SILVER = new InsurancePackageBuilder().withName("Silver").build();
    public static final InsurancePackage BRONZE = new InsurancePackageBuilder().withName("Bronze").build();
    public static final InsurancePackage UNDECIDED = new InsurancePackageBuilder().withName("Undecided").build();

    private TypicalInsurancePackages() {} // prevents instantiation
}
