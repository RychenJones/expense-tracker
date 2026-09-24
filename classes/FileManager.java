package classes;
import java.util.ArrayList;

public class FileManager {
    private String filename;
    private ArrayList<Expense> expenses;

    public FileManager(String filename, ArrayList<Expense> expenses) {
        this.filename = filename;
        this.expenses = expenses;
    }
}
