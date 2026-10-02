package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// Provides operations for changing and deleting user accounts.
public class ManageAccount extends Account {
    // Changes a user's username after verifying the current password.
    public boolean changeUsername(
            String username,
            String password,
            String newUsername
    ) throws IOException {
        // Load the current accounts before making any changes.
        Map<String, User> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, password)
                || !validUsername(newUsername)
                || currentUsers.containsKey(newUsername)) {
            return false;
        }

        HashMap<String, User> updatedUsers = new HashMap<>(currentUsers);
        User currentUser = updatedUsers.get(username);
        updatedUsers.remove(username);
        updatedUsers.put(
                newUsername,
                new User(
                        currentUser.getName(),
                        newUsername,
                        PasswordHasher.hash(password)
                )
        );
        writeUsers(updatedUsers);
        return true;
    }

    // Replaces a user's password after verifying the current password.
    public boolean changePassword(
            String username,
            String currentPassword,
            String newPassword
    ) throws IOException {
        // Load the current accounts before making any changes.
        Map<String, User> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, currentPassword)
                || !validPassword(newPassword)) {
            return false;
        }

        HashMap<String, User> updatedUsers = new HashMap<>(currentUsers);
        User currentUser = updatedUsers.get(username);
        updatedUsers.put(
                username,
                new User(
                        currentUser.getName(),
                        username,
                        PasswordHasher.hash(newPassword)
                )
        );
        writeUsers(updatedUsers);
        return true;
    }

    // Deletes an account after verifying its username and password.
    public boolean deleteAccount(
            String username,
            String password
    ) throws IOException {
        // Load the current accounts before making any changes.
        Map<String, User> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, password)) {
            return false;
        }

        HashMap<String, User> updatedUsers = new HashMap<>(currentUsers);
        updatedUsers.remove(username);
        writeUsers(updatedUsers);
        return true;
    }

    // Writes all user records back to the users file.
    private void writeUsers(Map<String, User> users)
            throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(getFilename()))) {
            for (User user : users.values()) {
                writer.println(
                        user.getName() + "|"
                                + user.getUsername() + "|"
                                + user.getPassword()
                );
            }
        }
    }

    // Checks whether the supplied password matches the user's stored hash.
    private boolean passwordMatches(
            Map<String, User> users,
            String username,
            String password
    ) {
        User user = users.get(username);
        return user != null
                && PasswordHasher.matches(password, user.getPassword());
    }

    // Checks that a username can be safely stored in the file format.
    private boolean validUsername(String username) {
        return username != null
                && !username.isBlank()
                && !username.contains("|");
    }

    // Checks that a password meets the minimum storage requirements.
    private boolean validPassword(String password) {
        return password != null
                && password.length() >= 6
                && !password.contains("|");
    }
}
