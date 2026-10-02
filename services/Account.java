package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Provides shared access to the user accounts file.
public class Account {
    private final HashMap<String, User> users;
    private String filename;

    // Creates an account manager that uses the default users file.
    public Account() {
        users = new HashMap<>();
        try {
            DataPaths.ensureDataDirectory();
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to create the data directory",
                    exception
            );
        }
        this.filename = DataPaths.usersFile().toString();
    }

    // Reads all valid user records from the accounts file.
    public Map<String, User> readUsers() throws IOException {
        users.clear();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\|", 3);

                if (values.length == 3) {
                    User user = new User(values[0], values[1], values[2]);
                    users.put(user.getUsername(), user);
                } else if (values.length == 2) {
                    // Support accounts created before names were saved.
                    User user = new User(values[0], values[0], values[1]);
                    users.put(user.getUsername(), user);
                }
            }
        }
        return Collections.unmodifiableMap(users);
    }

    // Provides subclasses with the file used to store user accounts.
    protected String getFilename() {
        return filename;
    }
}
