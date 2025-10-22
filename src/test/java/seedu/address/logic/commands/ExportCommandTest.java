package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalInsurancePackages.getTypicalInsuranceCatalog;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ExportCommand.
 */
public class ExportCommandTest {

    @TempDir
    public Path testFolder;

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalInsuranceCatalog(), new UserPrefs());

    @Test
    public void execute_validPerson_success() throws IOException {
        // 1. Define the test file path within the temporary directory
        Path exportFilePath = testFolder.resolve("addressbook.csv");

        // 2. Create the command with the specific file path
        ExportCommand exportCommand = new ExportCommand(exportFilePath);

        // 3. Define the expected success message
        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, exportFilePath.toString());

        // 4. The model is not expected to change
        Model expectedModel = new ModelManager(model.getAddressBook(), model.getInsuranceCatalog(), new UserPrefs());

        // 5. Assert that the command executes successfully
        assertCommandSuccess(exportCommand, model, expectedMessage, expectedModel);

        // 6. Verify the content of the created file
        List<String> lines = Files.readAllLines(exportFilePath);

        // Check header
        assertEquals("Name,Phone,Email,Address,Salary,Date of Birth,Marital Status,Occupation,Dependents,"
                + "Insurance Package,Tags",
                lines.get(0));

        // Check number of persons exported (header + 7 typical persons)
        assertEquals(1 + model.getFilteredPersonList().size(), lines.size());

        // Check a specific person's data (e.g., the first person, Alice Pauline)
        String expectedAliceCsv = "Alice Pauline,94351253,alice@example.com,"
                + "\"123, Jurong West Ave 6, #08-111\",\"$5,000.00\",1999-01-01,Single,Engineer,"
                + "0,Gold,friends";
        assertEquals(expectedAliceCsv, lines.get(1));
    }

    @Test
    public void execute_ioException_throwsCommandException() throws IOException {
        // 1. Create a file where a directory is expected, to trigger an IOException
        Path directoryAsFile = testFolder.resolve("directoryAsFile");
        Files.createFile(directoryAsFile);

        // 2. Define a path to a file inside the read-only directory
        Path exportFilePath = directoryAsFile.resolve("cannot_write.csv");

        // 3. Create the command
        ExportCommand exportCommand = new ExportCommand(exportFilePath);

        // 4. Assert that the command fails with the expected error message
        String expectedMessagePrefix = String.format(ExportCommand.MESSAGE_EXPORT_FAILURE, "").trim();
        CommandException thrown = assertThrows(CommandException.class, () -> exportCommand.execute(model));
        assertTrue(thrown.getMessage().startsWith(expectedMessagePrefix));
    }
}
