package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final InsuranceCatalog insuranceCatalog;
    private final UserPrefs userPrefs;
    private final FilteredList<InsurancePackage> filteredInsurancePackages;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook,
                        ReadOnlyInsuranceCatalog insuranceCatalog,
                        ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, insuranceCatalog, userPrefs);

        logger.fine("Initializing with address book: " + addressBook
                + " and user prefs " + userPrefs
                + " and insurance catalog " + insuranceCatalog);

        this.addressBook = new AddressBook(addressBook);
        this.insuranceCatalog = new InsuranceCatalog(insuranceCatalog);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredInsurancePackages = new FilteredList<>(this.insuranceCatalog.getInsurancePackageList());
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new InsuranceCatalog(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getInsuranceCatalogFilePath() {
        return userPrefs.getInsuranceCatalogFilePath();
    }

    @Override
    public void setInsuranceCatalogFilePath(Path insuranceCatalogFilePath) {
        requireNonNull(insuranceCatalogFilePath);
        userPrefs.setInsuranceCatalogFilePath(insuranceCatalogFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void sortPersonList(Comparator<Person> comparator) {
        addressBook.sortPersonList(comparator);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Insurance Package-Level Accessors ==========================================================

    @Override
    public void setInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog) {
        this.insuranceCatalog.resetData(insuranceCatalog);
    }

    @Override
    public ReadOnlyInsuranceCatalog getInsuranceCatalog() {
        return this.insuranceCatalog;
    }

    @Override
    public boolean hasInsurancePackage(InsurancePackage insurancePackage) {
        requireNonNull(insurancePackage);
        return this.insuranceCatalog.hasInsurancePackage(insurancePackage);
    }

    @Override
    public void deleteInsurancePackage(InsurancePackage target) {
        this.insuranceCatalog.removeInsurancePackage(target);
    }

    @Override
    public void addInsurancePackage(InsurancePackage insurancePackage) {
        this.insuranceCatalog.addInsurancePackage(insurancePackage);
    }

    @Override
    public void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage) {
        requireAllNonNull(target, editedInsurancePackage);
        this.insuranceCatalog.setInsurancePackage(target, editedInsurancePackage);
    }

    @Override
    public void sortInsurancePackageList(Comparator<InsurancePackage> comparator) {
        this.insuranceCatalog.sortInsurancePackageList(comparator);
    }

    //=========== Filtered Insurance Package List Accessors ==================================================

    @Override
    public ObservableList<InsurancePackage> getFilteredInsurancePackageList() {
        return filteredInsurancePackages;
    }

    @Override
    public void updateFilteredInsurancePackageList(Predicate<InsurancePackage> predicate) {
        requireNonNull(predicate);
        filteredInsurancePackages.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredInsurancePackages.equals(otherModelManager.filteredInsurancePackages);
    }
}
