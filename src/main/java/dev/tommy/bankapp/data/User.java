package dev.tommy.bankapp.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private final List<BankAccount> bankAccounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        bankAccounts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public BankAccount getBankAccount(int index) {
        return (index < 0 || index >= bankAccounts.size())
                ? null : bankAccounts.get(index);
    }

    public BankAccount getBankAccount(String name) {
        return this.bankAccounts.stream()
                .filter(acc -> Objects.equals(acc.getIdentifier(), name))
                .findFirst().orElse(null);
    }

    public List<BankAccount> getBankAccounts() {
        return this.bankAccounts;
    }

    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
    }

//    public boolean hasAccountNamed(String accountName) {
//        for (BankAccount bankAccount : this.bankAccounts) {
//            if (bankAccount.getIdentifier().equals(accountName)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public boolean hasAccountNamed(String accountName) {
        return this.getBankAccounts().stream()
                .anyMatch(bankAccount -> bankAccount.getIdentifier().equals(accountName));
    }

    public void deleteBankAccount(BankAccount account) {
        this.bankAccounts.remove(account);
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
