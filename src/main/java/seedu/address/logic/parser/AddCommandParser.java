package seedu.address.logic.parser;

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

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.insurance.InsurancePackage;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Dependents;
import seedu.address.model.person.Email;
import seedu.address.model.person.MaritalStatus;
import seedu.address.model.person.Name;
import seedu.address.model.person.Occupation;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Salary;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final Prefix[] ALL_PREFIXES = {
        PREFIX_ADDRESS, PREFIX_DATE_OF_BIRTH, PREFIX_DEPENDENTS, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE,
        PREFIX_MARITAL_STATUS, PREFIX_NAME, PREFIX_OCCUPATION, PREFIX_PHONE, PREFIX_SALARY, PREFIX_TAG
    };

    private static final Prefix[] MANDATORY_PREFIXES = new Prefix[] {
        PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_INSURANCE_PACKAGE
    };

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = tokenizeAndValidateArgs(args);

        Person person = createPersonFromArgs(argMultimap);

        return new AddCommand(person);
    }

    /**
     * Tokenizes the {@code args} and validates presence of compulsory prefixes,
     * lack of preamble and no duplicate prefixes.
     *
     * @param args The raw argument string.
     * @return A validated ArgumentMultimap.
     * @throws ParseException if validation fails.
     */
    private ArgumentMultimap tokenizeAndValidateArgs(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, ALL_PREFIXES);

        if (!arePrefixesPresent(argMultimap, MANDATORY_PREFIXES) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Prefix[] singleValuedPrefixes = Stream.of(ALL_PREFIXES)
                .filter(prefix -> !prefix.equals(PREFIX_TAG))
                .toArray(Prefix[]::new);

        argMultimap.verifyNoDuplicatePrefixesFor(singleValuedPrefixes);

        return argMultimap;
    }

    /**
     * Creates a {@link Person} object by parsing compulsory and optional fields
     * from the {@link ArgumentMultimap}.
     *
     * @param argMultimap The validated map of arguments.
     * @return A fully constructed Person.
     * @throws ParseException if any field parsing fails.
     */
    private Person createPersonFromArgs(ArgumentMultimap argMultimap) throws ParseException {
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        InsurancePackage insurancePackage = ParserUtil.parseInsurancePackage(argMultimap
                .getValue(PREFIX_INSURANCE_PACKAGE).get());

        Salary salary = parseOptionalSalary(argMultimap);
        DateOfBirth dateOfBirth = parseOptionalDateOfBirth(argMultimap);
        MaritalStatus maritalStatus = parseOptionalMaritalStatus(argMultimap);
        Occupation occupation = parseOptionalOccupation(argMultimap);
        Dependents numberOfDependents = parseOptionalDependents(argMultimap);
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Person(name, phone, email, address, salary, dateOfBirth, maritalStatus,
                occupation, numberOfDependents, insurancePackage, tagList);
    }

    private Salary parseOptionalSalary(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_SALARY).isPresent()) {
            return ParserUtil.parseSalary(argMultimap.getValue(PREFIX_SALARY).get());
        }
        return Salary.createUnspecified();
    }

    private DateOfBirth parseOptionalDateOfBirth(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_DATE_OF_BIRTH).isPresent()) {
            return ParserUtil.parseDateOfBirth(argMultimap.getValue(PREFIX_DATE_OF_BIRTH).get());
        }
        return DateOfBirth.createUnspecified();
    }

    private MaritalStatus parseOptionalMaritalStatus(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_MARITAL_STATUS).isPresent()) {
            return ParserUtil.parseMaritalStatus(argMultimap.getValue(PREFIX_MARITAL_STATUS).get());
        }
        return MaritalStatus.createUnspecified();
    }

    private Occupation parseOptionalOccupation(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_OCCUPATION).isPresent()) {
            return ParserUtil.parseOccupation(argMultimap.getValue(PREFIX_OCCUPATION).get());
        }
        return Occupation.createUnspecified();
    }

    private Dependents parseOptionalDependents(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getValue(PREFIX_DEPENDENTS).isPresent()) {
            return ParserUtil.parseDependents(argMultimap.getValue(PREFIX_DEPENDENTS).get());
        }
        return Dependents.createUnspecified();
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
