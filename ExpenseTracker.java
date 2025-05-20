import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {
    private List<Transaction> transactions = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void start() {
        while (true) {
            System.out.println("\n1. Add Transaction");
            System.out.println("2. Load from File");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Save to File");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

           switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    loadFromFile();
                    break;
                case 3:
                    showMonthlySummary();
                    break;
                case 4:
                    saveToFile();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }

        }
    }

    private void addTransaction() {
        System.out.print("Enter transaction type (INCOME/EXPENSE): ");
        TransactionType type = TransactionType.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Enter category (e.g., Salary, Food, Rent): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        transactions.add(new Transaction(type, date, category, amount));
        System.out.println("Transaction added.");
    }

    private void loadFromFile() {
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                TransactionType type = TransactionType.valueOf(parts[0]);
                LocalDate date = LocalDate.parse(parts[1]);
                String category = parts[2];
                double amount = Double.parseDouble(parts[3]);
                transactions.add(new Transaction(type, date, category, amount));
            }
            System.out.println("Transactions loaded from file.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    private void saveToFile() {
        System.out.print("Enter file path to save: ");
        String path = scanner.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Transactions saved to file.");
        } catch (IOException e) {
            System.out.println("Failed to write file: " + e.getMessage());
        }
    }

    private void showMonthlySummary() {
        System.out.print("Enter year and month (yyyy-MM): ");
        String input = scanner.nextLine();
        double totalIncome = 0, totalExpense = 0;

        for (Transaction t : transactions) {
            String transactionMonth = t.getDate().toString().substring(0, 7);
            if (transactionMonth.equals(input)) {
                if (t.getType() == TransactionType.INCOME)
                    totalIncome += t.getAmount();
                else
                    totalExpense += t.getAmount();
            }
        }

        System.out.println("Summary for " + input + ":");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpense);
        System.out.println("Net Savings: " + (totalIncome - totalExpense));
    }
}
