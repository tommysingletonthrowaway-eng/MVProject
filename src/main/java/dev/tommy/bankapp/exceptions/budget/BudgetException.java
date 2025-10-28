package dev.tommy.bankapp.exceptions.budget;

import dev.tommy.bankapp.exceptions.BankingAppException;

public class BudgetException extends BankingAppException {
    public BudgetException(String message) {
        super(message);
    }
}
