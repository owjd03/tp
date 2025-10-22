package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.insurance.InsurancePackage;

/**
 * Unmodifiable view of an insurance catalog.
 */
public interface ReadOnlyInsuranceCatalog {

    /**
     * Returns an unmodifiable view of the insurance package list.
     * This list will not contain any duplicate insurance packages.
     */
    ObservableList<InsurancePackage> getInsurancePackageList();
}
