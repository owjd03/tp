package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // Test with empty string
        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // Test with only the command word
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Test with invalid preamble (text before the first valid prefix)
        assertParseFailure(parser,
                "this is a test",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // Test with prefix but empty value
        assertParseFailure(parser,
                " " + PREFIX_NAME,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        assertParseFailure(parser,
                " " + PREFIX_NAME + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // Test with single keyword
        Map<Prefix, String> singleKeyword = new HashMap<>();
        singleKeyword.put(PREFIX_NAME, "Alpha");
        FilterCommand expectedFilterCommandSingle =
                new FilterCommand(new PersonContainsKeywordsPredicate(singleKeyword));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha", expectedFilterCommandSingle);

        // Test with multiple keywords
        Map<Prefix, String> multipleKeywords = new HashMap<>();
        multipleKeywords.put(PREFIX_NAME, "Alpha");
        multipleKeywords.put(PREFIX_ADDRESS, "Beta");
        FilterCommand expectedFilterCommandMultiple =
                new FilterCommand(new PersonContainsKeywordsPredicate(multipleKeywords));
        assertParseSuccess(parser,
                " " + PREFIX_NAME + "Alpha" + " " + PREFIX_ADDRESS + "Beta",
                expectedFilterCommandMultiple);
        // Test with repeated prefixes (last one is taken due to use of ArgumentMultiMap.getValue(Prefix))
        assertParseSuccess(parser,
                " " + PREFIX_ADDRESS + "fake" + " " + PREFIX_NAME + "Alpha" + " " + PREFIX_ADDRESS + "Beta",
                expectedFilterCommandMultiple);

        // Test with multiple keywords, including multi-word keyword
        Map<Prefix, String> multiWordKeywords = new HashMap<>();
        multiWordKeywords.put(PREFIX_NAME, "Alpha");
        multiWordKeywords.put(PREFIX_ADDRESS, "Kent Ridge");
        FilterCommand expectedFilterCommandMultiWord =
                new FilterCommand(new PersonContainsKeywordsPredicate(multiWordKeywords));
        assertParseSuccess(parser,
                " " + PREFIX_NAME + "Alpha" + " " + PREFIX_ADDRESS + "Kent Ridge",
                expectedFilterCommandMultiWord);
    }
}
