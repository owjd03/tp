package seedu.address.model.insurance;

/**
 * Enum representing predefined insurance packages, as well as an undecided option.
 * Each enum constant has a human-readable string representation.
 */
public enum InsurancePackageEnum {
    GOLD("Gold",
            "Our premium, all-inclusive plan offering maximum benefits and total peace of mind."),
    SILVER("Silver",
            "Our most popular plan, offering a balanced blend of coverage and value."),
    BRONZE("Bronze",
            "A foundational plan that covers all your core, essential needs."),
    UNDECIDED("Undecided", "No insurance package selected.");

    private final String packageName;
    private final String packageDescription;

    InsurancePackageEnum(String name, String description) {
        this.packageName = name;
        this.packageDescription = description;
    }

    /**
     * Returns the human-readable string representation of the insurance package.
     * In other words, this also returns the name of the insurance package.
     *
     * @return The string representation of the insurance package.
     */
    @Override
    public String toString() {
        return packageName;
    }

    /**
     * @return The description of the insurance package.
     */
    public String getDescription() {
        return packageDescription;
    }

    /**
     * Checks whether a given string is a valid insurance package.
     * Packages with the same name but different descriptions are considered valid.
     * @param test
     * @return true if the string is a valid insurance package, false otherwise.
     */
    public static boolean isValidInsurancePackage(String test) {
        for (seedu.address.model.insurance.InsurancePackageEnum insurancePackage
                : seedu.address.model.insurance.InsurancePackageEnum.values()) {
            if (insurancePackage.packageName.equals(test)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a string to its corresponding InsurancePackageEnum constant value.
     * @param test The string representation of the insurance package.
     * @return The corresponding InsurancePackageEnum.
     * @throws IllegalArgumentException if the string does not match any insurance package.
     */
    public static seedu.address.model.insurance.InsurancePackageEnum fromString(String test) {
        for (seedu.address.model.insurance.InsurancePackageEnum insurancePackage
                : seedu.address.model.insurance.InsurancePackageEnum.values()) {
            if (insurancePackage.packageName.equals(test)) {
                return insurancePackage;
            }
        }
        throw new IllegalArgumentException("Invalid insurance package: " + test);
    }
}

