package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private final Model model =
            new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());
    private final Model expectedModel =
            new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());

    @Test
    public void equals() {
        Map<Prefix, String> firstKeywords = Collections.singletonMap(PREFIX_NAME, "first");
        Map<Prefix, String> secondKeywords = Collections.singletonMap(PREFIX_NAME, "second");
        Set<Tag> firstTags = Collections.singleton(new Tag("firstTag"));
        Set<Tag> secondTags = Collections.singleton(new Tag("secondTag"));

        // Predicates
        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(firstKeywords, firstTags);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondKeywords, secondTags);

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));

        // different keywords, same tags -> returns false
        PersonContainsKeywordsPredicate diffKeywordsPredicate =
                new PersonContainsKeywordsPredicate(secondKeywords, firstTags);
        assertFalse(filterFirstCommand.equals(new FilterCommand(diffKeywordsPredicate)));

        // same keywords, different tags -> returns false
        PersonContainsKeywordsPredicate diffTagsPredicate =
                new PersonContainsKeywordsPredicate(firstKeywords, secondTags);
        assertFalse(filterFirstCommand.equals(new FilterCommand(diffTagsPredicate)));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Map<Prefix, String> predicateMap = Collections.singletonMap(PREFIX_NAME, "NonExistentName");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(predicateMap, Collections.emptySet());
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleKeyword_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<Prefix, String> predicateMap = Collections.singletonMap(PREFIX_NAME, "Benson");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(predicateMap, Collections.emptySet());
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<Prefix, String> predicateMap = new HashMap<>();
        predicateMap.put(PREFIX_NAME, "Alice");
        predicateMap.put(PREFIX_PHONE, "94351253");
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(predicateMap, Collections.emptySet());
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        Set<Tag> tagSet = Collections.singleton(new Tag("friends"));
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), tagSet);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleTags_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Set<Tag> tagSet = new HashSet<>(Arrays.asList(new Tag("friends"), new Tag("owesMoney")));
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.emptyMap(), tagSet);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_keywordsAndTags_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        Map<Prefix, String> predicateMap = Collections.singletonMap(PREFIX_NAME, "Alice");
        Set<Tag> tagSet = Collections.singleton(new Tag("friends"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(predicateMap, tagSet);
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        Map<Prefix, String> predicateMap = Collections.singletonMap(PREFIX_NAME, "keyword");
        Set<Tag> tagSet = new HashSet<>(Arrays.asList(new Tag("tag1"), new Tag("tag2")));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(predicateMap, tagSet);
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
