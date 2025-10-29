package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.budget.BudgetManager;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final UUID uuid;
    public final BudgetManager budgetManager;
    private final List<BankAccount> bankAccounts;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.budgetManager = new BudgetManager();
        this.bankAccounts = new ArrayList<>();
    }

    // --- Bank Accounts ---

    public List<BankAccount> getBankAccounts() {
        return Collections.unmodifiableList(bankAccounts);
    }

    public void addBankAccount(BankAccount account) {
        this.bankAccounts.add(account);
    }

    public void removeBankAccount(BankAccount account) throws BankAccountNotFoundException {
        if (!bankAccounts.contains(account)) {
            throw new BankAccountNotFoundException(account.getIdentifier());
        }
        this.bankAccounts.remove(account);
    }

    public BankAccount getBankAccount(int index) throws BankAccountNotFoundException {
        if (index < 0 || index >= bankAccounts.size()) {
            throw new BankAccountNotFoundException("No account exists at index " + index);
        }
        return bankAccounts.get(index);
    }

    public BankAccount getBankAccount(String name) throws BankAccountNotFoundException {
        return bankAccounts.stream()
                .filter(acc -> Objects.equals(acc.getIdentifier(), name))
                .findFirst()
                .orElseThrow(() -> new BankAccountNotFoundException(name));
    }

    public boolean hasAccountNamed(String accountName) {
        return bankAccounts.stream()
                .anyMatch(acc -> Objects.equals(acc.getIdentifier(), accountName));
    }

    public UUID getUuid() {
        return uuid;
    }
}
