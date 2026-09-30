package classes;

import java.io.IOException;
import java.util.Map;

// Provides methods for verifying user login information.
public class LoginAccount extends Account {

    // Returns true when the username and password belong to an account.
    public boolean passwordMatches(String username, String password)
            throws IOException {
        return authenticate(username, password) != null;
    }

    // Finds and returns the authenticated user, or null if authentication fails.
    public User authenticate(String username, String password)
            throws IOException {
        // Read the accounts and find the account for the supplied username.
        Map<String, User> users = readUsers();
        User user = users.get(username);

        // Compare the entered password with the user's stored password hash.
        if (user != null
                && PasswordHasher.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
