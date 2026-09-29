package classes;

import java.io.IOException;
import java.util.Map;

public class LoginAccount extends Account {

    public boolean passwordMatches(String username, String password)
            throws IOException {
        return authenticate(username, password) != null;
    }

    public User authenticate(String username, String password)
            throws IOException {
        Map<String, User> users = readUsers();
        User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}
