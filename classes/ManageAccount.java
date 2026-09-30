package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ManageAccount extends Account {
    public boolean changeUsername(
            String username,
            String password,
            String newUsername
    ) throws IOException {
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

    public boolean changePassword(
            String username,
            String currentPassword,
            String newPassword
    ) throws IOException {
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

    public boolean deleteAccount(
            String username,
            String password
    ) throws IOException {
        Map<String, User> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, password)) {
            return false;
        }

        HashMap<String, User> updatedUsers = new HashMap<>(currentUsers);
        updatedUsers.remove(username);
        writeUsers(updatedUsers);
        return true;
    }

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

    private boolean passwordMatches(
            Map<String, User> users,
            String username,
            String password
    ) {
        User user = users.get(username);
        return user != null
                && PasswordHasher.matches(password, user.getPassword());
    }

    private boolean validUsername(String username) {
        return username != null
                && !username.isBlank()
                && !username.contains("|");
    }

    private boolean validPassword(String password) {
        return password != null
                && password.length() >= 6
                && !password.contains("|");
    }
}
