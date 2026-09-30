package classes;

import java.time.LocalDate;

// Represents a one-time expense that does not repeat.
public class RegulerExpense extends Expense {
    
    // Creates a regular expense using today's date.
    public RegulerExpense(
            String name,
            double price,
            String category
    ) {
        super(name, price, category);
    }

    // Creates a regular expense with a specific date.
    public RegulerExpense(
            String name,
            double price,
            String category,
            LocalDate date
    ) {
        super(name, price, category, date);
    }

    // Converts the regular expense into the format used when saving it.
    @Override
    public String toString() {
        return String.format(
                "%s: $%.2f (%s) on %s",
                getName(),
                getPrice(),
                getCategory(),
                getDate()
        );
    }
}
