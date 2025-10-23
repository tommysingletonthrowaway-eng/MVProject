package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import dev.tommy.bankapp.exceptions.bankaccount.DuplicateBankAccountException;
import dev.tommy.bankapp.exceptions.bankaccount.InvalidBankAccountNameException;
import dev.tommy.bankapp.data.Currency;

import java.util.List;

public interface BankAccountService {
    BankAccount createAccount(User user, String accountName, Currency currency) throws DuplicateBankAccountException, InvalidBankAccountNameException;
    void deleteAccount(User user, String accountName) throws BankAccountNotFoundException;
    BankAccount getAccount(User user, String accountName) throws BankAccountNotFoundException;
    List<BankAccount> getAccounts(User user);
}
