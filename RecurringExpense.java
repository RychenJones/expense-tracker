import java.time.LocalDate;

public class RecurringExpense extends Expense {

    public RecurringExpense(
            String description,
            double price,
            String category
    ) {
        super(description, price, category);
    }

    public RecurringExpense(
            String description,
            double price,
            String category,
            LocalDate date
    ) {
        super(description, price, category, date);
    }
}