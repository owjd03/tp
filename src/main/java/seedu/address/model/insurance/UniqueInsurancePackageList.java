package seedu.address.model.insurance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.insurance.exceptions.DuplicateInsurancePackageException;
import seedu.address.model.insurance.exceptions.InsurancePackageNotFoundException;

/**
 * A list of insurance packages that enforces uniqueness between its elements and does not allow nulls.
 * An insurance package is considered unique by comparing using
 * {@code InsurancePackage#isSameInsurancePackage(InsurancePackage)}. As such, adding and updating of insurance packages
 * uses InsurancePackage#isSameInsurancePackage(InsurancePackage) for equality to ensure that the insurance package
 * being added or updated is unique in terms of package name in the UniqueInsurancePackageList. However, the removal of
 * an insurance package uses InsurancePackage#equals(Object) to ensure that the insurance package with exactly the same
 * fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see InsurancePackage#isSameInsurancePackage(InsurancePackage)
 */
public class UniqueInsurancePackageList implements Iterable<InsurancePackage> {
    private final ObservableList<InsurancePackage> internalList = FXCollections.observableArrayList();
    private final ObservableList<InsurancePackage> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent insurance package as the given argument.
     */
    public boolean contains(InsurancePackage toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameInsurancePackage);
    }

    /**
     * Adds an insurance package to the list.
     * The insurance package must not already exist in the list.
     */
    public void add(InsurancePackage toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateInsurancePackageException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the insurance package {@code target} in the list with {@code editedInsurancePackage}.
     * {@code target} must exist in the list.
     * The package name of {@code editedInsurancePackage} must not be the same as another
     * existing insurance package in the list.
     */
    public void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage) {
        requireAllNonNull(target, editedInsurancePackage);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new InsurancePackageNotFoundException();
        }

        if (!target.isSameInsurancePackage(editedInsurancePackage) && contains(editedInsurancePackage)) {
            throw new DuplicateInsurancePackageException();
        }

        internalList.set(index, editedInsurancePackage);
    }

    /**
     * Removes the equivalent insurance package from the list.
     * The insurance package must exist in the list.
     */
    public void remove(InsurancePackage toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new InsurancePackageNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code insurancePackages}.
     * {@code insurancePackages} must not contain duplicate insurance packages.
     */
    public void setInsurancePackages(List<InsurancePackage> insurancePackages) {
        requireAllNonNull(insurancePackages);
        if (!insurancePackagesAreUnique(insurancePackages)) {
            throw new DuplicateInsurancePackageException();
        }

        internalList.setAll(insurancePackages);
    }

    public void setInsurancePackages(UniqueInsurancePackageList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Sorts the list using the provided comparator.
     */
    public void sort(Comparator<InsurancePackage> comparator) {
        internalList.sort(comparator);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<InsurancePackage> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<InsurancePackage> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueInsurancePackageList)) {
            return false;
        }

        UniqueInsurancePackageList otherUniqueInsurancePackageList = (UniqueInsurancePackageList) other;
        return internalList.equals(otherUniqueInsurancePackageList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code insurancePackages} contains only unique insurancePackages.
     */
    private boolean insurancePackagesAreUnique(List<InsurancePackage> insurancePackages) {
        for (int i = 0; i < insurancePackages.size() - 1; i++) {
            for (int j = i + 1; j < insurancePackages.size(); j++) {
                if (insurancePackages.get(i).isSameInsurancePackage(insurancePackages.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
