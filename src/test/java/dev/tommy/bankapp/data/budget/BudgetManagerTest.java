package dev.tommy.bankapp.data.budget;

import dev.tommy.bankapp.exceptions.budget.CategoryAlreadyExistsException;
import dev.tommy.bankapp.exceptions.budget.CategoryDoesNotExistException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetManagerTest {
    @Test
    void addEntry_entersCollection() {
        BudgetManager budgetManager = new BudgetManager();
        var entry = generateGenericEntry("food");
        assertDoesNotThrow(() -> budgetManager.addEntry(entry));
        assertEquals(1, budgetManager.getEntries().size());
    }

    @Test
    void removeEntry_entryNotInCollection() {
        BudgetManager budgetManager = new BudgetManager();

        try {
            budgetManager.removeEntry(0);
        } catch (CategoryDoesNotExistException e) {
            return;
        }

        fail();
    }

    @Test
    void removeAllEntries_zeroEntriesRemain() {
        BudgetManager budgetManager = new BudgetManager();
        var entry1 = generateGenericEntry("Name1");
        var entry2 = generateGenericEntry("Name2");
        try {
            budgetManager.addEntry(entry1);
            budgetManager.addEntry(entry2);
        } catch (CategoryAlreadyExistsException _) { }

        budgetManager.removeAllEntries();
        assertEquals(0, budgetManager.getEntries().size());
    }

    @Test
    void hasEntry_entryInCollection() {
        BudgetManager budgetManager = new BudgetManager();
        var entry = generateGenericEntry("Name1");
        try {
            budgetManager.addEntry(entry);
        } catch (CategoryAlreadyExistsException e) { }
        assert (budgetManager.hasEntryWithCategory("Name1"));
    }

    @Test
    void hasEntry_entryNotInCollection() {
        BudgetManager budgetManager = new BudgetManager();
        assertFalse(budgetManager.hasEntryWithCategory("Name1"));
    }

    @Test
    void getNetMonthlyBalance_neutral() {
        BudgetManager budgetManager = new BudgetManager();
        var incomeEntry = generateGenericEntry("Income", BudgetEntry.Type.INCOME, 1);
        var expenseEntry = generateGenericEntry("Expense", BudgetEntry.Type.EXPENSE, 1);
        try {
            budgetManager.addEntry(incomeEntry);
            budgetManager.addEntry(expenseEntry);
        } catch (CategoryAlreadyExistsException e) {
            fail();
        }
        assertEquals(0, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void getNetMonthlyBalance_moreIncome() {
        BudgetManager budgetManager = new BudgetManager();
        var incomeEntry = generateGenericEntry("Income", BudgetEntry.Type.INCOME, 1);
        try {
            budgetManager.addEntry(incomeEntry);
        } catch (CategoryAlreadyExistsException e) {
            fail();
        }
        assertEquals(1, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void getNetMonthlyBalance_moreExpenses() {
        BudgetManager budgetManager = new BudgetManager();
        var expenseEntry = generateGenericEntry("Expense", BudgetEntry.Type.EXPENSE, 1);
        try {
            budgetManager.addEntry(expenseEntry);
        } catch (CategoryAlreadyExistsException e) {
            fail();
        }
        assertEquals(-1, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void getTotalExpenses() {
        BudgetManager budgetManager = new BudgetManager();
        var expenseEntry1 = generateGenericEntry("Expense1", BudgetEntry.Type.EXPENSE, 1);
        var expenseEntry2 = generateGenericEntry("Expense2", BudgetEntry.Type.EXPENSE, 1);
        try {
            budgetManager.addEntry(expenseEntry1);
            budgetManager.addEntry(expenseEntry2);
        } catch (CategoryAlreadyExistsException e) {
            fail();
        }
        assertEquals(2, budgetManager.getTotalExpenses());
    }

    @Test
    void getTotalIncome() {
        BudgetManager budgetManager = new BudgetManager();
        var incomeEntry1 = generateGenericEntry("Income1", BudgetEntry.Type.INCOME, 1);
        var incomeEntry2 = generateGenericEntry("Income2", BudgetEntry.Type.INCOME, 1);
        try {
            budgetManager.addEntry(incomeEntry1);
            budgetManager.addEntry(incomeEntry2);
        } catch (CategoryAlreadyExistsException e) {
            fail();
        }
        assertEquals(2, budgetManager.getTotalIncome());
    }

    BudgetEntry generateGenericEntry(String category) {
        return new BudgetEntry(category, 1, BudgetEntry.Type.EXPENSE, "notes");
    }

    BudgetEntry generateGenericEntry(String category, BudgetEntry.Type type, double amount) {
        return new BudgetEntry(category, amount, type, "notes");
    }
}