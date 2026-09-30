package classes;
import java.time.LocalDate;
import java.util.Objects;

// Stores the information shared by all types of expenses.
public abstract class Expense {
    private final String name;
    private final double price;
    private final String category;
    private final LocalDate date;

    // Creates an expense using today's date.
    protected Expense(
            String name,
            double price,
            String category
    ) {
        this(name, price, category, LocalDate.now());
    }

    // Creates an expense with the provided date and validates its values.
    protected Expense(
            String name,
            double price,
            String category,
            LocalDate date
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Expense name cannot be blank"
            );
        }

        if (price < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
            throw new IllegalArgumentException(
                    "Expense price must be valid and non-negative"
            );
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Expense category cannot be blank"
            );
        }

        this.name = name.trim();
        this.price = price;
        this.category = category.trim();
        this.date = Objects.requireNonNull(
                date,
                "Expense date cannot be null"
        );
    }

    // Returns the name of the expense.
    public String getName() {
        return name;
    }

    // Returns the expense amount.
    public double getPrice() {
        return price;
    }

    // Returns the category assigned to the expense.
    public String getCategory() {
        return category;
    }

    // Returns the date the expense was recorded.
    public LocalDate getDate() {
        return date;
    }

}
