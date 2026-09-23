import java.time.LocalDate;
import java.util.Objects;

public class Expense {
    private final String name;
    private final double price;
    private final String category;
    private final LocalDate date;

    public Expense(String name, double price, String category) {
        this(name, price, category, LocalDate.now());
    }

    public Expense(
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
                    "Expense price must be a valid, non-negative number"
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

    public String getName() {
        return name;
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
}