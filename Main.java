import gui.ExpenseTrackerFrame;

import javax.swing.SwingUtilities;

// Runs the command-line interface for the expense tracker.
public class Main {
    // Starts the Swing interface on the event-dispatching thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerFrame frame = new ExpenseTrackerFrame();
            frame.setVisible(true);
        });
    }
}
