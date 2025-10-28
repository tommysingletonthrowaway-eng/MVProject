package dev.tommy.bankapp.data.transaction;

import java.time.LocalDateTime;

public class TransactionFactory {
    public static Transaction deposit(String amount, String newBalance) {
        return new Transaction(TransactionType.DEPOSIT, amount, newBalance, LocalDateTime.now());
    }

    public static Transaction withdraw(String amount, String newBalance) {
        return new Transaction(TransactionType.WITHDRAW, amount, newBalance, LocalDateTime.now());
    }

    public static Transaction currencyChange(String prevBalance, String newBalance) {
        return new Transaction(TransactionType.CURRENCY_CHANGE, prevBalance, newBalance, LocalDateTime.now());
    }
}
