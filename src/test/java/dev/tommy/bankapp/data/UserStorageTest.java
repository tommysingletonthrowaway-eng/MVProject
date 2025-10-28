package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.transaction.TransactionType;
import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.data.user.UserStorage;
import dev.tommy.bankapp.encryption.EncryptionStrategy;
import dev.tommy.bankapp.encryption.NoEncryption;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import dev.tommy.bankapp.exceptions.account.NoTransactionException;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
    @Test
    void saveAndLoadTest() {
        saveAndLoadWithEncryption(new NoEncryption());
        saveAndLoadWithEncryption(new SimpleXOREncryption("SimpleKey"));
    }

    void saveAndLoadWithEncryption(EncryptionStrategy encryptionStrategy) {
        String filePath = "usersTest.dat";
        UserStorage storage = new UserStorage(filePath, encryptionStrategy);

        Set<User> savedUsers = new HashSet<>();
        User john = new User("John", "password");
        savedUsers.add(john);

        BankAccount natwest = new BankAccount("Natwest", Currency.GBP);
        john.addBankAccount(natwest);

        natwest.deposit(1000);
        natwest.withdraw(200);

        storage.saveUsers(savedUsers);

        Set<User> loadedUsers = storage.loadUsers();
        assertNotNull(loadedUsers);
        assertFalse(loadedUsers.isEmpty());
        User loadedJohn = loadedUsers.stream().findFirst().get();

        assertEquals(loadedJohn.getUsername(), john.getUsername());
        assertTrue(loadedJohn.checkPassword("password"));
        assertTrue(loadedJohn.hasAccountNamed("Natwest"));
        BankAccount loadedBankAccount = null;
        try {
            loadedBankAccount = loadedJohn.getBankAccount(0);
        } catch (BankAccountNotFoundException e) {
            fail();
        }
        assertEquals(800, loadedBankAccount.getBalance());
        assertEquals(Currency.GBP, loadedBankAccount.getCurrency());
        assertFalse(loadedBankAccount.getTransactionHistory().isEmpty());
        try {
            assertEquals(TransactionType.WITHDRAW, loadedBankAccount.getLatestTransaction().type());
        } catch (NoTransactionException e) {
            fail("No transaction found.");
        }
    }
}