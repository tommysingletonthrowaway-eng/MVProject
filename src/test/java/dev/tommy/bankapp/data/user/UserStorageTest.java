package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.transaction.TransactionType;
import dev.tommy.bankapp.encryption.EncryptionStrategy;
import dev.tommy.bankapp.encryption.NoEncryption;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserStorage Tests")
class UserStorageTest {

    @TempDir
    Path tempDir;

    // ───────────────────────────────
    // Helper Methods
    // ───────────────────────────────

    static Stream<EncryptionStrategy> encryptionStrategies() {
        return Stream.of(
                new NoEncryption(),
                new SimpleXOREncryption("SimpleKey")
        );
    }

    private User createUserWithAccount() {
        User user = new User("John", "password");
        BankAccount account = new BankAccount("Natwest", Currency.GBP);
        user.addBankAccount(account);
        account.deposit(1000);
        account.withdraw(200);
        return user;
    }

    private File getTempFile(String name) {
        return tempDir.resolve(name).toFile();
    }

    private void verifyUserRestoredCorrectly(User loadedUser) {
        assertEquals("John", loadedUser.getUsername(), "Username should match original");
        assertTrue(loadedUser.checkPassword("password"), "Password should be correctly loaded");
        assertTrue(loadedUser.hasAccountNamed("Natwest"), "Account name should persist correctly");

        try {
            BankAccount account = loadedUser.getBankAccount(0);
            assertEquals(800, account.getBalance(), "Balance should reflect persisted transactions");
            assertEquals(Currency.GBP, account.getCurrency(), "Currency should be GBP");
        } catch (BankAccountNotFoundException e) {
            fail("Expected bank account not found after load", e);
        }
    }

    // ───────────────────────────────
    // Parameterized Encryption Tests
    // ───────────────────────────────

    @ParameterizedTest(name = "Should save and load user with {0}")
    @MethodSource("encryptionStrategies")
    void testSaveAndLoad_WithEncryption(EncryptionStrategy encryption) {
        File file = getTempFile(encryption.getClass().getSimpleName() + ".dat");
        UserStorage storage = new UserStorage(file.getAbsolutePath(), encryption);

        User user = createUserWithAccount();
        storage.saveUsers(Set.of(user));

        Collection<User> loadedUsers = storage.loadUsers();
        assertEquals(1, loadedUsers.size(), "One user should be loaded");

        verifyUserRestoredCorrectly(loadedUsers.iterator().next());
    }

    // ───────────────────────────────
    // Additional Focused Tests
    // ───────────────────────────────

    @Test
    @DisplayName("Should load empty collection when file is missing or empty")
    void testLoadEmptyFile() {
        File file = getTempFile("empty.dat");
        UserStorage storage = new UserStorage(file.getAbsolutePath(), new NoEncryption());

        Collection<User> users = storage.loadUsers();

        assertTrue(users.isEmpty(), "Expected no users to be loaded from empty file");
    }

    @Test
    @DisplayName("Should preserve transaction history after saving and loading")
    void testTransactionHistoryPreservedAfterLoad() throws Exception {
        File file = getTempFile("txHistory.dat");
        UserStorage storage = new UserStorage(file.getAbsolutePath(), new NoEncryption());

        User user = createUserWithAccount();
        storage.saveUsers(Set.of(user));

        User loadedUser = storage.loadUsers().iterator().next();
        BankAccount loadedAccount = loadedUser.getBankAccount(0);

        assertEquals(2, loadedAccount.getTransactionHistory().size(), "Expected 2 transactions (deposit & withdraw)");
        assertEquals(TransactionType.WITHDRAW, loadedAccount.getLatestTransaction().type());
    }
}
