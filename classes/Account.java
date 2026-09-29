package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Account {
    private final HashMap<String, User> users;
    private String filename;

    public Account() {
        users = new HashMap<>();
        this.filename = "users.txt";
    }

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

    protected String getFilename() {
        return filename;
    }
}
