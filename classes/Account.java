package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Account {
    private final HashMap<String, String> users;
    private String filename;

    public Account() {
        users = new HashMap<>();
        this.filename = "users.txt";
    }

    public Map<String, String> readUsers() throws IOException {
        users.clear();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\|", 2);

                if (values.length == 2) {
                    users.put(values[0], values[1]);
                }
            }
        }
        return Collections.unmodifiableMap(users);
    }

    protected String getFilename() {
        return filename;
    }
}
