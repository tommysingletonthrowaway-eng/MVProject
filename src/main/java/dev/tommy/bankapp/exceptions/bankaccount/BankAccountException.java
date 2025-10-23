package dev.tommy.bankapp.exceptions.bankaccount;

import dev.tommy.bankapp.exceptions.BankingAppException;
import dev.tommy.bankapp.exceptions.account.AccountException;
import dev.tommy.bankapp.exceptions.user.UserException;

public class BankAccountException extends BankingAppException {
    public BankAccountException(String message) {
        super(message);
    }

    public BankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
