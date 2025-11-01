package seedu.address.model.insurance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class InsurancePackageTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new InsurancePackage(null, "Testing123"));
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new InsurancePackage("Gold", null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new InsurancePackage("", "Testing123"));
        assertThrows(IllegalArgumentException.class, () -> new InsurancePackage("   ", "Testing123"));
    }

    @Test
    public void constructor_validArguments_createsInsurancePackage() {
        InsurancePackage validInsurancePackage = new InsurancePackage("Testing123", "Testing456");
        assertEquals("Testing123", validInsurancePackage.getPackageName());
        assertEquals("Testing456", validInsurancePackage.getPackageDescription());

        // Check that empty descriptions are allowed
        InsurancePackage insurancePackageEmptyDescription = new InsurancePackage("Testing789", "");
        assertEquals("Testing789", insurancePackageEmptyDescription.getPackageName());
        assertEquals("", insurancePackageEmptyDescription.getPackageDescription());

        // Check that descriptions with spaces only are just empty descriptions
        InsurancePackage insurancePackageWhiteSpaceDescription = new InsurancePackage("Last", "      ");
        assertEquals("Last", insurancePackageWhiteSpaceDescription.getPackageName());
        assertEquals("", insurancePackageWhiteSpaceDescription.getPackageDescription());
    }

    @Test
    public void formatPackageName_variousInputs_returnsCorrectlyFormattedName() {
        assertEquals("Gold", new InsurancePackage("gold", "").getPackageName());
        assertEquals("Silver Plus", new InsurancePackage("sIlVeR pLuS", "").getPackageName());
        assertEquals("Platinum Deluxe", new InsurancePackage("  platinum   deluxe  ", "").getPackageName());
        assertEquals("Bronze", new InsurancePackage("Bronze", "").getPackageName());
    }

    @Test
    public void isSameInsurancePackage() {
        InsurancePackage goldV1 = new InsurancePackage("Gold", "Version 1");
        InsurancePackage goldV2 = new InsurancePackage("Gold", "Version 2");
        InsurancePackage silver = new InsurancePackage("Silver", "Version 1");

        // Same object -> returns true
        assertTrue(goldV1.isSameInsurancePackage(goldV1));

        // Null -> returns false
        assertFalse(goldV1.isSameInsurancePackage(null));

        // Same name, different description -> returns true
        assertTrue(goldV1.isSameInsurancePackage(goldV2));

        // Same name, different case -> returns true
        InsurancePackage goldLowercase = new InsurancePackage("gold", "Version 1");
        assertTrue(goldV1.isSameInsurancePackage(goldLowercase));

        // Different name -> returns false
        assertFalse(goldV1.isSameInsurancePackage(silver));
    }

    @Test
    public void equals() {
        InsurancePackage goldV1 = new InsurancePackage("Gold", "Version 1");
        InsurancePackage goldV2 = new InsurancePackage("Gold", "Version 2");
        InsurancePackage silver = new InsurancePackage("Silver", "Version 1");

        // Same object -> returns true
        assertTrue(goldV1.equals(goldV1));

        // Same values (name and description) -> returns true
        InsurancePackage goldV1Copy = new InsurancePackage("Gold", "Version 1");
        assertTrue(goldV1.equals(goldV1Copy));

        // Different type -> returns false
        assertFalse(goldV1.equals(5));

        // Null -> returns false
        assertFalse(goldV1.equals(null));

        // Different name -> returns false
        assertFalse(goldV1.equals(silver));

        // Same name, different description -> returns true
        assertTrue(goldV1.equals(goldV2));

        // Same name, different case -> returns true
        InsurancePackage goldLowercase = new InsurancePackage("gold", "Version 3");
        assertTrue(goldV1.equals(goldLowercase));
    }

    @Test
    public void hashCode_consistencyWithEquals() {
        // Two packages that are equal must have the same hash code
        InsurancePackage goldV1 = new InsurancePackage("Gold", "Version 1");
        InsurancePackage goldV2 = new InsurancePackage("Gold", "Version 2");
        InsurancePackage goldLowercase = new InsurancePackage("gold", "Another version");

        // Same name, different description
        assertEquals(goldV1.hashCode(), goldV2.hashCode());

        // Same name, different case
        assertEquals(goldV1.hashCode(), goldLowercase.hashCode());
    }

    @Test
    public void toString_worksCorrectly() {
        InsurancePackage gold = new InsurancePackage("Gold Plan", "I am Gold");
        assertEquals("Gold Plan: I am Gold", gold.toString());

        // No description provided
        InsurancePackage noDescription = new InsurancePackage("Testing", "");
        assertEquals("Testing: No description provided", noDescription.toString());
    }
}
