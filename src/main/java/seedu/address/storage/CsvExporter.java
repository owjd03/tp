package seedu.address.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.model.person.Person;

/**
 * A utility class to export Person data to a CSV file.
 */
public class CsvExporter {

    private static final String CSV_HEADER = "Name,Phone,Email,Address,Salary,Date of Birth,"
            + "Marital Status,Occupation,Dependents,Tags";

    /**
     * Exports a list of persons to a CSV file at the specified path.
     *
     * @param persons  The list of persons to export.
     * @param filePath The path of the file to write to.
     * @throws IOException If an error occurs during file writing.
     */
    public static void exportPersons(List<Person> persons, Path filePath) throws IOException {
        // Create parent directories if they don't exist
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Person person : persons) {
                writer.write(personToCsvRow(person));
                writer.newLine();
            }
        }
    }

    /**
     * Converts a Person object to a single CSV row string.
     *
     * @param person The person to convert.
     * @return A string representing a row in CSV format.
     */
    private static String personToCsvRow(Person person) {
        String tagsString = person.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(" | ")); // Use a separator for multiple tags

        // Build the row of data while handling nulls.
        return Stream.of(
                        person.getName(),
                        person.getPhone(),
                        person.getEmail(),
                        person.getAddress(),
                        person.getSalary(),
                        person.getDateOfBirth(),
                        person.getMaritalStatus(),
                        person.getOccupation(),
                        person.getDependents(),
                        tagsString
                )
                // Convert field to string, or empty if null
                .map(field -> field == null ? "" : field.toString())
                // Quote each field
                .map(CsvExporter::quoteCsvField)
                // Join with commas
                .collect(Collectors.joining(","));
    }

    /**
     * Encloses a field in double quotes if it contains a comma, a double quote, or a newline.
     * Double quotes within the field are escaped by another double quote.
     */
    private static String quoteCsvField(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
