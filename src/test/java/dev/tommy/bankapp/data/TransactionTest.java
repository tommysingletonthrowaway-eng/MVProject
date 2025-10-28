package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.transaction.Transaction;
import dev.tommy.bankapp.data.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    void createTransactionTest() {
        TransactionType type = TransactionType.DEPOSIT;
        String amount = "£100.00";
        String balanceAfter = "£120.22";
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, balanceAfter, LocalDateTime.now());

        assertEquals(type, transaction.type());
        assertEquals(amount, transaction.amount());
        assertEquals(balanceAfter, transaction.balanceAfter());
        assertNotNull(transaction.timestamp());
    }
}