package dev.tommy.bankapp.data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record Transaction(TransactionType type, String amount, String balanceAfter,
                          LocalDateTime timestamp) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return String.format("[%s] %s: %s -> Balance: %s",
                timestamp, type, amount, balanceAfter);
    }
}

