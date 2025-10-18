package seedu.address.model.insurance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalInsurancePackages.BRONZE;
import static seedu.address.testutil.TypicalInsurancePackages.GOLD;
import static seedu.address.testutil.TypicalInsurancePackages.SILVER;
import static seedu.address.testutil.TypicalInsurancePackages.UNDECIDED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.insurance.exceptions.DuplicateInsurancePackageException;
import seedu.address.model.insurance.exceptions.InsurancePackageNotFoundException;

public class UniqueInsurancePackageListTest {

    private final UniqueInsurancePackageList uniqueInsurancePackageList = new UniqueInsurancePackageList();

    @Test
    public void contains_nullInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueInsurancePackageList.contains(null));
    }

    @Test
    public void contains_insurancePackageNotInList_returnsFalse() {
        assertFalse(uniqueInsurancePackageList.contains(GOLD));
    }

    @Test
    public void contains_insurancePackageInList_returnsTrue() {
        uniqueInsurancePackageList.add(SILVER);
        assertTrue(uniqueInsurancePackageList.contains(SILVER));
    }

    @Test
    public void add_nullInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueInsurancePackageList.add(null));
    }

    @Test
    public void add_duplicateInsurancePackage_throwsDuplicateInsurancePackageException() {
        uniqueInsurancePackageList.add(SILVER);
        assertThrows(DuplicateInsurancePackageException.class, () -> uniqueInsurancePackageList.add(SILVER));
    }

    @Test
    public void setInsurancePackage_nullTargetInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueInsurancePackageList.setInsurancePackage(null, BRONZE));
    }

    @Test
    public void setInsurancePackage_nullEditedInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueInsurancePackageList.setInsurancePackage(SILVER, null));
    }

    @Test
    public void setInsurancePackage_targetInsurancePackageNotInList_throwsInsurancePackageNotFoundException() {
        assertThrows(InsurancePackageNotFoundException.class, () ->
                uniqueInsurancePackageList.setInsurancePackage(UNDECIDED, GOLD));
    }

    @Test
    public void setInsurancePackage_editedInsurancePackageIsSameInsurancePackage_success() {
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.setInsurancePackage(GOLD, GOLD);
        UniqueInsurancePackageList expectedUniqueInsurancePackageList = new UniqueInsurancePackageList();
        expectedUniqueInsurancePackageList.add(GOLD);
        assertEquals(expectedUniqueInsurancePackageList, uniqueInsurancePackageList);
    }

    @Test
    public void setInsurancePackage_editedInsurancePackageHasDifferentPackageName_success() {
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.setInsurancePackage(GOLD, SILVER);
        UniqueInsurancePackageList expectedUniqueInsurancePackageList = new UniqueInsurancePackageList();
        expectedUniqueInsurancePackageList.add(SILVER);
        assertEquals(expectedUniqueInsurancePackageList, uniqueInsurancePackageList);
    }

    @Test
    public void setInsurancePackage_editedPackageHasNonUniquePackageName_throwsDuplicateInsurancePackageException() {
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.add(SILVER);
        assertThrows(DuplicateInsurancePackageException.class, () ->
                uniqueInsurancePackageList.setInsurancePackage(GOLD, SILVER));
    }

    @Test
    public void remove_nullInsurancePackage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueInsurancePackageList.remove(null));
    }

    @Test
    public void remove_insurancePackageDoesNotExist_throwsInsurancePackageNotFoundException() {
        assertThrows(InsurancePackageNotFoundException.class, () -> uniqueInsurancePackageList.remove(UNDECIDED));
    }

    @Test
    public void remove_existingInsurancePackage_removesInsurancePackage() {
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.remove(GOLD);
        UniqueInsurancePackageList expectedUniqueInsurancePackageList = new UniqueInsurancePackageList();
        assertEquals(expectedUniqueInsurancePackageList, uniqueInsurancePackageList);
    }

    @Test
    public void setInsurancePackages_nullUniqueInsurancePackageList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages((UniqueInsurancePackageList) null));
    }

    @Test
    public void setInsurancePackages_uniqueInsurancePackageList_replacesListWithProvidedUniqueInsurancePackageList() {
        uniqueInsurancePackageList.add(GOLD);
        UniqueInsurancePackageList expectedUniqueInsurancePackageList = new UniqueInsurancePackageList();
        expectedUniqueInsurancePackageList.add(SILVER);
        uniqueInsurancePackageList.setInsurancePackages(expectedUniqueInsurancePackageList);
        assertEquals(expectedUniqueInsurancePackageList, uniqueInsurancePackageList);
    }

    @Test
    public void setInsurancePackages_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages((List<InsurancePackage>) null));
    }

    @Test
    public void setInsurancePackages_list_replacesOwnListWithProvidedList() {
        uniqueInsurancePackageList.add(GOLD);
        List<InsurancePackage> insurancePackageList = Collections.singletonList(SILVER);
        uniqueInsurancePackageList.setInsurancePackages(insurancePackageList);
        UniqueInsurancePackageList expectedUniqueInsurancePackageList = new UniqueInsurancePackageList();
        expectedUniqueInsurancePackageList.add(SILVER);
        assertEquals(expectedUniqueInsurancePackageList, uniqueInsurancePackageList);
    }

    @Test
    public void setInsurancePackages_listWithDuplicateInsurancePackages_throwsDuplicateInsurancePackageException() {
        List<InsurancePackage> listWithDuplicateInsurancePackages = Arrays.asList(GOLD, GOLD);
        assertThrows(DuplicateInsurancePackageException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages(listWithDuplicateInsurancePackages));

        List<InsurancePackage> longerListWithDuplicateInsurancePackages = Arrays.asList(GOLD, SILVER, GOLD);
        assertThrows(DuplicateInsurancePackageException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages(longerListWithDuplicateInsurancePackages));

        List<InsurancePackage> anotherLongerListWithDuplicateInsurancePackages = Arrays.asList(GOLD, SILVER, SILVER);
        assertThrows(DuplicateInsurancePackageException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages(anotherLongerListWithDuplicateInsurancePackages));

        List<InsurancePackage> longestListWithDuplicateInsurancePackages =
                Arrays.asList(GOLD, SILVER, BRONZE, UNDECIDED, BRONZE);
        assertThrows(DuplicateInsurancePackageException.class, () ->
                uniqueInsurancePackageList.setInsurancePackages(longestListWithDuplicateInsurancePackages));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                uniqueInsurancePackageList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void sort_list_sortsListCorrectly() {
        uniqueInsurancePackageList.add(UNDECIDED);
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.add(BRONZE);
        uniqueInsurancePackageList.add(SILVER);
        uniqueInsurancePackageList.sort(Comparator.comparing(p -> p.packageName));

        UniqueInsurancePackageList expectedList = new UniqueInsurancePackageList();
        expectedList.add(BRONZE);
        expectedList.add(GOLD);
        expectedList.add(SILVER);
        expectedList.add(UNDECIDED);
        assertEquals(expectedList, uniqueInsurancePackageList);
    }

    @Test
    public void iterator_valid_iteratesCorrectly() {
        uniqueInsurancePackageList.add(GOLD);
        uniqueInsurancePackageList.add(SILVER);
        List<InsurancePackage> iteratedList = new ArrayList<>();
        uniqueInsurancePackageList.iterator().forEachRemaining(iteratedList::add);
        assertEquals(Arrays.asList(GOLD, SILVER), iteratedList);
    }

    @Test
    public void equals_wordsCorrectly() {
        // same object returns true
        assertTrue(uniqueInsurancePackageList.equals(uniqueInsurancePackageList));

        // null returns false
        assertFalse(uniqueInsurancePackageList.equals(null));

        // different type returns false
        assertFalse(uniqueInsurancePackageList.equals("Clearly not an UniqueInsurancePackageList"));

        // different list returns false
        UniqueInsurancePackageList otherList = new UniqueInsurancePackageList();
        otherList.add(GOLD);
        assertFalse(uniqueInsurancePackageList.equals(otherList));

        // same internal list returns true
        uniqueInsurancePackageList.add(GOLD);
        UniqueInsurancePackageList sameList = new UniqueInsurancePackageList();
        sameList.add(GOLD);
        assertTrue(uniqueInsurancePackageList.equals(sameList));
    }

    @Test
    public void hashCode_valid_returnsSameHashCode() {
        uniqueInsurancePackageList.add(GOLD);
        UniqueInsurancePackageList otherList = new UniqueInsurancePackageList();
        otherList.add(GOLD);
        assertEquals(uniqueInsurancePackageList.hashCode(), otherList.hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueInsurancePackageList.asUnmodifiableObservableList().toString(),
                uniqueInsurancePackageList.toString());
    }
}
