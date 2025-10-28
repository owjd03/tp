package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
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

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

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

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String salary} into a {@code Salary}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code salary} is invalid.
     */
    public static Salary parseSalary(String salary) throws ParseException {
        requireNonNull(salary);
        String trimmedSalary = salary.trim();
        if (!Salary.isValidSalary(trimmedSalary)) {
            throw new ParseException(Salary.MESSAGE_CONSTRAINTS);
        }
        return new Salary(trimmedSalary);
    }

    /**
     * Parses a {@code String input} representing either a person's date of birth into a {@code DateOfBirth}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateOfBirth} is invalid.
     */
    public static DateOfBirth parseDateOfBirth(String input) throws ParseException {
        requireNonNull(input);
        String trimmedDateOfBirth = input.trim();
        if (!DateOfBirth.isValidDateOfBirth(trimmedDateOfBirth)) {
            throw new ParseException(DateOfBirth.MESSAGE_CONSTRAINTS);
        }
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
        requireNonNull(occupation);
        String trimmedOccupation = occupation.trim();
        if (!Occupation.isValidOccupation(trimmedOccupation)) {
            throw new ParseException(Occupation.MESSAGE_CONSTRAINTS);
        }
        return new Occupation(trimmedOccupation);
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
        String trimmedInsurancePackage = insurancePackage.trim();
        if (trimmedInsurancePackage.isEmpty()) {
            throw new ParseException(InsurancePackage.MESSAGE_CONSTRAINTS);
        }
        return new InsurancePackage(trimmedInsurancePackage, "");
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
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
