package gui;

import classes.ManageAccount;
import classes.User;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

// Displays dialogs for changing or deleting the current user account.
public final class AccountManagementDialog {
    private static final String CHANGE_USERNAME = "Change username";
    private static final String CHANGE_PASSWORD = "Change password";
    private static final String DELETE_ACCOUNT = "Delete account";

    // Prevents this dialog helper from being instantiated.
    private AccountManagementDialog() {
    }

    // Displays account-management options and processes the selected action.
    public static void show(
            Component parent,
            User user,
            ManageAccount accountManager,
            Consumer<User> accountUpdatedAction,
            Runnable accountDeletedAction
    ) {
        String[] options = {
            CHANGE_USERNAME,
            CHANGE_PASSWORD,
            DELETE_ACCOUNT,
            "Cancel"
        };
        int choice = JOptionPane.showOptionDialog(
                parent,
                "What would you like to manage?",
                "Account Settings",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[3]
        );

        if (choice == 0) {
            changeUsername(parent, user, accountManager, accountUpdatedAction);
        } else if (choice == 1) {
            changePassword(parent, user, accountManager);
        } else if (choice == 2) {
            deleteAccount(parent, user, accountManager, accountDeletedAction);
        }
    }

    // Changes the username after verifying the current password.
    private static void changeUsername(
            Component parent,
            User user,
            ManageAccount accountManager,
            Consumer<User> accountUpdatedAction
    ) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("New username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Current password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Change Username",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newUsername = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (newUsername.isBlank() || newUsername.contains("|")) {
            GuiMessages.showError(
                    parent,
                    "Username cannot be blank or contain '|'."
            );
            return;
        }

        Path oldExpenseFile = Path.of(user.getUsername() + ".txt");
        Path newExpenseFile = Path.of(newUsername + ".txt");
        if (Files.exists(newExpenseFile)) {
            GuiMessages.showError(
                    parent,
                    "That username already has an expense file."
            );
            return;
        }

        boolean expensesMoved = false;
        try {
            if (Files.exists(oldExpenseFile)) {
                Files.move(oldExpenseFile, newExpenseFile);
                expensesMoved = true;
            }

            boolean changed = accountManager.changeUsername(
                    user.getUsername(),
                    password,
                    newUsername
            );
            if (!changed) {
                restoreExpenseFile(oldExpenseFile, newExpenseFile, expensesMoved);
                GuiMessages.showError(
                        parent,
                        "The username could not be changed. Check your "
                                + "password or choose a different username."
                );
                return;
            }

            GuiMessages.showMessage(parent, "Username changed successfully.");
            accountUpdatedAction.accept(
                    new User(user.getName(), newUsername, "")
            );
        } catch (IOException exception) {
            restoreExpenseFile(oldExpenseFile, newExpenseFile, expensesMoved);
            GuiMessages.showError(
                    parent,
                    "The username could not be changed."
            );
        }
    }

    // Restores the expense file if changing the account did not succeed.
    private static void restoreExpenseFile(
            Path oldExpenseFile,
            Path newExpenseFile,
            boolean expensesMoved
    ) {
        if (!expensesMoved) {
            return;
        }
        try {
            Files.move(newExpenseFile, oldExpenseFile);
        } catch (IOException exception) {
            // The original error is more useful to the user than this cleanup error.
        }
    }

    // Changes the password after verifying the current password.
    private static void changePassword(
            Component parent,
            User user,
            ManageAccount accountManager
    ) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmationField = new JPasswordField();
        panel.add(new JLabel("Current password:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm new password:"));
        panel.add(confirmationField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Change Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmation = new String(confirmationField.getPassword());
        if (!newPassword.equals(confirmation)) {
            GuiMessages.showError(parent, "The new passwords do not match.");
            return;
        }

        try {
            boolean changed = accountManager.changePassword(
                    user.getUsername(),
                    currentPassword,
                    newPassword
            );
            if (!changed) {
                GuiMessages.showError(
                        parent,
                        "The password could not be changed. Check your "
                                + "current password and new password."
                );
                return;
            }
            GuiMessages.showMessage(parent, "Password changed successfully.");
        } catch (IOException exception) {
            GuiMessages.showError(parent, "The password could not be changed.");
        }
    }

    // Deletes the account and its saved expenses after confirmation.
    private static void deleteAccount(
            Component parent,
            User user,
            ManageAccount accountManager,
            Runnable accountDeletedAction
    ) {
        JPasswordField passwordField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Current password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Delete Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        boolean confirmed = GuiMessages.confirm(
                parent,
                "This will delete the account and all saved expenses. Continue?",
                "Confirm Account Deletion"
        );
        if (!confirmed) {
            return;
        }

        try {
            boolean deleted = accountManager.deleteAccount(
                    user.getUsername(),
                    new String(passwordField.getPassword())
            );
            if (!deleted) {
                GuiMessages.showError(parent, "The account could not be deleted.");
                return;
            }

            try {
                Files.deleteIfExists(Path.of(user.getUsername() + ".txt"));
            } catch (IOException exception) {
                GuiMessages.showError(
                        parent,
                        "The account was deleted, but its expense file "
                                + "could not be removed."
                );
            }
            accountDeletedAction.run();
        } catch (IOException exception) {
            GuiMessages.showError(parent, "The account could not be deleted.");
        }
    }
}
