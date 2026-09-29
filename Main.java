import classes.CreateAccount;
import classes.Expense;
import classes.FileManager;
import classes.LoginAccount;
import classes.RecurringExpense;
import classes.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String[] CATEGORIES = {
        "Food",
        "Transportation",
        "Housing",
        "Utilities",
        "Entertainment",
        "Health",
        "Shopping",
        "Other"
    };

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
                    User user = login(scanner, loginAccount);
                    if (user != null) {
                        postLoginMenu(scanner, user);
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

    private static User login(
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
                return user;
            }

            System.out.println("Incorrect username or password.");
        } catch (IOException exception) {
            System.out.println("Unable to access existing accounts.");
        }

        return null;
    }

    private static void postLoginMenu(Scanner scanner, User user) {
        ArrayList<Expense> expenses = new ArrayList<>();
        String expenseFilename = user.getUsername() + ".txt";
        FileManager fileManager = new FileManager(expenseFilename, expenses);

        if (new File(expenseFilename).exists()) {
            try {
                fileManager.read();
            } catch (IOException exception) {
                System.out.println("Unable to load your saved expenses.");
            }
        }

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Record an expense");
            System.out.println("2. View expenses");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> recordExpense(scanner, expenses, fileManager);
                case "2" -> viewExpenses(expenses);
                case "3" -> {
                    System.out.println("Goodbye!");
                    loggedIn = false;
                }
                default -> System.out.println(
                        "Please choose 1, 2, or 3."
                );
            }
        }
    }

    private static void recordExpense(
            Scanner scanner,
            ArrayList<Expense> expenses,
            FileManager fileManager
    ) {
        System.out.println("\nWhat type of expense would you like to record?");
        System.out.println("1. Single expense");
        System.out.println("2. Recurring expense");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            expenses.add(createSingleExpense(scanner));
        } else if (choice.equals("2")) {
            expenses.add(createRecurringExpense(scanner));
        } else {
            System.out.println("Please choose 1 or 2.");
            return;
        }

        try {
            fileManager.write();
            System.out.println("Expense recorded successfully.");
        } catch (IOException exception) {
            expenses.remove(expenses.size() - 1);
            System.out.println("The expense could not be saved.");
        }
    }

    private static Expense createSingleExpense(Scanner scanner) {
        System.out.println("\nRecord a single expense");
        String name = readRequired(scanner, "Expense name: ");
        double price = readPrice(scanner);
        String category = readCategory(scanner);

        return new Expense(name, price, category);
    }

    private static RecurringExpense createRecurringExpense(
            Scanner scanner
    ) {
        System.out.println("\nRecord a recurring expense");
        String name = readRequired(scanner, "Expense name: ");
        double price = readPrice(scanner);
        String category = readCategory(scanner);
        String frequency = readFrequency(scanner);

        return new RecurringExpense(name, price, category, frequency);
    }

    private static void viewExpenses(ArrayList<Expense> expenses) {
        System.out.println("\nYour expenses:");

        if (expenses.isEmpty()) {
            System.out.println("No expenses have been recorded yet.");
            return;
        }

        for (int index = 0; index < expenses.size(); index++) {
            System.out.println((index + 1) + ". " + expenses.get(index));
        }
    }

    private static double readPrice(Scanner scanner) {
        while (true) {
            System.out.print("Price: ");
            String input = scanner.nextLine().trim();

            try {
                double price = Double.parseDouble(input);
                if (price >= 0 && Double.isFinite(price)) {
                    return price;
                }
            } catch (NumberFormatException exception) {
                // Print the validation message below.
            }

            System.out.println("Enter a valid non-negative price.");
        }
    }

    private static String readCategory(Scanner scanner) {
        while (true) {
            System.out.println("Category:");
            for (int index = 0; index < CATEGORIES.length; index++) {
                System.out.println((index + 1) + ". " + CATEGORIES[index]);
            }
            System.out.print("Choose a category: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= CATEGORIES.length) {
                    return CATEGORIES[choice - 1];
                }
            } catch (NumberFormatException exception) {
                // Print the validation message below.
            }

            System.out.println("Please choose a category number from the list.");
        }
    }

    private static String readFrequency(Scanner scanner) {
        while (true) {
            System.out.println("Frequency:");
            System.out.println("1. Daily");
            System.out.println("2. Weekly");
            System.out.println("3. Monthly");
            System.out.println("4. Yearly");
            System.out.print("Choose a frequency: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    return RecurringExpense.DAILY;
                }
                case "2" -> {
                    return RecurringExpense.WEEKLY;
                }
                case "3" -> {
                    return RecurringExpense.MONTHLY;
                }
                case "4" -> {
                    return RecurringExpense.YEARLY;
                }
                default -> {
                    System.out.println(
                            "Please choose a frequency number from the list."
                    );
                }
            }
        }
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