package dev.tommy.bankapp.data;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {
    @Test
    void saveAndLoadTest() {
        Set<User> savedUsers = new HashSet<>();
        User john = new User("John", "password");
        savedUsers.add(john);

        BankAccount natwest = new BankAccount("Natwest", Currency.GBP);
        john.addBankAccount(natwest);

        natwest.deposit(1000);
        natwest.withdraw(200);

        String filePath = "usersTest.dat";
        UserStorage.saveUsers(filePath, savedUsers);

        Set<User> loadedUsers = UserStorage.loadUsers(filePath);
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