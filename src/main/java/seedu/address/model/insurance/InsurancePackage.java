package seedu.address.model.insurance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.InsuranceCatalog.getValidInsurancePackageNames;

import java.util.Objects;

/**
 * Represents an insurance package that a Person can be assigned to.
 * Guarantees: immutable; must be one of the predefined constants in {@link InsurancePackageEnum}.
 */
public class InsurancePackage {

    /**
     * A static String to inform the user the constraints of the Insurance Package class.
     * Usage of the MESSAGE_CONSTRAINTS will need to be with
     * {@link seedu.address.model.InsuranceCatalog#getValidInsurancePackageNames()}
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Insurance package name cannot be empty and must be one of the following: %s";

    private final String packageName;
    private final String packageDescription;

    /**
     * Constructs a {@code InsurancePackage}.
     *
     * @param name             A valid insurance package, represented by its name.
     * @param description      Description of the insurance package.
     */
    public InsurancePackage(String name, String description) {
        requireAllNonNull(name, description);
        packageName = formatPackageName(name);
        packageDescription = description;
    }

    /**
     * Formats a package name to have the first letter of each word capitalized.
     * e.g., "deluxe package" -> "Deluxe Package"
     *
     * @param name The inputted packageName by the user.
     * @return A formatted, standardized package name.
     */
    private static String formatPackageName(String name) {
        requireNonNull(name);
        checkArgument(!name.trim().isEmpty(),
                String.format(MESSAGE_CONSTRAINTS, getValidInsurancePackageNames()));
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return formattedName.toString().trim();
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getPackageDescription() {
        return this.packageDescription;
    }

    /**
     * Returns true if both insurance packages have the same package name.
     * This defines a weaker notion of equality between two insurance packages.
     */
    public boolean isSameInsurancePackage(InsurancePackage otherInsurancePackage) {
        if (otherInsurancePackage == this) {
            return true;
        }

        return otherInsurancePackage != null
                && otherInsurancePackage.packageName.equalsIgnoreCase(packageName);
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
        // 2 insurance packages are considered equal if they have the same packageName
        // even if they have different packageDescription
        return packageName.equalsIgnoreCase(otherInsurancePackage.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.packageName.toLowerCase(), this.packageDescription);
    }

    @Override
    public String toString() {
        return packageName.toString();
    }
}
