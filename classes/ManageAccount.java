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
        Map<String, String> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, password)
                || !validUsername(newUsername)
                || currentUsers.containsKey(newUsername)) {
            return false;
        }

        HashMap<String, String> updatedUsers = new HashMap<>(currentUsers);
        updatedUsers.remove(username);
        updatedUsers.put(newUsername, password);
        writeUsers(updatedUsers);
        return true;
    }

    public boolean changePassword(
            String username,
            String currentPassword,
            String newPassword
    ) throws IOException {
        Map<String, String> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, currentPassword)
                || !validPassword(newPassword)) {
            return false;
        }

        HashMap<String, String> updatedUsers = new HashMap<>(currentUsers);
        updatedUsers.put(username, newPassword);
        writeUsers(updatedUsers);
        return true;
    }

    public boolean deleteAccount(
            String username,
            String password
    ) throws IOException {
        Map<String, String> currentUsers = readUsers();
        if (!passwordMatches(currentUsers, username, password)) {
            return false;
        }

        HashMap<String, String> updatedUsers = new HashMap<>(currentUsers);
        updatedUsers.remove(username);
        writeUsers(updatedUsers);
        return true;
    }

    private void writeUsers(Map<String, String> users)
            throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(getFilename()))) {
            for (Map.Entry<String, String> user : users.entrySet()) {
                writer.println(user.getKey() + "|" + user.getValue());
            }
        }
    }

    private boolean passwordMatches(
            Map<String, String> users,
            String username,
            String password
    ) {
        return users.containsKey(username)
                && users.get(username).equals(password);
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
