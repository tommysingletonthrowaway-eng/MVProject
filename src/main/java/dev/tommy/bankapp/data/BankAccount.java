package dev.tommy.bankapp.data;

import dev.tommy.bankapp.exceptions.account.NoTransactionException;
import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class BankAccount implements Serializable {
    private String identifier;
    private double balance;
    private Currency currency;
    private final Map<LocalDateTime, Transaction> transactionHistory;


    public BankAccount(String identifier, Currency currency) {
        this.identifier = identifier;
        this.currency = currency;
        this.balance = 0.00;
        this.transactionHistory = new TreeMap<>();
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
        this.transactionHistory.put(transaction.timestamp(), transaction);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount > this.balance || amount < 0) {
            return false;
        }

        this.balance -= amount;

        String formattedAmount = BankUtils.formatMoney(amount, this.currency);
        Transaction transaction = TransactionFactory.withdraw(formattedAmount, getFormattedBalance());
        this.transactionHistory.put(transaction.timestamp(), transaction);
        return true;
    }

    public void setCurrency(Currency toCurrency, boolean convertBalance) {
        String oldFormattedBalance = getFormattedBalance();
        if (convertBalance) {
            setBalance(CurrencyConverter.convert(this.balance, this.currency, toCurrency));
        }

        this.currency = toCurrency;

        Transaction transaction = TransactionFactory.currencyChange(oldFormattedBalance, getFormattedBalance());
        this.transactionHistory.put(transaction.timestamp(), transaction);
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Map<LocalDateTime, Transaction> getTransactionHistory() {
        return this.transactionHistory;
    }

    public List<Map.Entry<LocalDateTime, Transaction>> filterTransactionsByDateTime(
            LocalDateTime start, LocalDateTime end) {

        TreeMap<LocalDateTime, Transaction> map = (TreeMap<LocalDateTime, Transaction>) transactionHistory;
        NavigableMap<LocalDateTime, Transaction> filtered;

        if (start == null && end == null) {
            filtered = map;
        } else if (start == null) {
            filtered = map.headMap(end, true);
        } else if (end == null) {
            filtered = map.tailMap(start, true);
        } else {
            filtered = map.subMap(start, true, end, true);
        }

        return new ArrayList<>(filtered.entrySet());
    }

    public LocalDateTime getLatestTransactionTime() throws NoTransactionException {
        if (transactionHistory.isEmpty()) {
            throw new NoTransactionException();
        }

        return ((TreeMap<LocalDateTime, Transaction>) transactionHistory).lastKey();
    }

    public Transaction getLatestTransaction() throws NoTransactionException {
        LocalDateTime lastTime = getLatestTransactionTime();
        if (lastTime == null) return null;
        return transactionHistory.get(lastTime);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Override
    public String toString() {
        return getIdentifier();
    }

    public int getTransactionHistorySize() { return transactionHistory.size(); }
}
