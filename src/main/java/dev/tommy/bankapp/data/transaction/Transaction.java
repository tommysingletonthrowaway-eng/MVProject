package dev.tommy.bankapp.data.transaction;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record Transaction(TransactionType type, String amount, String balanceAfter,
                          LocalDateTime timestamp) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Transaction {
        if (amount == null || balanceAfter == null) {
            throw new IllegalArgumentException("Amount and balanceAfter cannot be null");
        }

        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s -> Balance: %s",
                timestamp, type, amount, balanceAfter);
    }
}

