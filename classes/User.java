package classes;

// Stores the information associated with one user account.
public class User {
    private String name;
    private String username;
    private String password;

    // Creates a user with a name, username, and stored password value.
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Returns the user's display name.
    public String getName() {
        return name;
    }

    // Returns the user's login username.
    public String getUsername() {
        return username;
    }

    // Returns the user's stored password value, which should be a hash.
    public String getPassword() {
        return password;
    }
}
