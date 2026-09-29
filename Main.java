import classes.CreateAccount;
import classes.LoginAccount;
import classes.User;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CreateAccount accountCreator = new CreateAccount();
        LoginAccount loginAccount = new LoginAccount();

        System.out.println("Welcome to the Expense Tracker!");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Log in");
            System.out.println("2. Create an account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    if (login(scanner, loginAccount)) {
                        running = false;
                    }
                }
                case "2" -> {
                    createAccount(scanner, accountCreator);
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default ->
                    System.out.println("Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    private static boolean login(
            Scanner scanner,
            LoginAccount loginAccount
    ) {
        System.out.println("\nLog in");

        String username = readRequired(scanner, "Enter your username: ");
        String password = readRequired(scanner, "Enter your password: ");

        try {
            User user = loginAccount.authenticate(username, password);

            if (user != null) {
                System.out.println("Login successful. Welcome back, "
                        + user.getName() + "!");
                return true;
            }

            System.out.println("Incorrect username or password.");
        } catch (IOException exception) {
            System.out.println("Unable to access existing accounts.");
        }

        return false;
    }

    private static void createAccount(
            Scanner scanner,
            CreateAccount accountCreator
    ) {
        System.out.println("\nCreate an account");

        String name = "";
        boolean validName = false;

        while (!validName) {
            name = readRequired(scanner, "Enter your name: ");
            validName = accountCreator.validateName(name);

            if (!validName) {
                System.out.println(
                        "Name cannot be blank or contain '|'."
                );
            }
        }

        String username = "";
        boolean validUsername = false;

        while (!validUsername) {
            username = readRequired(scanner, "Enter a username: ");

            try {
                if (!accountCreator.validateUsername(username)) {
                    System.out.println("Username cannot contain '|'.");
                } else if (accountCreator.usernameExists(username)) {
                    System.out.println("That username is already in use.");
                } else {
                    validUsername = true;
                }
            } catch (IOException exception) {
                System.out.println("Unable to check existing accounts.");
                return;
            }
        }

        String password = "";
        boolean validPassword = false;

        while (!validPassword) {
            password = readRequired(
                    scanner,
                    "Enter a password (at least 6 characters): "
            );

            validPassword = accountCreator.validatePassword(password);

            if (!validPassword) {
                System.out.println(
                        "Password must be at least 6 characters "
                                + "and cannot contain '|'."
                );
            }
        }

        User user = new User(name, username, password);

        try {
            accountCreator.addUser(
                    user.getName(),
                    user.getUsername(),
                    user.getPassword()
            );
            System.out.println(
                    "Account created successfully. Welcome, "
                            + user.getName() + "!"
            );
        } catch (IOException exception) {
            System.out.println("The account could not be created.");
        }
    }

    private static String readRequired(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be blank.");
        }
    }
}
