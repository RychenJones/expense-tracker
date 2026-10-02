package gui;

import services.CreateAccount;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;

// Displays and processes the account-creation dialog.
public final class AccountDialog {
    // Prevents this dialog helper from being instantiated.
    private AccountDialog() {
    }

    // Displays the account form and saves the account when it is valid.
    public static void show(Component parent, CreateAccount accountCreator) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextField nameField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Create an Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (result == JOptionPane.OK_OPTION) {
            createAccount(
                    parent,
                    accountCreator,
                    nameField.getText(),
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );
        }
    }

    // Validates and saves a new account from the dialog fields.
    private static void createAccount(
            Component parent,
            CreateAccount accountCreator,
            String name,
            String username,
            String password
    ) {
        name = name.trim();
        username = username.trim();

        if (!accountCreator.validateName(name)) {
            GuiMessages.showError(parent, "Name cannot be blank or contain '|'.");
            return;
        }
        if (!accountCreator.validateUsername(username)) {
            GuiMessages.showError(parent, "Username cannot contain '|'.");
            return;
        }
        if (!accountCreator.validatePassword(password)) {
            GuiMessages.showError(
                    parent,
                    "Password must be at least 6 characters and cannot "
                            + "contain '|'."
            );
            return;
        }

        try {
            if (accountCreator.usernameExists(username)) {
                GuiMessages.showError(parent, "That username is already in use.");
                return;
            }
            accountCreator.addUser(name, username, password);
            GuiMessages.showMessage(parent, "Account created successfully.");
        } catch (IOException exception) {
            GuiMessages.showError(parent, "The account could not be created.");
        }
    }
}
