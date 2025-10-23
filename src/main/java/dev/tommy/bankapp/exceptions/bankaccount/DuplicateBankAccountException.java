package dev.tommy.bankapp.exceptions.bankaccount;

public class DuplicateBankAccountException extends BankAccountException {
    public DuplicateBankAccountException(String message) {
        super(message);
    }
}

