package dev.tommy.bankapp.data.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void givenNullTimestamp_whenCreateTransaction_thenThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(TransactionType.WITHDRAW, "£50.00", "£70.00", null);
        }, "Transaction should throw IllegalArgumentException if timestamp is null");
    }

    @Test
    void givenNullAmount_whenCreateTransaction_thenThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(TransactionType.DEPOSIT, null, "£70.00", LocalDateTime.now());
        }, "Transaction should throw IllegalArgumentException if amount is null");
    }

    @Test
    void givenNullBalanceAfter_whenCreateTransaction_thenThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(TransactionType.DEPOSIT, "£50.00", null, LocalDateTime.now());
        }, "Transaction should throw IllegalArgumentException if balanceAfter is null");
    }
}
