package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.insurance.UniqueInsurancePackageList;
import seedu.address.model.insurance.exceptions.InsurancePackageNotFoundException;

/**
 * Wraps all insurance package data at the insurance catalog level.
 * Duplicates are not allowed (by .isSameInsurancePackage comparison)
 *
 * @see InsurancePackage#isSameInsurancePackage(InsurancePackage)
 */
public class InsuranceCatalog implements ReadOnlyInsuranceCatalog {

    /**
     * A static list of valid insurance package names, used for quick validation by commands and parsers.
     * This list is kept in sync with the master list of insurance packages.
     */
    private static final List<String> VALID_PACKAGE_NAMES = new ArrayList<>();

    /**
     * A static map to store descriptions, keyed by the package's lowercase name.
     * This list is kept in sync with the master list of insurance packages.
     */
    private static final Map<String, String> PACKAGE_DESCRIPTIONS = new HashMap<>();


    private final UniqueInsurancePackageList insurancePackages;

    /*
     * The 'unusual' code block below is a guard against non-instantiation of UniqueInsurancePackageList.
     */
    {
        this.insurancePackages = new UniqueInsurancePackageList();
    }

    public InsuranceCatalog() {}

    /**
     * Creates an InsuranceCatalog using the InsurancePackages in the {@code toBeCopied}.
     */
    public InsuranceCatalog(ReadOnlyInsuranceCatalog toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// Static validation checks

    /**
     * Checks whether a given string is a valid insurance package.
     * Packages with the same name but different descriptions are considered valid.
     * @param toCheck
     * @return true if the string is a valid insurance package, false otherwise.
     */
    public static boolean isValidInsurancePackage(String toCheck) {
        if (toCheck == null) {
            return false;
        }
        return VALID_PACKAGE_NAMES.stream().anyMatch(name -> name.equalsIgnoreCase(toCheck));
    }

    /**
     * Returns a string of valid insurance package names formatted nicely.
     */
    public static String getValidInsurancePackageNames() {
        return String.join(", ", VALID_PACKAGE_NAMES);
    }

    /**
     * This method should only be called by {@link InsuranceCatalog#setInsurancePackages}.
     */
    private static void loadInsurancePackages(List<InsurancePackage> insurancePackages) {
        VALID_PACKAGE_NAMES.clear();
        PACKAGE_DESCRIPTIONS.clear();
        for (InsurancePackage pkg : insurancePackages) {
            String packageName = pkg.getPackageName();
            String packageDesc = pkg.getPackageDescription();

            VALID_PACKAGE_NAMES.add(packageName);
            PACKAGE_DESCRIPTIONS.put(packageName.toLowerCase(), packageDesc);
        }
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the insurance package list with {@code insurancePackages}.
     * {@code insurancePackages} must not contain duplicate insurance packages.
     */
    public void setInsurancePackages(List<InsurancePackage> insurancePackages) {
        this.insurancePackages.setInsurancePackages(insurancePackages);
        loadInsurancePackages(insurancePackages);
    }

    /**
     * Resets the existing data of this {@code InsuranceCatalog} with {@code newData}.
     */
    public void resetData(ReadOnlyInsuranceCatalog newData) {
        requireNonNull(newData);
        setInsurancePackages(newData.getInsurancePackageList());
    }

    //// insurance package-level operations

    /**
     * Returns true if an insurance package with the same identity as {@code insurancePackage} exists in the catalog.
     */
    public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
        requireNonNull(insurancePackage);
        return this.insurancePackages.contains(insurancePackage);
    }

    /**
     * Adds an insurance package to the insurance catalog.
     * The insurance package must not already exist in the catalog.
     */
    public void addInsurancePackage(InsurancePackage p) {
        this.insurancePackages.add(p);
        VALID_PACKAGE_NAMES.add(p.getPackageName());
        PACKAGE_DESCRIPTIONS.put(p.getPackageName().toLowerCase(), p.getPackageDescription());
    }

    /**
     * Replaces the given insurance package {@code target} in the list with {@code editedInsurancePackage}.
     * {@code target} must exist in the insurance catalog.
     * The package name of {@code editedInsurancePackage} must be the same as {@code target}, as this method is
     * used only for editing the package description.
     *
     * @throws InsurancePackageNotFoundException if {@code target} is not found in the list.
     * @throws IllegalArgumentException if the package name of {@code editedInsurancePackage}
     *      is different from {@code target}.
     */
    public void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage) {
        requireNonNull(editedInsurancePackage);
        this.insurancePackages.setInsurancePackage(target, editedInsurancePackage);
    }

    /**
     * Removes {@code key} from this {@code InsuranceCatalog}.
     * {@code key} must exist in the catalog.
     */
    public void removeInsurancePackage(InsurancePackage key) {
        this.insurancePackages.remove(key);
        VALID_PACKAGE_NAMES.remove(key.getPackageName());
        PACKAGE_DESCRIPTIONS.remove(key.getPackageName().toLowerCase());
    }

    /**
     * Sorts the insurance package list using the provided comparator.
     */
    public void sortInsurancePackageList(Comparator<InsurancePackage> comparator) {
        this.insurancePackages.sort(comparator);
    }

    /**
     * Gets the description for a given package name (case-insensitive).
     * Returns an empty string if no description is found.
     *
     * @param name The name of the package.
     * @return The description, or an empty string if not found.
     */
    public static String getPackageDescription(String name) {
        if (name == null) {
            return "";
        }
        return PACKAGE_DESCRIPTIONS.getOrDefault(name.toLowerCase(), "");
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("insurancePackages", this.insurancePackages)
                .toString();
    }

    @Override
    public ObservableList<InsurancePackage> getInsurancePackageList() {
        return this.insurancePackages.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InsuranceCatalog)) {
            return false;
        }

        InsuranceCatalog otherInsuranceCatalog = (InsuranceCatalog) other;
        return this.insurancePackages.equals(otherInsuranceCatalog.insurancePackages);
    }

    @Override
    public int hashCode() {
        return this.insurancePackages.hashCode();
    }
}
