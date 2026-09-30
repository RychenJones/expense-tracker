package classes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Calculates spending summaries from a user's expenses.
public class Analytics {
    private final List<Expense> expenses;

    // Creates an analytics object using the user's expense list.
    public Analytics(List<Expense> expenses) {
        this.expenses = expenses;
    }

    // Calculates what percentage of total spending belongs to each category.
    public Map<String, Double> getCategoryPercentages() {
        Map<String, Double> categoryTotals = new HashMap<>();
        double totalSpending = 0;

        for (Expense expense : expenses) {
            categoryTotals.merge(
                    expense.getCategory(),
                    expense.getPrice(),
                    Double::sum
            );
            totalSpending += expense.getPrice();
        }

        Map<String, Double> categoryPercentages = new HashMap<>();

        if (totalSpending == 0) {
            return categoryPercentages;
        }

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            double percentage = entry.getValue() / totalSpending * 100;
            categoryPercentages.put(entry.getKey(), percentage);
        }

        return categoryPercentages;
    }

    // Calculates the average amount spent per month represented in the list.
    public double getAverageMonthlySpending() {
        Map<YearMonth, Double> monthlyTotals = new HashMap<>();

        for (Expense expense : expenses) {
            YearMonth month = YearMonth.from(expense.getDate());
            monthlyTotals.merge(
                    month,
                    expense.getPrice(),
                    Double::sum
            );
        }

        if (monthlyTotals.isEmpty()) {
            return 0.0;
        }

        double totalSpending = 0;

        for (double monthlyTotal : monthlyTotals.values()) {
            totalSpending += monthlyTotal;
        }

        return totalSpending / monthlyTotals.size();
    }

    // Calculates the average amount spent per week represented in the list.
    public double getAverageWeeklySpending() {
        Map<LocalDate, Double> weeklyTotals = new HashMap<>();

        for (Expense expense : expenses) {
            LocalDate weekStart = expense.getDate().with(
                    TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            );
            weeklyTotals.merge(
                    weekStart,
                    expense.getPrice(),
                    Double::sum
            );
        }

        if (weeklyTotals.isEmpty()) {
            return 0.0;
        }

        double totalSpending = 0;

        for (double weeklyTotal : weeklyTotals.values()) {
            totalSpending += weeklyTotal;
        }

        return totalSpending / weeklyTotals.size();
    }

    // Calculates this week's spending grouped by category.
    public Map<String, Double> getWeeklySpendingByCategory() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );
        LocalDate endOfWeek = today.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
        );
        Map<String, Double> weeklySpending = new HashMap<>();

        for (Expense expense : expenses) {
            LocalDate expenseDate = expense.getDate();

            if (!expenseDate.isBefore(startOfWeek)
                    && !expenseDate.isAfter(endOfWeek)) {
                weeklySpending.merge(
                        expense.getCategory(),
                        expense.getPrice(),
                        Double::sum
                );
            }
        }

        return weeklySpending;
    }

    // Calculates this month's spending grouped by category.
    public Map<String, Double> getMonthlySpendingByCategory() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        Map<String, Double> monthlySpending = new HashMap<>();

        for (Expense expense : expenses) {
            LocalDate expenseDate = expense.getDate();

            if (!expenseDate.isBefore(startOfMonth)
                    && !expenseDate.isAfter(endOfMonth)) {
                monthlySpending.merge(
                        expense.getCategory(),
                        expense.getPrice(),
                        Double::sum
                );
            }
        }

        return monthlySpending;
    }

    // Calculates the total amount spent during the current week.
    public double getTotalWeeklySpending() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );
        LocalDate endOfWeek = today.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
        );

        double total = 0;

        for (Expense expense : expenses) {
            LocalDate expenseDate = expense.getDate();

            if (!expenseDate.isBefore(startOfWeek)
                    && !expenseDate.isAfter(endOfWeek)) {
                total += expense.getPrice();
            }
        }

        return total;
    }

    // Calculates the total amount spent during the current month.
    public double getTotalMonthlySpending() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        double total = 0;

        for (Expense expense : expenses) {
            LocalDate expenseDate = expense.getDate();

            if (!expenseDate.isBefore(startOfMonth)
                    && !expenseDate.isAfter(endOfMonth)) {
                total += expense.getPrice();
            }
        }

        return total;
    }
}
