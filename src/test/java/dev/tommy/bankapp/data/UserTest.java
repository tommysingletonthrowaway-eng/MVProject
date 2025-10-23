package dev.tommy.bankapp.data;

import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void getUsername() {
        User user = new User("John", "password");
        assertEquals("John", user.getUsername());
        assertNotEquals("Jim", user.getUsername());
    }

    @Test
    void changePassword() {
        String originalPassword = "password";
        User user = new User("John", originalPassword);
        assertTrue(user.checkPassword(originalPassword));

        String newPassword = "newPassword";
        user.changePassword(newPassword);
        assertTrue(user.checkPassword(newPassword));
        assertFalse(user.checkPassword(originalPassword));
    }

    @Test
    void changeUsername() {
        String originalUsername = "John";
        User user = new User(originalUsername, "password");
        assertEquals(originalUsername, user.getUsername());

        String newUsername = "Dave";
        user.changeUsername(newUsername);
        assertNotEquals(originalUsername, user.getUsername());
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    void checkPassword() {
        String password = "password";
        User user = new User("John", password);
        assertTrue(user.checkPassword(password));
        assertFalse(user.checkPassword("pringles"));
    }

    @Test
    void getBankAccount() {
        User user = new User("John", "password");
        BankAccount bankAccount = new BankAccount("Natwest", Currency.GBP);
        user.addBankAccount(bankAccount);

        try {
            assertEquals(bankAccount, user.getBankAccount("Natwest"));
        } catch (BankAccountNotFoundException e) {
            fail();
        }

        try {
            assertNull(user.getBankAccount("HSBC"));
        } catch (BankAccountNotFoundException _) { }

        try {
            assertEquals(bankAccount, user.getBankAccount(0));
        } catch (BankAccountNotFoundException e) {
            fail();
        }

        try {
            assertNull(user.getBankAccount(3));
        } catch (BankAccountNotFoundException _) { }
    }

    @Test
    void getBankAccounts() {
        User user = new User("John", "password");
        BankAccount bankAccount1 = new BankAccount("Natwest", Currency.GBP);
        BankAccount bankAccount2 = new BankAccount("Natwest", Currency.GBP);

        var accounts = user.getBankAccounts();
        assertFalse(accounts.contains(bankAccount1));
        assertFalse(accounts.contains(bankAccount2));
        user.addBankAccount(bankAccount1);
        user.addBankAccount(bankAccount2);

        var newAccounts = user.getBankAccounts();
        assertEquals(2, newAccounts.size());
        assertTrue(newAccounts.contains(bankAccount1));
        assertTrue(newAccounts.contains(bankAccount2));
    }

    @Test
    void addBankAccount() {
        User user = new User("John", "password");
        BankAccount bankAccount = new BankAccount("Natwest", Currency.GBP);

        assertFalse(user.getBankAccounts().contains(bankAccount));
        user.addBankAccount(bankAccount);
        assertEquals(1, user.getBankAccounts().size());
        assertTrue(user.getBankAccounts().contains(bankAccount));

    }

    @Test
    void hasAccountNamed() {
        User user = new User("John", "password");
        BankAccount bankAccount = new BankAccount("Natwest", Currency.GBP);
        user.addBankAccount(bankAccount);

        assertTrue(user.hasAccountNamed("Natwest"));
        assertFalse(user.hasAccountNamed("HSBC"));
    }

    @Test
    void deleteBankAccount() {
        User user = new User("John", "password");
        BankAccount bankAccount = new BankAccount("Natwest", Currency.GBP);
        user.addBankAccount(bankAccount);

        assertTrue(user.hasAccountNamed("Natwest"));
        try {
            user.removeBankAccount(bankAccount);
        } catch (BankAccountNotFoundException e) {
            fail();
        }
        assertFalse(user.hasAccountNamed("Natwest"));
    }
}