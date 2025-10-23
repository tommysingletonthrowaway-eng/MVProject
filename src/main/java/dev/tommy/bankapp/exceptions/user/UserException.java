package dev.tommy.bankapp.exceptions.user;

import dev.tommy.bankapp.exceptions.BankingAppException;

public class UserException extends BankingAppException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
