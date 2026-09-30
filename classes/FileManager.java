package classes;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

// Saves, loads, and deletes expenses in a user's expense file.
public class FileManager {
    private String filename;
    private ArrayList<Expense> expenses;

    // Connects this manager to a file and the list of expenses it controls.
    public FileManager(String filename, ArrayList<Expense> expenses) {
        this.filename = filename;
        this.expenses = expenses;
    }

    // Writes every expense in the list to the expense file.
    public void write() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Expense expense : expenses) {
                writer.println(expense);
            }
        }
    }

    // Removes an expense and saves the updated list to the file.
    public void deleteExpense(int index) throws IOException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException("Invalid expense index");
        }

        Expense deletedExpense = expenses.remove(index);

        try {
            write();
        } catch (IOException exception) {
            // Restore the expense if saving the deletion fails.
            expenses.add(index, deletedExpense);
            throw exception;
        }
    }

    // Reads all saved expenses and replaces the current list with them.
    public void read() throws IOException {
        ArrayList<Expense> loadedExpenses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    loadedExpenses.add(createExpense(line));
                } catch (RuntimeException exception) {
                    throw new IOException(
                            "Could not read expense on line " + lineNumber,
                            exception
                    );
                }
            }
        }

        expenses.clear();
        expenses.addAll(loadedExpenses);
    }

    // Parses one saved line into either a regular or recurring expense.
    private Expense createExpense(String line) {
        int priceStart = line.indexOf(": $");
        int detailsStart = line.indexOf(" (");
        int detailsEnd = line.lastIndexOf(") on ");

        if (priceStart < 0 || detailsStart < 0 || detailsEnd < 0) {
            throw new IllegalArgumentException("Invalid expense format");
        }

        String name = line.substring(0, priceStart);
        double price = Double.parseDouble(
                line.substring(priceStart + 3, detailsStart)
        );
        String details = line.substring(detailsStart + 2, detailsEnd);
        LocalDate date = LocalDate.parse(line.substring(detailsEnd + 5));

        int frequencySeparator = details.indexOf(", ");
        if (frequencySeparator < 0) {
            return new RegulerExpense(name, price, details, date);
        }

        String category = details.substring(0, frequencySeparator);
        String frequency = details.substring(frequencySeparator + 2);
        return new RecurringExpense(
                name,
                price,
                category,
                date,
                frequency
        );
    }

}
