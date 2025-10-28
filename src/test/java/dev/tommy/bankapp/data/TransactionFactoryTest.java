package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.transaction.Transaction;
import dev.tommy.bankapp.data.transaction.TransactionFactory;
import dev.tommy.bankapp.data.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFactoryTest {

    @Test
    void deposit() {
        String amount = "£20.00";
        String balanceAfter = "£156.52";
        Transaction transaction = TransactionFactory.deposit(amount, balanceAfter);

        assertEquals(TransactionType.DEPOSIT, transaction.type());
        assertEquals(amount, transaction.amount());
        assertEquals(balanceAfter, transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }

    @Test
    void withdraw() {
        String amount = "£20.00";
        String balanceAfter = "£1.52";
        Transaction transaction = TransactionFactory.withdraw(amount, balanceAfter);

        assertEquals(TransactionType.WITHDRAW, transaction.type());
        assertEquals(amount, transaction.amount());
        assertEquals(balanceAfter, transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }

    @Test
    void currencyChange() {
        String amount = "$20.00";
        String balanceAfter = "£1.52";
        Transaction transaction = TransactionFactory.currencyChange(amount, balanceAfter);

        assertEquals(TransactionType.CURRENCY_CHANGE, transaction.type());
        assertEquals(amount, transaction.amount());
        assertEquals(balanceAfter, transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }
}