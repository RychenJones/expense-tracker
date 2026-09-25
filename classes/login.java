package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class login {
    private String username;
    private String password;
    private HashMap<String, String> users;

    public login(String username, String password) {
        this.username = username;
        this.password = password;
        users = new HashMap<>();
    }

    public void readUsers(String filename) throws IOException {
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
    }
}
