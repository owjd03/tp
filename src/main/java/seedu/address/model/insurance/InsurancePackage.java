package seedu.address.model.insurance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an insurance package that a Person can be assigned to.
 * Guarantees: immutable; must be one of the predefined constants in {@link InsurancePackageEnum}.
 */
public class InsurancePackage {

    public static final String MESSAGE_CONSTRAINTS =
            "Insurance package must be one of the predefined constants: "
                    + "Gold, Silver, Bronze, Undecided.";

    public final String value;

    /**
     * Constructs a {@code InsurancePackage}.
     *
     * @param insurancePackage A valid insurance package.
     */
    public InsurancePackage(String insurancePackage) {
        requireNonNull(insurancePackage);
        checkArgument(InsurancePackageEnum.isValidInsurancePackage(insurancePackage), MESSAGE_CONSTRAINTS);
        value = String.valueOf(InsurancePackageEnum.fromString(insurancePackage));
    }


    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.model.insurance.InsurancePackage)) {
            return false;
        }

        seedu.address.model.insurance.InsurancePackage otherInsurancePackage =
                (seedu.address.model.insurance.InsurancePackage) other;
        return value.equals(otherInsurancePackage.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
