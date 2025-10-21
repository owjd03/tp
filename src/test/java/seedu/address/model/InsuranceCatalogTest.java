package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalInsurancePackages.BRONZE;
import static seedu.address.testutil.TypicalInsurancePackages.GOLD;
import static seedu.address.testutil.TypicalInsurancePackages.SILVER;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.insurance.InsurancePackage;

public class InsuranceCatalogTest {

    private InsuranceCatalog insuranceCatalog;

    @BeforeEach
    public void setUp() {
        insuranceCatalog = new InsuranceCatalog();
        List<InsurancePackage> packages = Arrays.asList(GOLD, SILVER, BRONZE);
        insuranceCatalog.setInsurancePackages(packages);
    }

    @Test
    public void isValidInsurancePackage() {
        // valid package names (case-insensitive)
        assertTrue(InsuranceCatalog.isValidInsurancePackage("Gold"));
        assertTrue(InsuranceCatalog.isValidInsurancePackage("gold"));
        assertTrue(InsuranceCatalog.isValidInsurancePackage("GOLD"));
        assertTrue(InsuranceCatalog.isValidInsurancePackage("Silver"));
        assertTrue(InsuranceCatalog.isValidInsurancePackage("BRONZE"));

        // invalid package names
        assertFalse(InsuranceCatalog.isValidInsurancePackage("Platinum"));
        assertFalse(InsuranceCatalog.isValidInsurancePackage("goldd")); // typo
        assertFalse(InsuranceCatalog.isValidInsurancePackage("")); // empty string
        assertFalse(InsuranceCatalog.isValidInsurancePackage(" ")); // whitespace
        assertFalse(InsuranceCatalog.isValidInsurancePackage(null)); // null input
    }
}
