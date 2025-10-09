package dev.tommy.bankapp.data;

import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;
import org.junit.jupiter.api.Test;

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
    void TestTransactionHistory() {
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        assertEquals(0, account.getTransactionHistory().size());

        Transaction transaction;

        account.deposit(100);
        transaction = account.getTransactionHistory().get(0);
        assertEquals(TransactionType.DEPOSIT, transaction.type());
        assertEquals(BankUtils.formatMoney(100, account.getCurrency()), transaction.amount());
        assertEquals(BankUtils.formatMoney(100, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());

        account.withdraw(20);
        transaction = account.getTransactionHistory().get(1);
        assertEquals(TransactionType.WITHDRAW, transaction.type());
        assertEquals(BankUtils.formatMoney(20, account.getCurrency()), transaction.amount());
        assertEquals(BankUtils.formatMoney(80, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());

        account.setCurrency(Currency.USD, false);
        transaction = account.getTransactionHistory().get(2);
        assertEquals(TransactionType.CURRENCY_CHANGE, transaction.type());
        assertEquals(BankUtils.formatMoney(80, Currency.GBP), transaction.amount());
        assertEquals(BankUtils.formatMoney(80, account.getCurrency()), transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }
}