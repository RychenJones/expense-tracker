import classes.CreateAccount;
import classes.Analytics;
import classes.Expense;
import classes.FileManager;
import classes.LoginAccount;
import classes.RecurringExpense;
import classes.RegulerExpense;
import classes.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

// Runs the command-line interface for the expense tracker.
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

    // Starts the application and displays the main account menu.
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

    // Collects login information and returns the authenticated user.
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

    // Displays the expense-management menu after a successful login.
    private static void postLoginMenu(Scanner scanner, User user) {
        ArrayList<Expense> expenses = new ArrayList<>();
        String expenseFilename = user.getUsername() + ".txt";
        FileManager fileManager = new FileManager(expenseFilename, expenses);
        Analytics analytics = new Analytics(expenses);

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
            System.out.println("3. View analytics");
            System.out.println("4. Delete an expense");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> recordExpense(scanner, expenses, fileManager);
                case "2" -> viewExpenses(expenses);
                case "3" -> viewAnalytics(analytics);
                case "4" -> deleteExpense(scanner, expenses, fileManager);
                case "5" -> {
                    System.out.println("Goodbye!");
                    loggedIn = false;
                }
                default -> System.out.println(
                        "Please choose 1, 2, 3, 4, or 5."
                );
            }
        }
    }

    // Creates and saves either a regular or recurring expense.
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

    // Collects input and creates a regular expense.
    private static Expense createSingleExpense(Scanner scanner) {
        System.out.println("\nRecord a single expense");
        String name = readRequired(scanner, "Expense name: ");
        double price = readPrice(scanner);
        String category = readCategory(scanner);
        LocalDate date = readDate(scanner);

        return new RegulerExpense(name, price, category, date);
    }

    // Collects input and creates a recurring expense.
    private static RecurringExpense createRecurringExpense(
            Scanner scanner
    ) {
        System.out.println("\nRecord a recurring expense");
        String name = readRequired(scanner, "Expense name: ");
        double price = readPrice(scanner);
        String category = readCategory(scanner);
        LocalDate date = readDate(scanner);
        String frequency = readFrequency(scanner);

        return new RecurringExpense(
                name,
                price,
                category,
                date,
                frequency
        );
    }

    // Displays all expenses currently loaded for the user.
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

    // Confirms and deletes an expense selected by the user.
    private static void deleteExpense(
            Scanner scanner,
            ArrayList<Expense> expenses,
            FileManager fileManager
    ) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses have been recorded yet.");
            return;
        }

        viewExpenses(expenses);
        System.out.print("Enter the number of the expense to delete: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            int index = choice - 1;

            if (index < 0 || index >= expenses.size()) {
                System.out.println("Please choose a valid expense number.");
                return;
            }

            Expense expense = expenses.get(index);
            System.out.print("Delete \"" + expense.getName()
                    + "\"? (y/n): ");
            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            fileManager.deleteExpense(index);
            System.out.println("Expense deleted successfully.");
        } catch (NumberFormatException exception) {
            System.out.println("Please enter a valid expense number.");
        } catch (IOException exception) {
            System.out.println("The expense could not be deleted.");
        }
    }

    // Displays spending totals, averages, and category breakdowns.
    private static void viewAnalytics(Analytics analytics) {
        System.out.println("\nYour analytics:");
        System.out.printf(
                "Total weekly spending: $%.2f%n",
                analytics.getTotalWeeklySpending()
        );
        System.out.printf(
                "Total monthly spending: $%.2f%n",
                analytics.getTotalMonthlySpending()
        );
        System.out.printf(
                "Average weekly spending: $%.2f%n",
                analytics.getAverageWeeklySpending()
        );
        System.out.printf(
                "Average monthly spending: $%.2f%n",
                analytics.getAverageMonthlySpending()
        );

        System.out.println("\nWeekly spending by category:");
        printSpendingByCategory(analytics.getWeeklySpendingByCategory());

        System.out.println("\nMonthly spending by category:");
        printSpendingByCategory(analytics.getMonthlySpendingByCategory());

        System.out.println("\nCategory percentages:");
        for (Map.Entry<String, Double> entry
                : analytics.getCategoryPercentages().entrySet()) {
            System.out.printf(
                    "  %s: %.2f%%%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    // Prints a category-to-total-spending map.
    private static void printSpendingByCategory(
            Map<String, Double> spendingByCategory
    ) {
        if (spendingByCategory.isEmpty()) {
            System.out.println("  No spending recorded.");
            return;
        }

        for (Map.Entry<String, Double> entry
                : spendingByCategory.entrySet()) {
            System.out.printf(
                    "  %s: $%.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    // Reads and validates a non-negative expense price.
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

    // Displays the category choices and returns the selected category.
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

    // Reads an optional date, using today's date when the user presses Enter.
    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            System.out.print(
                    "Date (YYYY-MM-DD, or press Enter for today): "
            );
            String input = scanner.nextLine().trim();

            if (input.isBlank()) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(input);
            } catch (java.time.format.DateTimeParseException exception) {
                System.out.println(
                        "Please enter a date in YYYY-MM-DD format."
                );
            }
        }
    }

    // Displays frequency choices and returns the selected frequency.
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

    // Collects validated account information and creates a new account.
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

    // Reads a required text value and rejects blank input.
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
