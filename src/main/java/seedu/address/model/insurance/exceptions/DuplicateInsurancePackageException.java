package seedu.address.model.insurance.exceptions;

/**
 * Signals that the operation will result in duplicate Insurance Package
 * (Insurance Packages are considered duplicates if they have the same package name).
 */
public class DuplicateInsurancePackageException extends RuntimeException {
    public DuplicateInsurancePackageException() {
        super("Operation would result in duplicate insurance packages");
    }
}
