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

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS,
                                PREFIX_DEPENDENTS, PREFIX_OCCUPATION, PREFIX_INSURANCE_PACKAGE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_INSURANCE_PACKAGE) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_SALARY, PREFIX_DATE_OF_BIRTH, PREFIX_MARITAL_STATUS, PREFIX_DEPENDENTS, PREFIX_OCCUPATION,
                PREFIX_INSURANCE_PACKAGE);


        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        InsurancePackage insurancePackage = ParserUtil.parseInsurancePackage(argMultimap
                .getValue(PREFIX_INSURANCE_PACKAGE).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Salary salary = Salary.createUnspecified();
        DateOfBirth dateOfBirth = DateOfBirth.createUnspecified();
        MaritalStatus maritalStatus = MaritalStatus.createUnspecified();
        Occupation occupation = Occupation.createUnspecified();
        Dependents numberOfDependents = Dependents.createUnspecified();


        if (argMultimap.getValue(PREFIX_SALARY).isPresent()) {
            salary = ParserUtil.parseSalary(argMultimap.getValue(PREFIX_SALARY).get());
        }
        if (argMultimap.getValue(PREFIX_DATE_OF_BIRTH).isPresent()) {
            dateOfBirth = ParserUtil.parseDateOfBirth(argMultimap.getValue(PREFIX_DATE_OF_BIRTH).get());
        }
        if (argMultimap.getValue(PREFIX_MARITAL_STATUS).isPresent()) {
            maritalStatus = ParserUtil.parseMaritalStatus(argMultimap.getValue(PREFIX_MARITAL_STATUS).get());
        }
        if (argMultimap.getValue(PREFIX_OCCUPATION).isPresent()) {
            occupation = ParserUtil.parseOccupation(argMultimap.getValue(PREFIX_OCCUPATION).get());
        }
        if (argMultimap.getValue(PREFIX_DEPENDENTS).isPresent()) {
            numberOfDependents = ParserUtil.parseDependents(argMultimap.getValue(PREFIX_DEPENDENTS).get());
        }

        Person person = new Person(name, phone, email, address, salary, dateOfBirth, maritalStatus,
                occupation, numberOfDependents, insurancePackage, tagList);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
