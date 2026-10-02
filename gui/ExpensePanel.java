package gui;

import classes.Analytics;
import classes.Expense;
import classes.FileManager;
import classes.ManageAccount;
import classes.RecurringExpense;
import classes.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

// Displays and manages expenses for one authenticated user.
public class ExpensePanel extends JPanel {
    private final Component parent;
    private final Runnable logoutAction;
    private final Consumer<User> accountUpdatedAction;
    private final User user;
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final FileManager fileManager;
    private final Analytics analytics;
    private final ManageAccount accountManager = new ManageAccount();
    private final DefaultTableModel tableModel;

    // Creates an expense panel and loads the user's saved expenses.
    public ExpensePanel(
            Component parent,
            User user,
            Runnable logoutAction,
            Consumer<User> accountUpdatedAction
    ) {
        this.parent = parent;
        this.logoutAction = logoutAction;
        this.accountUpdatedAction = accountUpdatedAction;
        this.user = user;
        String filename = user.getUsername() + ".txt";
        fileManager = new FileManager(filename, expenses);
        analytics = new Analytics(expenses);
        loadExpenses(filename);

        tableModel = createTableModel();
        buildInterface(user);
        refreshTable();
    }

    // Builds the expense table and action buttons.
    private void buildInterface(User user) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel welcome = new JLabel("Welcome, " + user.getName() + "!");
        welcome.setFont(welcome.getFont().deriveFont(18.0f));
        add(welcome, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Selected");
        JButton analyticsButton = new JButton("View Analytics");
        JButton accountButton = new JButton("Account Settings");
        JButton logoutButton = new JButton("Log out");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(analyticsButton);
        buttons.add(accountButton);
        buttons.add(logoutButton);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(event -> showAddExpenseDialog());
        deleteButton.addActionListener(
                event -> deleteSelectedExpense(table)
        );
        analyticsButton.addActionListener(
                event -> AnalyticsDialog.show(parent, analytics)
        );
        accountButton.addActionListener(event -> showAccountSettings());
        logoutButton.addActionListener(event -> logoutAction.run());
    }

    // Creates a non-editable table model for displaying expenses.
    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[] {"Name", "Amount", "Category", "Date", "Type"},
                0
        ) {
            // Keeps expense table cells read-only.
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // Loads saved expenses when the user's expense file exists.
    private void loadExpenses(String filename) {
        if (!new File(filename).exists()) {
            return;
        }
        try {
            fileManager.read();
        } catch (IOException exception) {
            GuiMessages.showError(parent, "Unable to load your saved expenses.");
        }
    }

    // Updates the table to match the current expense list.
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Expense expense : expenses) {
            String type = "Single";
            if (expense instanceof RecurringExpense recurringExpense) {
                type = "Recurring (" + recurringExpense.getFrequency() + ")";
            }
            tableModel.addRow(new Object[] {
                expense.getName(),
                String.format("$%.2f", expense.getPrice()),
                expense.getCategory(),
                expense.getDate(),
                type
            });
        }
    }

    // Displays the add-expense dialog and saves the submitted expense.
    private void showAddExpenseDialog() {
        Expense expense = AddExpenseDialog.show(parent);
        if (expense == null) {
            return;
        }

        expenses.add(expense);
        try {
            fileManager.write();
            refreshTable();
        } catch (IOException exception) {
            expenses.remove(expenses.size() - 1);
            GuiMessages.showError(parent, "The expense could not be saved.");
        }
    }

    // Confirms and deletes the expense selected in the table.
    private void deleteSelectedExpense(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            GuiMessages.showError(parent, "Select an expense to delete first.");
            return;
        }

        Expense expense = expenses.get(selectedRow);
        if (!GuiMessages.confirm(
                parent,
                "Delete \"" + expense.getName() + "\"?",
                "Confirm Deletion"
        )) {
            return;
        }

        try {
            fileManager.deleteExpense(selectedRow);
            refreshTable();
        } catch (IOException exception) {
            GuiMessages.showError(parent, "The expense could not be deleted.");
        }
    }

    // Displays the account-management options for the current user.
    private void showAccountSettings() {
        AccountManagementDialog.show(
                parent,
                user,
                accountManager,
                accountUpdatedAction,
                logoutAction
        );
    }
}
