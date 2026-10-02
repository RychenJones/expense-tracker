package gui;

import services.Analytics;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Component;
import java.util.Map;

// Displays spending totals, averages, and category analytics.
public final class AnalyticsDialog {
    // Prevents this dialog helper from being instantiated.
    private AnalyticsDialog() {
    }

    // Displays the current analytics for the user's expense list.
    public static void show(Component parent, Analytics analytics) {
        StringBuilder text = new StringBuilder();
        text.append(String.format(
                "Total weekly spending: $%.2f%n",
                analytics.getTotalWeeklySpending()
        ));
        text.append(String.format(
                "Total monthly spending: $%.2f%n",
                analytics.getTotalMonthlySpending()
        ));
        text.append(String.format(
                "Average weekly spending: $%.2f%n",
                analytics.getAverageWeeklySpending()
        ));
        text.append(String.format(
                "Average monthly spending: $%.2f%n%n",
                analytics.getAverageMonthlySpending()
        ));

        text.append("Weekly spending by category:\n");
        appendCategoryTotals(text, analytics.getWeeklySpendingByCategory());
        text.append("\nMonthly spending by category:\n");
        appendCategoryTotals(text, analytics.getMonthlySpendingByCategory());
        text.append("\nCategory percentages:\n");
        for (Map.Entry<String, Double> entry
                : analytics.getCategoryPercentages().entrySet()) {
            text.append(String.format(
                    "  %s: %.2f%%%n",
                    entry.getKey(),
                    entry.getValue()
            ));
        }

        JTextArea area = new JTextArea(text.toString(), 16, 42);
        area.setEditable(false);
        area.setCaretPosition(0);
        GuiMessages.showScrollDialog(parent, new JScrollPane(area), "Analytics");
    }

    // Adds category totals to the analytics display text.
    private static void appendCategoryTotals(
            StringBuilder text,
            Map<String, Double> categoryTotals
    ) {
        if (categoryTotals.isEmpty()) {
            text.append("  No spending recorded.\n");
            return;
        }
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            text.append(String.format(
                    "  %s: $%.2f%n",
                    entry.getKey(),
                    entry.getValue()
            ));
        }
    }
}
