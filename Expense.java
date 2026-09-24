import java.time.LocalDate;
import java.util.Objects;

public abstract class Expense {
    private final String description;
    private final double price;
    private final String category;
    private final LocalDate date;

    protected Expense(
            String description,
            double price,
            String category
    ) {
        this(description, price, category, LocalDate.now());
    }

    protected Expense(
            String description,
            double price,
            String category,
            LocalDate date
    ) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Expense description cannot be blank"
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

        this.description = description.trim();
        this.price = price;
        this.category = category.trim();
        this.date = Objects.requireNonNull(
                date,
                "Expense date cannot be null"
        );
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(
                "%s: $%.2f (%s) on %s",
                description,
                price,
                category,
                date
        );
    }
}