package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Defines the folder and file locations used for application data.
public final class DataPaths {
    private static final Path DATA_DIRECTORY = Path.of("data");

    // Prevents this path helper from being instantiated.
    private DataPaths() {
    }

    // Creates the data folder when it does not already exist.
    public static void ensureDataDirectory() throws IOException {
        Files.createDirectories(DATA_DIRECTORY);
    }

    // Returns the path used to store account records.
    public static Path usersFile() {
        return DATA_DIRECTORY.resolve("users.txt");
    }

    // Returns the path used to store one user's expenses.
    public static Path expensesFile(String username) {
        return DATA_DIRECTORY.resolve(username + ".txt");
    }
}
