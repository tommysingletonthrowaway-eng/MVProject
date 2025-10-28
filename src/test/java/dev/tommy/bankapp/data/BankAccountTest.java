package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.transaction.Transaction;
import dev.tommy.bankapp.data.transaction.TransactionType;
import dev.tommy.bankapp.exceptions.account.NoTransactionException;
import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    @Test
    void accountCreationTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);

        assertEquals(Currency.GBP, account.getCurrency());
        assertEquals("Natwest", account.getIdentifier());
        assertEquals("Natwest", account.toString());
        assertEquals(0, account.getBalance());
        assertEquals(0, account.getTransactionHistory().size());
    }

    @Test
    void getBalanceTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        assertEquals(0, account.getBalance());
        assertEquals("£0.00", account.getFormattedBalance());
    }

    @Test
    void setBalanceTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        account.setBalance(100);
        assertEquals(100, account.getBalance());
        assertEquals("£100.00", account.getFormattedBalance());
    }

    @Test
    void setCurrencyTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        account.setBalance(100);

        assertEquals(100, account.getBalance());
        assertEquals("£100.00", account.getFormattedBalance());

        account.setCurrency(Currency.USD, false);
        assertEquals(100, account.getBalance());
        assertEquals("$100.00", account.getFormattedBalance());

        account.setCurrency(Currency.GBP, false);
        account.setCurrency(Currency.USD, true);
        double conversionRate = CurrencyConverter.getConversionRate(Currency.GBP, Currency.USD);
        double expectedResult = 100.00 * conversionRate;
        assertEquals(expectedResult, account.getBalance());
        assertEquals(BankUtils.formatMoney(expectedResult, Currency.USD), account.getFormattedBalance());
    }

    @Test
    void withdrawMoneyTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);

        assertFalse(account.withdraw(100));
        account.setBalance(200);
        assertEquals(200, account.getBalance());

        assertFalse(account.withdraw(-100));
        assertTrue(account.withdraw(100));
        assertEquals(100, account.getBalance());
    }

    @Test
    void depositMoneyTest() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);

        assertFalse(account.deposit(-100));
        assertTrue(account.deposit(100));
        assertEquals(100, account.getBalance());
    }

    @Test
    void testTransactionHistory() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        assertEquals(0, account.getTransactionHistory().size());

        Transaction transaction;

        assertTrue(account.deposit(100));
        try {
            transaction = account.getLatestTransaction();
        } catch (NoTransactionException e) {
            fail("No transaction found.");
            return;
        }
        assertEquals(TransactionType.DEPOSIT, transaction.type());
        assertEquals(BankUtils.formatMoney(100, account.getCurrency()), transaction.amount());
        assertEquals(BankUtils.formatMoney(100, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());

        assertTrue(account.withdraw(20));
        try {
            transaction = account.getLatestTransaction();
        } catch (NoTransactionException e) {
            fail("No transaction found.");
            return;
        }
        assertEquals(TransactionType.WITHDRAW, transaction.type());
        assertEquals(BankUtils.formatMoney(20, account.getCurrency()), transaction.amount());
        assertEquals(BankUtils.formatMoney(80, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());

        account.setCurrency(Currency.USD, false);
        try {
            transaction = account.getLatestTransaction();
        } catch (NoTransactionException e) {
            fail("No transaction found.");
            return;
        }
        assertEquals(TransactionType.CURRENCY_CHANGE, transaction.type());
        assertEquals(BankUtils.formatMoney(80, Currency.GBP), transaction.amount());
        assertEquals(BankUtils.formatMoney(80, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }

    @Test
    void testTransactionHistoryFiltering() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        assertEquals(0, account.getTransactionHistory().size());

        // Add some deposits/withdrawals
        account.deposit(100); // timestamp = now
        LocalDateTime startTime = LocalDateTime.now();
        try {
            sleep(100);          // small delay to ensure different timestamps
        } catch (InterruptedException e) {
            fail();
        }
        account.deposit(200); // timestamp = now + 0.1s
        LocalDateTime middleTime = LocalDateTime.now();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            fail();
        }
        account.withdraw(50); // timestamp = now + 0.2s
        LocalDateTime endTime = LocalDateTime.now();

        // Filter everything (no bounds)
        var filtered = account.filterTransactionsByDateTime(null, null);
        assertEquals(3, filtered.size());

        // Filter everything after first transaction
        filtered = account.filterTransactionsByDateTime(middleTime, null);
        assertEquals(1, filtered.size());

        // Filter everything before last transaction
        filtered = account.filterTransactionsByDateTime(null, middleTime);
        assertEquals(2, filtered.size());

        filtered = account.filterTransactionsByDateTime(startTime, endTime);
        assertEquals(2, filtered.size());
        assertEquals(TransactionType.WITHDRAW, filtered.get(1).getValue().type());
    }
}