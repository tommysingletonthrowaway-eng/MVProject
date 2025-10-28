package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.budget.BudgetManager;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import dev.tommy.bankapp.validator.Validator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    public final BudgetManager budgetManager;
    private final List<BankAccount> bankAccounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.budgetManager = new BudgetManager();
        this.bankAccounts = new ArrayList<>();
    }

    // --- Username and Password ---

    public String getUsername() {
        return username;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean validatePassword(Validator validator) {
        return validator.isValid(password);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
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

    // --- Equality based on username ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return username;
    }
}
