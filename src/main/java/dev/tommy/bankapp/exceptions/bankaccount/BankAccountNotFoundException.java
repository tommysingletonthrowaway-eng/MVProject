package dev.tommy.bankapp.exceptions.bankaccount;

public class BankAccountNotFoundException extends BankAccountException {
    public BankAccountNotFoundException(String name) {
        super("Bank account with name " + name + " not found.");
    }
}
