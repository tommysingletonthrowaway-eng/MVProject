package dev.tommy.bankapp.data.budget;

import dev.tommy.bankapp.exceptions.budget.CategoryAlreadyExistsException;
import dev.tommy.bankapp.exceptions.budget.CategoryDoesNotExistException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BudgetManager implements Serializable {

    private final List<BudgetEntry> entries = new ArrayList<>();

    public void addEntry(BudgetEntry entry) throws CategoryAlreadyExistsException {
        if (hasEntryWithCategory(entry.category())) {
            throw new CategoryAlreadyExistsException(entry.category());
        }
        entries.add(entry);
    }

    public void removeEntry(int index) throws CategoryDoesNotExistException {
        BudgetEntry entry;
        try {
            entry = entries.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new CategoryDoesNotExistException(index);
        }
        entries.remove(index);
    }

    public void removeAllEntries() {
        entries.clear();
    }

    public List<BudgetEntry> getEntries() {
        return List.copyOf(entries);
    }

    public boolean hasEntryWithCategory(String s) {
        return entries.stream().anyMatch(e -> e.category().equalsIgnoreCase(s));
    }

    public double getTotalIncome() {
        return entries.stream()
                .filter(e -> e.type() == BudgetEntry.Type.INCOME)
                .mapToDouble(BudgetEntry::amount)
                .sum();
    }

    public double getTotalExpenses() {
        return entries.stream()
                .filter(e -> e.type() == BudgetEntry.Type.EXPENSE)
                .mapToDouble(BudgetEntry::amount)
                .sum();
    }

    public double getNetMonthlyBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    public void printSummary() {
        System.out.println("==== Monthly Budget Summary ====");
        System.out.printf("Expected Income : £%.2f%n", getTotalIncome());
        System.out.printf("Expected Expenses: £%.2f%n", getTotalExpenses());
        System.out.printf("Net Monthly Balance: £%.2f%n", getNetMonthlyBalance());
        System.out.println();
    }

    public void printAllEntries() {
        System.out.println("==== Income ====");
        entries.stream()
                .filter(e -> e.type() == BudgetEntry.Type.INCOME)
                .forEach(System.out::println);

        System.out.println("\n==== Expenses ====");
        entries.stream()
                .filter(e -> e.type() == BudgetEntry.Type.EXPENSE)
                .forEach(System.out::println);
    }
}
