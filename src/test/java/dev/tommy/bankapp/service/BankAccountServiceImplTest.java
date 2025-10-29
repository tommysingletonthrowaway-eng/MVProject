package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.exceptions.bankaccount.*;
import dev.tommy.bankapp.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceImplTest {

    private Validator accountNameValidator;
    private BankAccountServiceImpl bankAccountService;
    private User user;

    @BeforeEach
    void setUp() {
        accountNameValidator = mock(Validator.class);
        bankAccountService = new BankAccountServiceImpl(accountNameValidator);
        user = new User(UUID.randomUUID());
    }

    @Test
    void createAccountShouldSucceed() throws Exception {
        when(accountNameValidator.isValid("Savings")).thenReturn(true);

        BankAccount account = bankAccountService.createAccount(user, "Savings", Currency.USD);

        assertEquals("Savings", account.getIdentifier());
        assertEquals(Currency.USD, account.getCurrency());
        assertTrue(user.hasAccountNamed("Savings"));
    }

    @Test
    void createAccountInvalidNameShouldThrow() {
        when(accountNameValidator.isValid("Invalid!")).thenReturn(false);
        when(accountNameValidator.getErrorMessage()).thenReturn("Invalid account name");

        InvalidBankAccountNameException exception = assertThrows(
                InvalidBankAccountNameException.class,
                () -> bankAccountService.createAccount(user, "Invalid!", Currency.USD)
        );

        assertEquals("Invalid account name", exception.getMessage());
    }

    @Test
    void createDuplicateAccountShouldThrow() throws Exception {
        when(accountNameValidator.isValid("Savings")).thenReturn(true);
        bankAccountService.createAccount(user, "Savings", Currency.USD);

        assertThrows(DuplicateBankAccountException.class,
                () -> bankAccountService.createAccount(user, "Savings", Currency.USD));
    }

    @Test
    void deleteAccountShouldSucceed() throws Exception {
        when(accountNameValidator.isValid("Checking")).thenReturn(true);
        BankAccount account = bankAccountService.createAccount(user, "Checking", Currency.EUR);

        bankAccountService.deleteAccount(user, "Checking");

        assertFalse(user.hasAccountNamed("Checking"));
    }

    @Test
    void deleteNonexistentAccountShouldThrow() {
        assertThrows(BankAccountNotFoundException.class,
                () -> bankAccountService.deleteAccount(user, "NonExistent"));
    }

    @Test
    void getAccountByNameShouldReturnAccount() throws Exception {
        when(accountNameValidator.isValid("Investments")).thenReturn(true);
        BankAccount account = bankAccountService.createAccount(user, "Investments", Currency.GBP);

        BankAccount found = bankAccountService.getAccount(user, "Investments");
        assertEquals(account, found);
    }

    @Test
    void getAccountByNameNonexistentShouldThrow() {
        assertThrows(BankAccountNotFoundException.class,
                () -> bankAccountService.getAccount(user, "FakeAccount"));
    }

    @Test
    void getAccountByIndexShouldReturnAccount() throws Exception {
        when(accountNameValidator.isValid("Main")).thenReturn(true);
        BankAccount account = bankAccountService.createAccount(user, "Main", Currency.USD);

        BankAccount found = bankAccountService.getAccount(user, 0);
        assertEquals(account, found);
    }

    @Test
    void getAccountsShouldReturnAllAccounts() throws Exception {
        when(accountNameValidator.isValid("A")).thenReturn(true);
        when(accountNameValidator.isValid("B")).thenReturn(true);

        BankAccount a = bankAccountService.createAccount(user, "A", Currency.USD);
        BankAccount b = bankAccountService.createAccount(user, "B", Currency.EUR);

        List<BankAccount> accounts = bankAccountService.getAccounts(user);

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(a));
        assertTrue(accounts.contains(b));
    }
}
