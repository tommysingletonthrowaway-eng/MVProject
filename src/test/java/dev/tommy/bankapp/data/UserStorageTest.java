package dev.tommy.bankapp.data;

import dev.tommy.bankapp.encryption.EncryptionStrategy;
import dev.tommy.bankapp.encryption.NoEncryption;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import org.junit.jupiter.api.Test;

import javax.security.auth.kerberos.EncryptionKey;
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
        UserStorage storage = new UserStorage(encryptionStrategy);

        Set<User> savedUsers = new HashSet<>();
        User john = new User("John", "password");
        savedUsers.add(john);

        BankAccount natwest = new BankAccount("Natwest", Currency.GBP);
        john.addBankAccount(natwest);

        natwest.deposit(1000);
        natwest.withdraw(200);

        String filePath = "usersTest.dat";
        storage.saveUsers(filePath, savedUsers);

        Set<User> loadedUsers = storage.loadUsers(filePath);
        assertNotNull(loadedUsers);
        assertFalse(loadedUsers.isEmpty());
        User loadedJohn = loadedUsers.stream().findFirst().get();

        assertEquals(loadedJohn.getUsername(), john.getUsername());
        assertTrue(loadedJohn.checkPassword("password"));
        assertTrue(loadedJohn.hasAccountNamed("Natwest"));
        BankAccount loadedBankAccount = loadedJohn.getBankAccount(0);
        assertEquals(800, loadedBankAccount.getBalance());
        assertEquals(Currency.GBP, loadedBankAccount.getCurrency());
        assertFalse(loadedBankAccount.getTransactionHistory().isEmpty());
        assertEquals(TransactionType.DEPOSIT, loadedBankAccount.getTransactionHistory().getFirst().type());
    }
}