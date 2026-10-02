package gui;

import classes.CreateAccount;
import classes.LoginAccount;
import classes.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.function.Consumer;

// Displays the login form and account-creation option.
public class LoginPanel extends JPanel {
    private final Component parent;
    private final LoginAccount loginAccount;
    private final CreateAccount accountCreator;
    private final Consumer<User> loginSuccess;

    // Creates a login panel with the account services and success callback.
    public LoginPanel(
            Component parent,
            LoginAccount loginAccount,
            CreateAccount accountCreator,
            Consumer<User> loginSuccess
    ) {
        this.parent = parent;
        this.loginAccount = loginAccount;
        this.accountCreator = accountCreator;
        this.loginSuccess = loginSuccess;
        buildInterface();
    }

    // Builds the login form and connects its buttons to their actions.
    private void buildInterface() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JLabel title = new JLabel("Expense Tracker", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(24.0f));
        addToGrid(title, constraints, 0, 0, 2);

        JLabel subtitle = new JLabel(
                "Log in to manage your expenses.",
                SwingConstants.CENTER
        );
        addToGrid(subtitle, constraints, 0, 1, 2);

        add(new JSeparator(), gridConstraints(constraints, 0, 2, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        addToGrid(usernameLabel, constraints, 0, 3, 1);
        addToGrid(usernameField, constraints, 1, 3, 1);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        addToGrid(passwordLabel, constraints, 0, 4, 1);
        addToGrid(passwordField, constraints, 1, 4, 1);

        JButton loginButton = new JButton("Log in");
        JButton createButton = new JButton("Create an account");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(loginButton);
        buttons.add(createButton);
        addToGrid(buttons, constraints, 0, 5, 2);

        loginButton.addActionListener(event -> login(
                usernameField.getText(),
                new String(passwordField.getPassword())
        ));
        passwordField.addActionListener(event -> login(
                usernameField.getText(),
                new String(passwordField.getPassword())
        ));
        createButton.addActionListener(
                event -> AccountDialog.show(parent, accountCreator)
        );
    }

    // Adds a component to the form at the supplied grid position.
    private void addToGrid(
            Component component,
            GridBagConstraints constraints,
            int column,
            int row,
            int width
    ) {
        add(component, gridConstraints(constraints, column, row, width));
    }

    // Creates grid constraints for one component without changing the source.
    private GridBagConstraints gridConstraints(
            GridBagConstraints source,
            int column,
            int row,
            int width
    ) {
        GridBagConstraints result = (GridBagConstraints) source.clone();
        result.gridx = column;
        result.gridy = row;
        result.gridwidth = width;
        return result;
    }

    // Authenticates the entered credentials and reports the logged-in user.
    private void login(String username, String password) {
        username = username.trim();
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required.");
            return;
        }

        try {
            User user = loginAccount.authenticate(username, password);
            if (user == null) {
                showError("Incorrect username or password.");
                return;
            }
            loginSuccess.accept(user);
        } catch (IOException exception) {
            showError("Unable to access the accounts file.");
        }
    }

    // Displays an error message on the login screen.
    private void showError(String message) {
        GuiMessages.showError(parent, message);
    }
}
