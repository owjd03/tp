package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // Test with empty string
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);

        // Test with only the command word
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_preamble_throwsParseException() {
        // Preamble present
        assertParseFailure(parser, "some random string", MESSAGE_INVALID_FORMAT);

        // Preamble present before keywords
        assertParseFailure(parser, "some random string " + PREFIX_NAME + "Alice", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_noKeywords_throwsParseException() {
        // Prefix present but no keyword
        assertParseFailure(parser, " " + PREFIX_NAME, MESSAGE_INVALID_FORMAT);

        // Prefix with only whitespace as keyword
        assertParseFailure(parser, " " + PREFIX_NAME + " ", MESSAGE_INVALID_FORMAT);

        // Tag prefix present but no keyword
        assertParseFailure(parser, " " + PREFIX_TAG, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_noKeywordsAndNoTags_throwsParseException() {
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        // Duplicate name prefix
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob",
                "Multiple values specified for the following single-valued field(s): n/");

        // Duplicate address prefix
        assertParseFailure(parser, " " + PREFIX_ADDRESS + "Main St " + PREFIX_ADDRESS + "North St",
                "Multiple values specified for the following single-valued field(s): a/");

        // Multiple duplicate prefixes
        assertParseFailure(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + "Main St "
                        + PREFIX_NAME + "Bob " + PREFIX_ADDRESS + "North St",
                "Multiple values specified for the following single-valued field(s): n/ a/");
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // Single keyword
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alpha");
        FilterCommand expectedCommand =
                new FilterCommand(new PersonContainsKeywordsPredicate(keywords, Collections.emptySet()));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha", expectedCommand);

        // Multiple keywords
        keywords.put(PREFIX_ADDRESS, "Beta");
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(keywords, Collections.emptySet()));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha " + PREFIX_ADDRESS + "Beta", expectedCommand);

        // Multi-word keyword
        keywords.put(PREFIX_ADDRESS, "Kent Ridge");
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(keywords, Collections.emptySet()));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alpha " + PREFIX_ADDRESS + "Kent Ridge", expectedCommand);
    }

    @Test
    public void parse_validTags_returnsFilterCommand() {
        // Single tag
        Set<Tag> tags = new HashSet<>(Collections.singletonList(new Tag("friend")));
        FilterCommand expectedCommand =
                new FilterCommand(new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend", expectedCommand);

        // Multiple tags
        tags = new HashSet<>(Arrays.asList(new Tag("friend"), new Tag("colleague")));
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "colleague", expectedCommand);

        // Duplicate tags - should be treated as one
        tags = new HashSet<>(Collections.singletonList(new Tag("friend")));
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(Collections.emptyMap(), tags));
        assertParseSuccess(parser, " " + PREFIX_TAG + "friend " + PREFIX_TAG + "friend", expectedCommand);
    }

    @Test
    public void parse_validKeywordsAndTags_returnsFilterCommand() {
        // One keyword, one tag
        Map<Prefix, String> keywords = new HashMap<>();
        keywords.put(PREFIX_NAME, "Alice");
        Set<Tag> tags = new HashSet<>(Collections.singletonList(new Tag("friend")));
        FilterCommand expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(keywords, tags));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_TAG + "friend", expectedCommand);

        // Multiple keywords, multiple tags
        keywords.put(PREFIX_ADDRESS, "Kent Ridge");
        tags.add(new Tag("colleague"));
        expectedCommand = new FilterCommand(new PersonContainsKeywordsPredicate(keywords, tags));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + "Kent Ridge "
                + PREFIX_TAG + "friend " + PREFIX_TAG + "colleague", expectedCommand);
    }
}
