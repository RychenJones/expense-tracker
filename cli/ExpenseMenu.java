package cli;

import classes.Analytics;
import classes.Expense;
import classes.FileManager;
import classes.RecurringExpense;
import classes.RegulerExpense;
import classes.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

// Handles expense actions for one logged-in user.
public class ExpenseMenu {
    private final Scanner scanner;
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final FileManager fileManager;
    private final Analytics analytics;

    // Creates an expense menu and loads the user's saved expenses.
    public ExpenseMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        String expenseFilename = user.getUsername() + ".txt";
        fileManager = new FileManager(expenseFilename, expenses);
        analytics = new Analytics(expenses);
        loadExpenses(expenseFilename);
    }

    // Displays the expense-management menu after a successful login.
    public void show() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Record an expense");
            System.out.println("2. View expenses");
            System.out.println("3. View analytics");
            System.out.println("4. Delete an expense");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> recordExpense();
                case "2" -> viewExpenses();
                case "3" -> viewAnalytics();
                case "4" -> deleteExpense();
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

    // Loads saved expenses when the user's expense file exists.
    private void loadExpenses(String filename) {
        if (new File(filename).exists()) {
            try {
                fileManager.read();
            } catch (IOException exception) {
                System.out.println("Unable to load your saved expenses.");
            }
        }
    }

    // Collects and saves either a regular or recurring expense.
    private void recordExpense() {
        System.out.println("\nWhat type of expense would you like to record?");
        System.out.println("1. Single expense");
        System.out.println("2. Recurring expense");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            expenses.add(createSingleExpense());
        } else if (choice.equals("2")) {
            expenses.add(createRecurringExpense());
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
    private Expense createSingleExpense() {
        System.out.println("\nRecord a single expense");
        String name = ConsoleInput.readRequired(scanner, "Expense name: ");
        double price = ConsoleInput.readPrice(scanner);
        String category = ConsoleInput.readCategory(scanner);
        LocalDate date = ConsoleInput.readDate(scanner);
        return new RegulerExpense(name, price, category, date);
    }

    // Collects input and creates a recurring expense.
    private RecurringExpense createRecurringExpense() {
        System.out.println("\nRecord a recurring expense");
        String name = ConsoleInput.readRequired(scanner, "Expense name: ");
        double price = ConsoleInput.readPrice(scanner);
        String category = ConsoleInput.readCategory(scanner);
        LocalDate date = ConsoleInput.readDate(scanner);
        String frequency = ConsoleInput.readFrequency(scanner);
        return new RecurringExpense(name, price, category, date, frequency);
    }

    // Displays all expenses currently loaded for the user.
    private void viewExpenses() {
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
    private void deleteExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses have been recorded yet.");
            return;
        }

        viewExpenses();
        System.out.print("Enter the number of the expense to delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= expenses.size()) {
                System.out.println("Please choose a valid expense number.");
                return;
            }

            Expense expense = expenses.get(index);
            System.out.print("Delete \"" + expense.getName() + "\"? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
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
    private void viewAnalytics() {
        System.out.println("\nYour analytics:");
        System.out.printf("Total weekly spending: $%.2f%n",
                analytics.getTotalWeeklySpending());
        System.out.printf("Total monthly spending: $%.2f%n",
                analytics.getTotalMonthlySpending());
        System.out.printf("Average weekly spending: $%.2f%n",
                analytics.getAverageWeeklySpending());
        System.out.printf("Average monthly spending: $%.2f%n",
                analytics.getAverageMonthlySpending());

        System.out.println("\nWeekly spending by category:");
        printSpendingByCategory(analytics.getWeeklySpendingByCategory());
        System.out.println("\nMonthly spending by category:");
        printSpendingByCategory(analytics.getMonthlySpendingByCategory());
        System.out.println("\nCategory percentages:");
        for (Map.Entry<String, Double> entry
                : analytics.getCategoryPercentages().entrySet()) {
            System.out.printf("  %s: %.2f%%%n",
                    entry.getKey(), entry.getValue());
        }
    }

    // Prints a category-to-total-spending map.
    private void printSpendingByCategory(Map<String, Double> spending) {
        if (spending.isEmpty()) {
            System.out.println("  No spending recorded.");
            return;
        }
        for (Map.Entry<String, Double> entry : spending.entrySet()) {
            System.out.printf("  %s: $%.2f%n",
                    entry.getKey(), entry.getValue());
        }
    }
}
