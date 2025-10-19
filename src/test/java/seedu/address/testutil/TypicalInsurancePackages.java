package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.InsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;

/**
 * A utility class containing a list of {@code InsurancePackage} objects to be used in tests.
 */
public class TypicalInsurancePackages {
    public static final InsurancePackage GOLD =
            new InsurancePackageBuilder()
                    .withName("Gold")
                    .withDescription("This is the premium deluxe insurance package. Buy this and worry no more!")
                    .build();
    public static final InsurancePackage SILVER =
            new InsurancePackageBuilder()
                    .withName("Silver")
                    .withDescription("This is the premium package. Buy this and worry less about life!")
                    .build();
    public static final InsurancePackage BRONZE =
            new InsurancePackageBuilder()
                    .withName("Bronze")
                    .withDescription("This is the basic insurance package. Buy this and continue worrying about life!")
                    .build();
    public static final InsurancePackage UNDECIDED =
            new InsurancePackageBuilder()
                    .withName("Undecided")
                    .withDescription("No package selected. Keep an eye open at all times, even when sleeping!")
                    .build();

    private TypicalInsurancePackages() {} // prevents instantiation

    public static List<InsurancePackage> getTypicalInsurancePackages() {
        return new ArrayList<InsurancePackage>(Arrays.asList(GOLD, SILVER, BRONZE, UNDECIDED));
    }

    public static InsuranceCatalog getTypicalInsuranceCatalog() {
        InsuranceCatalog insuranceCatalog = new InsuranceCatalog();
        for (InsurancePackage pkg : getTypicalInsurancePackages()) {
            insuranceCatalog.addInsurancePackage(pkg);
        }
        return insuranceCatalog;
    }
}
