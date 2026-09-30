package classes;
import java.time.LocalDate;

// Represents an expense that repeats on a regular schedule.
// It inherits the basic expense information from Expense.
public class RecurringExpense extends Expense {
    public static final String DAILY = "daily";
    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";

    private final String frequency;

    // Creates a recurring expense using today's date.
    public RecurringExpense(
            String name,
            double price,
            String category,
            String frequency
    ) {
        this(name, price, category, LocalDate.now(), frequency);
    }

    // Creates a recurring expense with a specific date and frequency.
    public RecurringExpense(
            String name,
            double price,
            String category,
            LocalDate date,
            String frequency
    ) {
        super(name, price, category, date);
        
        // A recurring expense must have a frequency describing its schedule.
        if (frequency == null || frequency.isBlank()) {
            throw new IllegalArgumentException(
                    "Recurring expense frequency cannot be blank"
            );
        }

        this.frequency = frequency.trim();
    }

    // Returns how often the expense repeats.
    public String getFrequency() {
        return frequency;
    }

    @Override
    // Converts the recurring expense into the format used when saving it.
    public String toString() {
        return String.format(
                "%s: $%.2f (%s, %s) on %s",
                getName(),
                getPrice(),
                getCategory(),
                getFrequency(),
                getDate()
        );
    }
}
