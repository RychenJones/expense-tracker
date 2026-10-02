import classes.CreateAccount;
import classes.LoginAccount;
import classes.User;
import cli.AccountMenu;
import cli.ExpenseMenu;

import java.util.Scanner;

// Runs the command-line interface for the expense tracker.
public class Main {
    // Starts the application and displays the main account menu.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountMenu accountMenu = new AccountMenu(
                scanner,
                new CreateAccount(),
                new LoginAccount()
        );

        System.out.println("Welcome to the Expense Tracker!");

        boolean running = true;
        while (running) {
            User user = accountMenu.show();

            if (user == null) {
                running = false;
            } else {
                new ExpenseMenu(scanner, user).show();
                running = false;
            }
        }

        scanner.close();
    }
}
