package classes;

import java.io.IOException;
import java.util.Map;

public class LoginAccount extends Account {
    public boolean passwordMatches(String username, String password)
            throws IOException {
        Map<String, String> users = readUsers();

        return users.containsKey(username)
                && users.get(username).equals(password);
    }
}
