package gui;

import services.Expense;
import services.RecurringExpense;
import services.RegulerExpense;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Displays the form used to create a regular or recurring expense.
public final class AddExpenseDialog {
    private static final String[] CATEGORIES = {
        "Food", "Transportation", "Housing", "Utilities",
        "Entertainment", "Health", "Shopping", "Other"
    };

    private static final String[] FREQUENCIES = {
        RecurringExpense.DAILY,
        RecurringExpense.WEEKLY,
        RecurringExpense.MONTHLY,
        RecurringExpense.YEARLY
    };

    // Prevents this dialog helper from being instantiated.
    private AddExpenseDialog() {
    }

    // Displays the form and returns the created expense, or null if cancelled.
    public static Expense show(Component parent) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JComboBox<String> typeBox = new JComboBox<>(
                new String[] {"Single", "Recurring"}
        );
        JComboBox<String> frequencyBox = new JComboBox<>(FREQUENCIES);
        frequencyBox.setEnabled(false);
        typeBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                frequencyBox.setEnabled(typeBox.getSelectedIndex() == 1);
            }
        });

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Type:"));
        panel.add(typeBox);
        panel.add(new JLabel("Frequency:"));
        panel.add(frequencyBox);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Add Expense",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        return createExpense(
                parent,
                nameField.getText(),
                priceField.getText(),
                (String) categoryBox.getSelectedItem(),
                dateField.getText(),
                typeBox.getSelectedIndex() == 1,
                (String) frequencyBox.getSelectedItem()
        );
    }

    // Validates the form values and creates the selected expense type.
    private static Expense createExpense(
            Component parent,
            String name,
            String priceText,
            String category,
            String dateText,
            boolean recurring,
            String frequency
    ) {
        try {
            double price = Double.parseDouble(priceText.trim());
            if (price < 0 || !Double.isFinite(price)) {
                GuiMessages.showError(
                        parent,
                        "Price must be a valid non-negative number."
                );
                return null;
            }

            LocalDate date = LocalDate.parse(dateText.trim());
            if (recurring) {
                return new RecurringExpense(
                        name, price, category, date, frequency
                );
            }
            return new RegulerExpense(name, price, category, date);
        } catch (NumberFormatException exception) {
            GuiMessages.showError(parent, "Price must be a valid number.");
        } catch (DateTimeParseException exception) {
            GuiMessages.showError(parent, "Date must use YYYY-MM-DD format.");
        } catch (IllegalArgumentException exception) {
            GuiMessages.showError(parent, exception.getMessage());
        }
        return null;
    }
}
