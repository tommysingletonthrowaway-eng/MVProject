package dev.tommy.bankapp.data;

import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankAccount implements Serializable {
    private String identifier;
    private double balance;
    private Currency currency;
    private final List<Transaction> transactionHistory;


    public BankAccount(String identifier, Currency currency) {
        this.identifier = identifier;
        this.currency = currency;
        this.balance = 0.00;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return this.balance;
    }

    public String getFormattedBalance() {
        return BankUtils.formatMoney(this.balance, this.currency);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean deposit(double amount) {
        if (amount < 0) {
            return false;
        }

        this.balance += amount;

        String formattedAmount = BankUtils.formatMoney(amount, this.currency);
        Transaction transaction = TransactionFactory.deposit(formattedAmount, getFormattedBalance());
        this.transactionHistory.add(transaction);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount > this.balance || amount < 0) {
            return false;
        }

        this.balance -= amount;

        String formattedAmount = BankUtils.formatMoney(amount, this.currency);
        Transaction transaction = TransactionFactory.withdraw(formattedAmount, getFormattedBalance());
        this.transactionHistory.add(transaction);
        return true;
    }

    public void setCurrency(Currency toCurrency, boolean convertBalance) {
        String oldFormattedBalance = getFormattedBalance();
        if (convertBalance) {
            setBalance(CurrencyConverter.convert(this.balance, this.currency, toCurrency));
        }

        this.currency = toCurrency;

        Transaction transaction = TransactionFactory.currencyChange(oldFormattedBalance, getFormattedBalance());
        this.transactionHistory.add(transaction);
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public List<Transaction> getTransactionHistory() {
        return this.transactionHistory;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}
