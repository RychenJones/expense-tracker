package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CreateAccount extends Account {

    public void addUser(
            String username,
            String password
    ) throws IOException {
        addUser(username, username, password);
    }
    
    public void addUser(
            String name,
            String username,
            String password
    ) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(getFilename(), true))) {
            writer.println(name + "|" + username + "|" + password);
        }
    }

    public boolean usernameExists(String proposedUsername)
        throws IOException {
        Map<String, User> users = readUsers();
        return users.containsKey(proposedUsername);
    }

    public boolean validateName(String name) {
        return name != null
                && !name.isBlank()
                && !name.contains("|");
    }

    public boolean validateUsername(String username) {
        return username != null && !username.contains("|");
    }

    public boolean validatePassword(String password) {
        return password != null
                && password.length() >= 6
                && !password.contains("|");
    }
}
