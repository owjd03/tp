package seedu.address.model;

import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.model.insurance.InsurancePackage;


/**
 * Unmodifiable view of an insurance catalog.
 */
public interface ReadOnlyInsuranceCatalog {

    String DEFAULT_UNDECIDED_PACKAGE_NAME = "Undecided";

    /**
     * Returns an unmodifiable view of the insurance package list.
     * This list will not contain any duplicate insurance packages.
     */
    ObservableList<InsurancePackage> getInsurancePackageList();

    /**
     * Returns the package with the given name from the catalog.
     *
     * @param packageName The name of the package to find.
     * @return An Optional containing the package if found, or an empty Optional.
     */
    default Optional<InsurancePackage> getPackage(String packageName) {
        return getInsurancePackageList().stream()
                .filter(pkg -> pkg.getPackageName().equalsIgnoreCase(packageName))
                .findFirst();
    }

    /**
     * Returns the default "Undecided" package from the catalog.
     * Since the "Undecided" package is a default package,
     * this exception should not be thrown.
     *
     * @return The "Undecided" package.
     * @throws java.util.NoSuchElementException if the "Undecided" package is not in the catalog.
     */
    default InsurancePackage getUndecidedPackage() {
        return getPackage(DEFAULT_UNDECIDED_PACKAGE_NAME)
                .orElseThrow(() -> new java.util.NoSuchElementException(
                        "The default 'Undecided' package is missing from the catalog."));
    }
}
