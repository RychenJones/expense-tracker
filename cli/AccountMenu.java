package cli;

import classes.CreateAccount;
import classes.LoginAccount;
import classes.User;

import java.io.IOException;
import java.util.Scanner;

// Handles account creation and login prompts.
public class AccountMenu {
    private final Scanner scanner;
    private final CreateAccount accountCreator;
    private final LoginAccount loginAccount;

    // Creates an account menu using the shared console and account services.
    public AccountMenu(
            Scanner scanner,
            CreateAccount accountCreator,
            LoginAccount loginAccount
    ) {
        this.scanner = scanner;
        this.accountCreator = accountCreator;
        this.loginAccount = loginAccount;
    }

    // Displays the account menu. Returns null when the user chooses to exit.
    public User show() {
        while (true) {
            System.out.println();
            System.out.println("1. Log in");
            System.out.println("2. Create an account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    User user = login();
                    if (user != null) {
                        return user;
                    }
                }
                case "2" -> createAccount();
                case "3" -> {
                    System.out.println("Goodbye!");
                    return null;
                }
                default -> System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    // Collects login information and returns the authenticated user.
    private User login() {
        System.out.println("\nLog in");
        String username = ConsoleInput.readRequired(
                scanner, "Enter your username: "
        );
        String password = ConsoleInput.readRequired(
                scanner, "Enter your password: "
        );

        try {
            User user = loginAccount.authenticate(username, password);
            if (user != null) {
                System.out.println("Login successful. Welcome back, "
                        + user.getName() + "!");
                return user;
            }
            System.out.println("Incorrect username or password.");
        } catch (IOException exception) {
            System.out.println("Unable to access existing accounts.");
        }
        return null;
    }

    // Collects validated account information and creates a new account.
    private void createAccount() {
        System.out.println("\nCreate an account");

        String name;
        while (true) {
            name = ConsoleInput.readRequired(scanner, "Enter your name: ");
            if (accountCreator.validateName(name)) {
                break;
            }
            System.out.println("Name cannot be blank or contain '|'.");
        }

        String username;
        while (true) {
            username = ConsoleInput.readRequired(
                    scanner, "Enter a username: "
            );
            try {
                if (!accountCreator.validateUsername(username)) {
                    System.out.println("Username cannot contain '|'.");
                } else if (accountCreator.usernameExists(username)) {
                    System.out.println("That username is already in use.");
                } else {
                    break;
                }
            } catch (IOException exception) {
                System.out.println("Unable to check existing accounts.");
                return;
            }
        }

        String password;
        while (true) {
            password = ConsoleInput.readRequired(
                    scanner,
                    "Enter a password (at least 6 characters): "
            );
            if (accountCreator.validatePassword(password)) {
                break;
            }
            System.out.println("Password must be at least 6 characters "
                    + "and cannot contain '|'.");
        }

        User user = new User(name, username, password);
        try {
            accountCreator.addUser(
                    user.getName(), user.getUsername(), user.getPassword()
            );
            System.out.println("Account created successfully. Welcome, "
                    + user.getName() + "!");
        } catch (IOException exception) {
            System.out.println("The account could not be created.");
        }
    }
}
