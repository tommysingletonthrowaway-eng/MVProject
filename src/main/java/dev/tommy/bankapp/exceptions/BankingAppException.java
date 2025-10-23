package dev.tommy.bankapp.exceptions;

public class BankingAppException extends Exception {
    public BankingAppException(String message) {
        super(message);
    }

    public BankingAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
