package dev.tommy.bankapp.exceptions.account;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String accountId) {
        super("Account with ID '" + accountId + "' was not found.");
    }
}

