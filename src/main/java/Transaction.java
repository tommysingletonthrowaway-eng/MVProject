import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

// Record means each input not has a get accessor, and the constructor is auto created for each field
// No values can be set after the fact however
// Like a struct but still Class based with interfaces
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

