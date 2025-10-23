package dev.tommy.bankapp.exceptions.account;

import dev.tommy.bankapp.exceptions.BankingAppException;

public class AccountException extends BankingAppException {
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
