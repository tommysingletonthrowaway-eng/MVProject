package dev.tommy.bankapp.data.budget;

import java.io.Serializable;

/**
 * @param category e.g., "Salary", "Rent"
 * @param amount   monthly expected amount
 * @param type     INCOME or EXPENSE
 * @param notes    optional notes
 */
public record BudgetEntry(String category, double amount, Type type, String notes) implements Serializable {
    public enum Type {
        INCOME,
        EXPENSE
    }

    @Override
    public String toString() {
        return String.format("%-12s %-8s £%.2f  %s",
                category, type, amount, (notes == null ? "" : notes));
    }
}
