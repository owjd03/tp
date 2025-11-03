package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.InsuranceCatalog;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.MaritalStatusEnum;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "The index must be a positive whole number (e.g. 1, 2, 3).";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    private static String validateStringField(String value, Predicate<String> validator, String constraintMessage)
            throws ParseException {
        requireNonNull(value);
        String trimmedValue = value.trim();
        if (!validator.test(trimmedValue)) {
            throw new ParseException(constraintMessage);
        }
        return trimmedValue;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String processedName = name.trim();

        if (processedName.startsWith("\"") && processedName.endsWith("\"")) {
            if (processedName.length() < 2) {
                throw new ParseException("Name value cannot be an empty quote.");
            }
            processedName = processedName.substring(1, processedName.length() - 1);
        }

        if (!Name.isValidName(processedName) || processedName.isBlank()) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        return new Name(processedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        String trimmedPhone = validateStringField(phone, Phone::isValidPhone, Phone.MESSAGE_CONSTRAINTS);
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        String processedAddress = address.trim();
        if (processedAddress.startsWith("\"") && processedAddress.endsWith("\"")) {
            if (processedAddress.length() < 2) {
                throw new ParseException(Address.MESSAGE_CONSTRAINTS);
            }
            processedAddress = processedAddress.substring(1, processedAddress.length() - 1).trim();
        }
        if (processedAddress.isEmpty()) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(processedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        String trimmedEmail = validateStringField(email, Email::isValidEmail, Email.MESSAGE_CONSTRAINTS);
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String salary} into a {@code Salary}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code salary} is invalid.
     */
    public static Salary parseSalary(String salary) throws ParseException {
        String trimmedSalary = validateStringField(salary, Salary::isValidSalary, Salary.MESSAGE_CONSTRAINTS);
        return new Salary(trimmedSalary);
    }

    /**
     * Parses a {@code String input} representing either a person's date of birth into a {@code DateOfBirth}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateOfBirth} is invalid.
     */
    public static DateOfBirth parseDateOfBirth(String input) throws ParseException {
        String trimmedDateOfBirth = validateStringField(input, DateOfBirth::isValidDateOfBirth,
                DateOfBirth.MESSAGE_CONSTRAINTS);
        return new DateOfBirth(trimmedDateOfBirth);
    }

    /**
     * Parses a {@code String maritalStatus} into a {@code MaritalStatus}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code maritalStatus} is invalid.
     */
    public static MaritalStatus parseMaritalStatus(String maritalStatus) throws ParseException {
        requireNonNull(maritalStatus);
        String trimmedMaritalStatus = maritalStatus.trim();

        if (trimmedMaritalStatus.isEmpty()) {
            throw new ParseException(MaritalStatus.MESSAGE_CONSTRAINTS);
        }

        String normalizedStatus = Character.toUpperCase(trimmedMaritalStatus.charAt(0))
                + trimmedMaritalStatus.substring(1).toLowerCase();

        if (!MaritalStatusEnum.isValidMaritalStatus(normalizedStatus)) {
            throw new ParseException(MaritalStatus.MESSAGE_CONSTRAINTS);
        }
        return new MaritalStatus(normalizedStatus);
    }

    /**
     * Parses a {@code String occupation} into an {@code Occupation}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code occupation} is invalid.
     */
    public static Occupation parseOccupation(String occupation) throws ParseException {
        String processedOccupation = validateStringField(occupation, Occupation::isValidOccupation,
                Occupation.MESSAGE_CONSTRAINTS);
        if (processedOccupation.startsWith("\"") && processedOccupation.endsWith("\"")) {
            if (processedOccupation.length() < 2) {
                throw new ParseException(Occupation.MESSAGE_CONSTRAINTS);
            }
            processedOccupation = processedOccupation.substring(1, processedOccupation.length() - 1).trim();
        }
        if (processedOccupation.isEmpty()) {
            throw new ParseException(Occupation.MESSAGE_CONSTRAINTS);
        }
        return new Occupation(processedOccupation);
    }

    /**
     * Parses an {@code String dependents} into a {@code Dependents}.
     * @throws ParseException if the given {@code dependents} is invalid.
     */
    public static Dependents parseDependents(String dependents) throws ParseException {
        requireNonNull(dependents);
        int num;
        String trimmedDependents = dependents.trim();

        String unspecifiedString = Dependents.createUnspecified().toString();
        if (trimmedDependents.equalsIgnoreCase(unspecifiedString)) {
            return Dependents.createUnspecified();
        }

        try {
            num = Integer.parseInt(trimmedDependents);
        } catch (NumberFormatException e) {
            // Catches "d/abc"
            throw new ParseException(Dependents.MESSAGE_CONSTRAINTS);
        }

        // This test is stricter than the Dependents class's own validation
        // because the user cannot enter a negative number.
        if (num < 0) {
            throw new ParseException(Dependents.MESSAGE_CONSTRAINTS);
        }

        try {
            return new Dependents(num);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Dependents.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String insurancePackage} into an {@code InsurancePackage}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code insurancePackage} is invalid.
     */
    public static InsurancePackage parseInsurancePackage(String insurancePackage) throws ParseException {
        requireNonNull(insurancePackage);
        String trimmedPackageName = insurancePackage.trim();

        if (!InsuranceCatalog.isValidInsurancePackage(trimmedPackageName)) {
            String validNamesString = InsuranceCatalog.getValidInsurancePackageNames();
            throw new ParseException("The insurance package '"
                    + trimmedPackageName + "' does not exist.\n"
                    + "Available packages are: " + validNamesString);
        }

        String packageDescription = InsuranceCatalog.getPackageDescription(trimmedPackageName);
        return new InsurancePackage(trimmedPackageName, packageDescription);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        String trimmedTag = validateStringField(tag, Tag::isValidTagName, Tag.MESSAGE_CONSTRAINTS);
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
