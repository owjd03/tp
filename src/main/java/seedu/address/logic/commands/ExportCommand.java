package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.CsvExporter;

/**
 * Exports the address book data to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the address book to a CSV file."
            + "You can specify a valid file path and it must end with .csv. "
            + "If no file path is specified, it will be saved to data/addressbook.csv.\n"
            + "Parameters: FILE_PATH\n"
            + "Example (Windows): " + COMMAND_WORD + " C:\\Users\\User\\Downloads\\addressbook.csv\n"
            + "Example (macOS/Linux): " + COMMAND_WORD + " ~/Downloads/addressbook.csv";

    public static final String MESSAGE_SUCCESS = "Address book has been exported to %1$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Error occurred during exporting: %1$s";
    public static final String MESSAGE_IS_DIRECTORY = "The specified path is a directory, please specify a file path.";

    // Default file path for the export.
    private final Path filePath;

    /**
     * Creates an ExportCommand to export the address book to the default location.
     */
    public ExportCommand() {
        // Default export path is data/addressbook.csv
        this.filePath = Paths.get("data", "addressbook.csv");
    }

    /**
     * Creates an ExportCommand to export the address book to the specified location.
     *
     * @param filePath The path to export the file to.
     */
    public ExportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    /**
     * Executes the export command to write the address book data to a CSV file.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (Files.isDirectory(filePath)) {
            throw new CommandException(MESSAGE_IS_DIRECTORY);
        }

        List<Person> personList = model.getAddressBook().getPersonList();

        try {
            CsvExporter.exportPersons(personList, filePath);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE, e.getMessage()));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, filePath.toString()));
    }

    /**
     * Checks if two ExportCommand objects are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        return filePath.equals(((ExportCommand) other).filePath);
    }
}
