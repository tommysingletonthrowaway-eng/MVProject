package dev.tommy.bankapp.exceptions.account;

public class NoTransactionException extends AccountException {
    public NoTransactionException() {
        super("No transaction found.");
    }
}
