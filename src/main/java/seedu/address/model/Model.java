package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Sorts the person list by name in alphabetical order.
     */
    void sortPersonList(Comparator<Person> comparator);

    /**
     * Returns the user prefs' insurance catalog file path.
     */
    Path getInsuranceCatalogFilePath();

    /**
     * Sets the user prefs' insurance catalog file path.
     */
    void setInsuranceCatalogFilePath(Path insuranceCatalogFilePath);

    /**
     * Replaces insurance catalog data with the data in {@code insuranceCatalog}.
     */
    void setInsuranceCatalog(ReadOnlyInsuranceCatalog insuranceCatalog);

    /** Returns the InsuranceCatalog */
    ReadOnlyInsuranceCatalog getInsuranceCatalog();

    /**
     * Returns true if an insurance package with the same identity as {@code insurancePackage} exists in the insurance catalog.
     */
    boolean hasInsurancePackage(InsurancePackage insurancePackage);

    /**
     * Deletes the given insurance package.
     * The insurance package must exist in the insurance catalog.
     */
    void deleteInsurancePackage(InsurancePackage target);

    /**
     * Adds the given insurance package.
     * {@code insurancePackage} must not already exist in the insurance catalog.
     */
    void addInsurancePackage(InsurancePackage insurancePackage);

    /**
     * Replaces the given insurance package {@code target} with {@code editedInsurancePackage}.
     * {@code target} must exist in the insurance catalog.
     * The insurance package identity of {@code editedInsurancePackage} must not be the same as another existing
     * insurance package in the insurance catalog.
     */
    void setInsurancePackage(InsurancePackage target, InsurancePackage editedInsurancePackage);

    /**
     * Sorts the insurance package list using the given comparator.
     */
    void sortInsurancePackageList(Comparator<InsurancePackage> comparator);

    /** Returns an unmodifiable view of the filtered insurance package list */
    ObservableList<InsurancePackage> getFilteredInsurancePackageList();

    /**
     * Updates the filter of the filtered InsurancePackage list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredInsurancePackageList(Predicate<InsurancePackage> predicate);
}
