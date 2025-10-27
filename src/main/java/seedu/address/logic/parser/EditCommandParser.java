package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPENDENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE_PACKAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MARITAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OCCUPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = tokenizeArguments(args);
        Index index = parseIndex(argMultimap);
        verifyNoDuplicatePrefixes(argMultimap);

        EditPersonDescriptor editPersonDescriptor = buildEditPersonDescriptor(argMultimap);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    private ArgumentMultimap tokenizeArguments(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS, PREFIX_DEPENDENTS,
                PREFIX_OCCUPATION, PREFIX_INSURANCE_PACKAGE, PREFIX_TAG);
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }
    }

    private void verifyNoDuplicatePrefixes(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS, PREFIX_DEPENDENTS,
                PREFIX_OCCUPATION, PREFIX_INSURANCE_PACKAGE);
    }

    private EditPersonDescriptor buildEditPersonDescriptor(ArgumentMultimap argMultimap) throws ParseException {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();

        parseNameIfPresent(argMultimap, descriptor);
        parsePhoneIfPresent(argMultimap, descriptor);
        parseEmailIfPresent(argMultimap, descriptor);
        parseAddressIfPresent(argMultimap, descriptor);
        parseSalaryIfPresent(argMultimap, descriptor);
        parseDateOfBirthIfPresent(argMultimap, descriptor);
        parseMaritalStatusIfPresent(argMultimap, descriptor);
        parseDependentsIfPresent(argMultimap, descriptor);
        parseOccupationIfPresent(argMultimap, descriptor);
        parseInsurancePackageIfPresent(argMultimap, descriptor);
        parseTagsIfPresent(argMultimap, descriptor);

        return descriptor;
    }

    private void parseNameIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_NAME).isPresent()) {
            desc.setName(ParserUtil.parseName(map.getValue(PREFIX_NAME).get()));
        }
    }

    private void parsePhoneIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_PHONE).isPresent()) {
            desc.setPhone(ParserUtil.parsePhone(map.getValue(PREFIX_PHONE).get()));
        }
    }

    private void parseEmailIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_EMAIL).isPresent()) {
            desc.setEmail(ParserUtil.parseEmail(map.getValue(PREFIX_EMAIL).get()));
        }
    }

    private void parseAddressIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_ADDRESS).isPresent()) {
            desc.setAddress(ParserUtil.parseAddress(map.getValue(PREFIX_ADDRESS).get()));
        }
    }

    private void parseSalaryIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_SALARY).isPresent()) {
            desc.setSalary(ParserUtil.parseSalary(map.getValue(PREFIX_SALARY).get()));
        }
    }

    private void parseDateOfBirthIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_DATE_OF_BIRTH).isPresent()) {
            desc.setDateOfBirth(ParserUtil.parseDateOfBirth(map.getValue(PREFIX_DATE_OF_BIRTH).get()));
        }
    }

    private void parseMaritalStatusIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_MARITAL_STATUS).isPresent()) {
            desc.setMaritalStatus(ParserUtil.parseMaritalStatus(map.getValue(PREFIX_MARITAL_STATUS).get()));
        }
    }

    private void parseDependentsIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_DEPENDENTS).isPresent()) {
            desc.setDependents(ParserUtil.parseDependents(map.getValue(PREFIX_DEPENDENTS).get()));
        }
    }

    private void parseOccupationIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_OCCUPATION).isPresent()) {
            desc.setOccupation(ParserUtil.parseOccupation(map.getValue(PREFIX_OCCUPATION).get()));
        }
    }

    private void parseInsurancePackageIfPresent(ArgumentMultimap map,
            EditPersonDescriptor desc) throws ParseException {
        if (map.getValue(PREFIX_INSURANCE_PACKAGE).isPresent()) {
            desc.setInsurancePackage(ParserUtil.parseInsurancePackage(map.getValue(PREFIX_INSURANCE_PACKAGE).get()));
        }
    }

    private void parseTagsIfPresent(ArgumentMultimap map, EditPersonDescriptor desc) throws ParseException {
        Optional<Set<Tag>> tagsOptional = parseTagsForEdit(map.getAllValues(PREFIX_TAG));
        tagsOptional.ifPresent(desc::setTags);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
