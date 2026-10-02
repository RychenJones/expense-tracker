package cli;

import classes.RecurringExpense;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Provides reusable input prompts and validation for the command-line UI.
public final class ConsoleInput {
    private static final String[] CATEGORIES = {
        "Food", "Transportation", "Housing", "Utilities",
        "Entertainment", "Health", "Shopping", "Other"
    };

    // Prevents this utility class from being instantiated.
    private ConsoleInput() {
    }

    // Reads a required text value and rejects blank input.
    public static String readRequired(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be blank.");
        }
    }

    // Reads and validates a non-negative expense price.
    public static double readPrice(Scanner scanner) {
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

    // Displays category choices and returns the selected category.
    public static String readCategory(Scanner scanner) {
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
    public static LocalDate readDate(Scanner scanner) {
        while (true) {
            System.out.print("Date (YYYY-MM-DD, or press Enter for today): ");
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException exception) {
                System.out.println("Please enter a date in YYYY-MM-DD format.");
            }
        }
    }

    // Displays frequency choices and returns the selected frequency.
    public static String readFrequency(Scanner scanner) {
        while (true) {
            System.out.println("Frequency:");
            System.out.println("1. Daily");
            System.out.println("2. Weekly");
            System.out.println("3. Monthly");
            System.out.println("4. Yearly");
            System.out.print("Choose a frequency: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> { return RecurringExpense.DAILY; }
                case "2" -> { return RecurringExpense.WEEKLY; }
                case "3" -> { return RecurringExpense.MONTHLY; }
                case "4" -> { return RecurringExpense.YEARLY; }
                default -> System.out.println(
                        "Please choose a frequency number from the list."
                );
            }
        }
    }
}
