package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.insurance.UniqueInsurancePackageList;

/**
 * Wraps all insurance package data at the insurance catalog level.
 * Duplicates are not allowed (by .isSameInsurancePackage comparison)
 *
 * @see InsurancePackage#isSameInsurancePackage(InsurancePackage)
 */
public class InsuranceCatalog implements ReadOnlyInsuranceCatalog {

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

    //// list overwrite operations

    /**
     * Replaces the contents of the insurance package list with {@code insurancePackages}.
     * {@code insurancePackages} must not contain duplicate insurance packages.
     */
    public void setInsurancePackages(List<InsurancePackage> insurancePackages) {
        this.insurancePackages.setInsurancePackages(insurancePackages);
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
    }

    /**
     * Replaces the given insurance package {@code target} in the list with {@code editedInsurancePackage}.
     * {@code target} must exist in the insurance catalog.
     * The insurance package name of {@code editedInsurancePackage} must not be the same as another existing package.
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
    }

    /**
     * Sorts the insurance package list using the provided comparator.
     */
    public void sortInsurancePackageList(Comparator<InsurancePackage> comparator) {
        this.insurancePackages.sort(comparator);
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
