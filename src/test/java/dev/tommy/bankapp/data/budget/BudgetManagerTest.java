package dev.tommy.bankapp.data.budget;

import dev.tommy.bankapp.exceptions.budget.CategoryAlreadyExistsException;
import dev.tommy.bankapp.exceptions.budget.CategoryDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetManagerTest {

    private BudgetManager budgetManager;

    @BeforeEach
    void setUp() {
        budgetManager = new BudgetManager();
    }

    // ------------------------------------------------------------------------
    // Entry management
    // ------------------------------------------------------------------------

    @Test
    void givenValidEntry_whenAddEntry_thenEntryAddedToCollection() {
        BudgetEntry entry = new BudgetEntry("Food", 50, BudgetEntry.Type.EXPENSE, "Lunch");

        assertDoesNotThrow(() -> budgetManager.addEntry(entry));
        assertEquals(1, budgetManager.getEntries().size());
        assertTrue(budgetManager.hasEntryWithCategory("Food"));
    }

    @Test
    void givenDuplicateCategory_whenAddEntry_thenThrowsException() throws CategoryAlreadyExistsException {
        BudgetEntry entry = new BudgetEntry("Food", 50, BudgetEntry.Type.EXPENSE, "Lunch");
        budgetManager.addEntry(entry);

        BudgetEntry duplicate = new BudgetEntry("Food", 100, BudgetEntry.Type.EXPENSE, "Dinner");
        assertThrows(CategoryAlreadyExistsException.class, () -> budgetManager.addEntry(duplicate));
    }

    @Test
    void givenExistingEntries_whenRemoveAllEntries_thenCollectionIsEmpty() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Rent", 1000, BudgetEntry.Type.EXPENSE, ""));
        budgetManager.addEntry(new BudgetEntry("Salary", 2000, BudgetEntry.Type.INCOME, ""));

        budgetManager.removeAllEntries();

        assertTrue(budgetManager.getEntries().isEmpty());
    }

    @Test
    void givenInvalidIndex_whenRemoveEntry_thenThrowsException() {
        assertThrows(CategoryDoesNotExistException.class, () -> budgetManager.removeEntry(0));
    }

    // ------------------------------------------------------------------------
    // Existence checks
    // ------------------------------------------------------------------------

    @Test
    void givenEntryAdded_whenHasEntryWithCategory_thenReturnsTrue() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Rent", 1000, BudgetEntry.Type.EXPENSE, ""));
        assertTrue(budgetManager.hasEntryWithCategory("Rent"));
    }

    @Test
    void givenNoEntries_whenHasEntryWithCategory_thenReturnsFalse() {
        assertFalse(budgetManager.hasEntryWithCategory("Nonexistent"));
    }

    // ------------------------------------------------------------------------
    // Calculations
    // ------------------------------------------------------------------------

    @Test
    void givenEqualIncomeAndExpense_whenGetNetMonthlyBalance_thenReturnsZero() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Job", 1000, BudgetEntry.Type.INCOME, ""));
        budgetManager.addEntry(new BudgetEntry("Rent", 1000, BudgetEntry.Type.EXPENSE, ""));
        assertEquals(0, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void givenMoreIncomeThanExpenses_whenGetNetMonthlyBalance_thenReturnsPositive() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Job", 1500, BudgetEntry.Type.INCOME, ""));
        budgetManager.addEntry(new BudgetEntry("Rent", 1000, BudgetEntry.Type.EXPENSE, ""));
        assertEquals(500, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void givenMoreExpensesThanIncome_whenGetNetMonthlyBalance_thenReturnsNegative() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Job", 1000, BudgetEntry.Type.INCOME, ""));
        budgetManager.addEntry(new BudgetEntry("Rent", 1200, BudgetEntry.Type.EXPENSE, ""));
        assertEquals(-200, budgetManager.getNetMonthlyBalance());
    }

    @Test
    void givenMultipleExpenses_whenGetTotalExpenses_thenSumsCorrectly() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Rent", 1000, BudgetEntry.Type.EXPENSE, ""));
        budgetManager.addEntry(new BudgetEntry("Utilities", 300, BudgetEntry.Type.EXPENSE, ""));
        assertEquals(1300, budgetManager.getTotalExpenses());
    }

    @Test
    void givenMultipleIncomes_whenGetTotalIncome_thenSumsCorrectly() throws CategoryAlreadyExistsException {
        budgetManager.addEntry(new BudgetEntry("Job", 1500, BudgetEntry.Type.INCOME, ""));
        budgetManager.addEntry(new BudgetEntry("Freelance", 500, BudgetEntry.Type.INCOME, ""));
        assertEquals(2000, budgetManager.getTotalIncome());
    }
}
