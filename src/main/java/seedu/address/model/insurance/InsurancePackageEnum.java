package seedu.address.model.insurance;

/**
 * Enum representing predefined insurance packages, as well as an undecided option.
 * Each enum constant has a human-readable string representation.
 */
public enum InsurancePackageEnum {
    GOLD("Gold"),
    SILVER("Silver"),
    BRONZE("Bronze"),
    UNDECIDED("Undecided");

    private final String displayValue;


    InsurancePackageEnum(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Returns the human-readable string representation of the insurance package.
     * @return The string representation of the insurance package.
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Checks whether a given string is a valid insurance package.
     * @param test
     * @return true if the string is a valid insurance package, false otherwise.
     */
    public static boolean isValidInsurancePackage(String test) {
        for (seedu.address.model.insurance.InsurancePackageEnum insurancePackage
                : seedu.address.model.insurance.InsurancePackageEnum.values()) {
            if (insurancePackage.displayValue.equals(test)) {
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
            if (insurancePackage.displayValue.equals(test)) {
                return insurancePackage;
            }
        }
        throw new IllegalArgumentException("Invalid insurance package: " + test);
    }
}

