package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

// Creates user accounts and validates their account information.
public class CreateAccount extends Account {

    // Creates an account using the username as the display name.
    public void addUser(
            String username,
            String password
    ) throws IOException {
        addUser(username, username, password);
    }
    
    // Saves a new user with a securely hashed password.
    public void addUser(
            String name,
            String username,
            String password
    ) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(getFilename(), true))) {
            writer.println(
                    name + "|" + username + "|" + PasswordHasher.hash(password)
            );
        }
    }

    // Checks whether a username is already stored in the accounts file.
    public boolean usernameExists(String proposedUsername)
        throws IOException {
        Map<String, User> users = readUsers();
        return users.containsKey(proposedUsername);
    }

    // Checks that a name is present and safe for the file format.
    public boolean validateName(String name) {
        return name != null
                && !name.isBlank()
                && !name.contains("|");
    }

    // Checks that a username does not contain the file separator character.
    public boolean validateUsername(String username) {
        return username != null && !username.contains("|");
    }

    // Checks that a password meets the minimum length and format rules.
    public boolean validatePassword(String password) {
        return password != null
                && password.length() >= 6
                && !password.contains("|");
    }
}
