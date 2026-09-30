package classes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics {
    private final List<Expense> expenses;

    public Analytics(List<Expense> expenses) {
        this.expenses = expenses;
    }

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
