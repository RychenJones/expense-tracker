package gui;

import javax.swing.JOptionPane;

import java.awt.Component;

// Provides consistent message dialogs for the Swing interface.
public final class GuiMessages {
    // Prevents this message helper from being instantiated.
    private GuiMessages() {
    }

    // Displays an error message.
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // Displays an informational message.
    public static void showMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Expense Tracker",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Asks the user to confirm an action and returns their response.
    public static boolean confirm(
            Component parent,
            String message,
            String title
    ) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    // Displays a scrollable informational component in a dialog.
    public static void showScrollDialog(
            Component parent,
            Component content,
            String title
    ) {
        JOptionPane.showMessageDialog(
                parent,
                content,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
