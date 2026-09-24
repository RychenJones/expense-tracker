package classes;
import java.time.LocalDate;

public class RecurringExpense extends Expense {
    public static final String DAILY = "daily";
    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";

    private final String frequency;

    public RecurringExpense(
            String description,
            double price,
            String category,
            String frequency
    ) {
        this(description, price, category, LocalDate.now(), frequency);
    }

    public RecurringExpense(
            String description,
            double price,
            String category,
            LocalDate date,
            String frequency
    ) {
        super(description, price, category, date);
        
        if (frequency == null || frequency.isBlank()) {
            throw new IllegalArgumentException(
                    "Recurring expense frequency cannot be blank"
            );
        }

        this.frequency = frequency.trim();
    }

    public String getFrequency() {
        return frequency;
    }
}