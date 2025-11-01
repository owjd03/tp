package seedu.address.logic.parser.filter;

import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * Represents a parser for a specific filter prefix.
 * Classes implementing this interface are responsible for parsing the args
 * associated with a prefix and providing a way to apply the filter.
 */
public interface FilterPrefixParser {

    /**
     * Returns the prefix this parser handles.
     */
    Prefix getPrefix();

    /**
     * Parses the string args associated with the prefix.
     *
     * @param args The trimmed string args for the prefix. Guaranteed to be non-empty.
     * @throws ParseException If the args is invalid for this prefix type.
     */
    void parse(String args) throws ParseException;

    /**
     * Tests if a given person matches the filter criterion defined by this parser.
     *
     * @param person The person to test.
     * @return true if the person matches, false otherwise.
     */
    boolean test(Person person);

    /**
     * Returns a string representation of the filter arguments this parser holds, formatted for display.
     * The format should be the prefix followed by the keyword(s). For parsers that handle
     * multiple values for the same prefix (like tags), each value should be individually prefixed.
     *
     * @return A formatted string representing the filter arguments for this parser.
     */
    String getArg();
}
