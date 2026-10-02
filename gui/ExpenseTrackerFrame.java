package gui;

import classes.CreateAccount;
import classes.LoginAccount;
import classes.User;

import javax.swing.JFrame;

import java.awt.Container;
import java.awt.Dimension;

// Provides the main window and switches between the application screens.
public class ExpenseTrackerFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final LoginAccount loginAccount = new LoginAccount();
    private final CreateAccount accountCreator = new CreateAccount();

    // Creates the application window and displays the login screen.
    public ExpenseTrackerFrame() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);
        showLoginScreen();
    }

    // Displays the login screen used when the application starts or logs out.
    private void showLoginScreen() {
        setScreen(new LoginPanel(
                this,
                loginAccount,
                accountCreator,
                this::showExpenseScreen
        ));
    }

    // Displays the expense screen for the authenticated user.
    private void showExpenseScreen(User user) {
        setScreen(new ExpensePanel(
                this,
                user,
                this::showLoginScreen,
                this::showExpenseScreen
        ));
    }

    // Replaces the current window content with a new screen.
    private void setScreen(Container screen) {
        setContentPane(screen);
        revalidate();
        repaint();
    }
}
