package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import dev.tommy.bankapp.exceptions.bankaccount.DuplicateBankAccountException;
import dev.tommy.bankapp.exceptions.bankaccount.InvalidBankAccountNameException;
import dev.tommy.bankapp.validator.Validator;

import java.util.List;

public class BankAccountServiceImpl implements BankAccountService {
    private final Validator accountNameValidator;

    public BankAccountServiceImpl(Validator accountNameValidator) {
        this.accountNameValidator = accountNameValidator;
    }

    @Override
    public BankAccount createAccount(User user, String accountName, Currency currency) throws DuplicateBankAccountException, InvalidBankAccountNameException {
        if (!accountNameValidator.isValid(accountName)) {
            throw new InvalidBankAccountNameException(accountNameValidator.getErrorMessage());
        }

        if (user.hasAccountNamed(accountName)) {
            throw new DuplicateBankAccountException(accountName);
        }

        BankAccount account = new BankAccount(accountName, currency);
        user.addBankAccount(account);
        return account;
    }

    @Override
    public void deleteAccount(User user, String accountName) throws BankAccountNotFoundException {
        BankAccount account = user.getBankAccount(accountName);
        user.removeBankAccount(account);
    }

    @Override
    public BankAccount getAccount(User user, String accountName) throws BankAccountNotFoundException {
        return user.getBankAccount(accountName);
    }

    public BankAccount getAccount(User user, int index) throws BankAccountNotFoundException {
        return user.getBankAccount(index);
    }

    @Override
    public List<BankAccount> getAccounts(User user) {
        return user.getBankAccounts();
    }
}
