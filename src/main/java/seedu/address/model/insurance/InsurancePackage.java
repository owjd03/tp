package seedu.address.model.insurance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents an insurance package that a Person can be assigned to.
 * Guarantees: immutable; must be one of the predefined constants in {@link InsurancePackageEnum}.
 */
public class InsurancePackage {

    public static final String MESSAGE_CONSTRAINTS =
            "Insurance package must either one of the predefined constants: "
                    + "Gold, Silver, Bronze, Undecided, or a custom name";

    public final String packageName;
    public final String packageDescription;

    /**
     * Constructs a {@code InsurancePackage}.
     *
     * @param name             A valid insurance package, represented by its name.
     * @param description      Description of the insurance package.
     */
    public InsurancePackage(String name, String description) {
        requireNonNull(name);
        checkArgument(InsurancePackageEnum.isValidInsurancePackage(name), MESSAGE_CONSTRAINTS);
        packageName = String.valueOf(InsurancePackageEnum.fromString(name));
        packageDescription = description;
    }

    public String getPackageName() {
        return this.packageName;
    }

    /**
     * Returns true if both insurance packages have the same package name.
     * This defines a weaker notion of equality between two insurance packages.
     */
    public boolean isSameInsurancePackage(InsurancePackage otherInsurancePackage) {
        if (otherInsurancePackage == this) {
            return true;
        }

        return otherInsurancePackage != null && otherInsurancePackage.packageName.equals(packageName);
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
        return packageName.equals(otherInsurancePackage.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.packageName, this.packageDescription);
    }

    @Override
    public String toString() {
        return packageName.toString();
    }
}
