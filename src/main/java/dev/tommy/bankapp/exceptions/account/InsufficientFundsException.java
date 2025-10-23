package dev.tommy.bankapp.exceptions.account;

public class InsufficientFundsException extends AccountException {
    public InsufficientFundsException(double requested, double available) {
        super("Insufficient funds: tried to withdraw " + requested + ", but only " + available + " available.");
    }
}
