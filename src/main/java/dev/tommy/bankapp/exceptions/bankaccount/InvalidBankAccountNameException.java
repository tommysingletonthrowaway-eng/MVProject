package dev.tommy.bankapp.exceptions.bankaccount;

public class InvalidBankAccountNameException extends BankAccountException {
    public InvalidBankAccountNameException(String message) {
        super(message);
    }
}
