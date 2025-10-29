package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.transaction.Transaction;
import dev.tommy.bankapp.data.transaction.TransactionType;
import dev.tommy.bankapp.exceptions.account.NoTransactionException;
import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("Natwest", Currency.GBP);
    }

    // ------------------------------------------------------------------------
    // Account creation and basic state
    // ------------------------------------------------------------------------

    @Test
    void givenNewAccount_whenCreated_thenDefaultsAreCorrect() {
        assertEquals("Natwest", account.getIdentifier());
        assertEquals(Currency.GBP, account.getCurrency());
        assertEquals(0.0, account.getBalance());
        assertEquals("£0.00", account.getFormattedBalance());
        assertTrue(account.getTransactionHistory().isEmpty());
    }

    // ------------------------------------------------------------------------
    // Balance management
    // ------------------------------------------------------------------------

    @Test
    void whenSetBalance_thenBalanceAndFormatAreUpdated() {
        account.setBalance(150.50);
        assertEquals(150.50, account.getBalance());
        assertEquals("£150.50", account.getFormattedBalance());
    }

    // ------------------------------------------------------------------------
    // Currency change behavior
    // ------------------------------------------------------------------------

    @Test
    void whenChangeCurrencyWithoutConversion_thenSymbolChangesOnly() {
        account.setBalance(100);
        account.setCurrency(Currency.USD, false);

        assertEquals(100, account.getBalance());
        assertEquals("$100.00", account.getFormattedBalance());
    }

    @Test
    void whenChangeCurrencyWithConversion_thenBalanceIsConverted() {
        account.setBalance(100);
        double rate = CurrencyConverter.getConversionRate(Currency.GBP, Currency.USD);
        double expected = 100 * rate;

        account.setCurrency(Currency.USD, true);

        assertEquals(expected, account.getBalance());
        assertEquals(BankUtils.formatMoney(expected, Currency.USD), account.getFormattedBalance());
    }

    // ------------------------------------------------------------------------
    // Deposit and withdraw
    // ------------------------------------------------------------------------

    @Test
    void givenNegativeDeposit_whenDeposit_thenFails() {
        assertFalse(account.deposit(-50));
        assertEquals(0, account.getBalance());
    }

    @Test
    void givenPositiveDeposit_whenDeposit_thenBalanceIncreases() {
        assertTrue(account.deposit(200));
        assertEquals(200, account.getBalance());
    }

    @Test
    void givenInsufficientBalance_whenWithdraw_thenFails() {
        assertFalse(account.withdraw(50));
        assertEquals(0, account.getBalance());
    }

    @Test
    void givenValidAmount_whenWithdraw_thenBalanceDecreases() {
        account.setBalance(200);
        assertTrue(account.withdraw(75));
        assertEquals(125, account.getBalance());
    }

    // ------------------------------------------------------------------------
    // Transaction history
    // ------------------------------------------------------------------------

    @Test
    void whenDeposit_thenTransactionRecordedCorrectly() throws NoTransactionException {
        account.deposit(100);

        Transaction tx = account.getLatestTransaction();
        assertEquals(TransactionType.DEPOSIT, tx.type());
        assertEquals(BankUtils.formatMoney(100, Currency.GBP), tx.amount());
        assertEquals(BankUtils.formatMoney(100, Currency.GBP), tx.balanceAfter());
    }

    @Test
    void whenWithdraw_thenTransactionRecordedCorrectly() throws NoTransactionException {
        account.setBalance(100);
        account.withdraw(40);

        Transaction tx = account.getLatestTransaction();
        assertEquals(TransactionType.WITHDRAW, tx.type());
        assertEquals(BankUtils.formatMoney(40, Currency.GBP), tx.amount());
        assertEquals(BankUtils.formatMoney(60, Currency.GBP), tx.balanceAfter());
    }

    @Test
    void whenCurrencyChanged_thenCurrencyChangeTransactionRecorded() throws NoTransactionException {
        account.setBalance(80);
        account.setCurrency(Currency.USD, false);

        Transaction tx = account.getLatestTransaction();
        assertEquals(TransactionType.CURRENCY_CHANGE, tx.type());
        assertEquals(BankUtils.formatMoney(80, Currency.GBP), tx.amount());
        assertEquals(BankUtils.formatMoney(80, Currency.USD), tx.balanceAfter());
    }

    // ------------------------------------------------------------------------
    // Transaction filtering
    // ------------------------------------------------------------------------

    @Test
    void whenFilterTransactionsByTime_thenCorrectSubsetReturned() {
        account.deposit(100);
        LocalDateTime first = LocalDateTime.now();

        account.deposit(200);
        LocalDateTime second = LocalDateTime.now();

        account.withdraw(50);
        LocalDateTime third = LocalDateTime.now();

        // No filter → all
        List<?> all = account.filterTransactionsByDateTime(null, null);
        assertEquals(3, all.size());

        // After second deposit → last only
        List<?> beforeSecond = account.filterTransactionsByDateTime(second, null);
        assertEquals(1, beforeSecond.size());

        // Before second deposit → first only
        List<?> afterSecond = account.filterTransactionsByDateTime(null, second);
        assertEquals(2, afterSecond.size());

        // Between first and third → middle transaction(s)
        List<?> between = account.filterTransactionsByDateTime(first, third);
        assertFalse(between.isEmpty());
    }

    // ------------------------------------------------------------------------
    // Error handling
    // ------------------------------------------------------------------------

    @Test
    void givenNoTransactions_whenGetLatestTransaction_thenThrowsException() {
        assertThrows(NoTransactionException.class, () -> account.getLatestTransaction());
    }
}
