package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    private User user;
    private BankAccount natwestAccount;
    private BankAccount hsbcAccount;

    @BeforeEach
    void setUp() {
        user = new User("John", "password");
        natwestAccount = new BankAccount("Natwest", Currency.GBP);
        hsbcAccount = new BankAccount("HSBC", Currency.GBP);
    }

    // ───────────────────────────
    // User Credentials Tests
    // ───────────────────────────

    @Test
    @DisplayName("Should return correct username")
    void testGetUsername() {
        assertEquals("John", user.getUsername());
        assertNotEquals("Jim", user.getUsername());
    }

    @Test
    @DisplayName("Should change password successfully")
    void testChangePassword() {
        String newPassword = "newPassword";

        assertTrue(user.checkPassword("password"), "Original password should be valid");

        user.changePassword(newPassword);

        assertTrue(user.checkPassword(newPassword), "New password should be valid");
        assertFalse(user.checkPassword("password"), "Old password should no longer be valid");
    }

    @Test
    @DisplayName("Should change username successfully")
    void testChangeUsername() {
        String newUsername = "Dave";

        assertEquals("John", user.getUsername());

        user.changeUsername(newUsername);

        assertEquals(newUsername, user.getUsername());
        assertNotEquals("John", user.getUsername());
    }

    @Test
    @DisplayName("Should verify password correctly")
    void testCheckPassword() {
        assertTrue(user.checkPassword("password"));
        assertFalse(user.checkPassword("wrongPassword"));
    }

    // ───────────────────────────
    // Bank Account Tests
    // ───────────────────────────

    @Test
    @DisplayName("Should add and retrieve bank accounts correctly")
    void testAddAndGetBankAccounts() {
        assertTrue(user.getBankAccounts().isEmpty());

        user.addBankAccount(natwestAccount);
        user.addBankAccount(hsbcAccount);

        List<BankAccount> accounts = user.getBankAccounts();
        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(natwestAccount));
        assertTrue(accounts.contains(hsbcAccount));
    }

    @Test
    @DisplayName("Should retrieve bank account by name or index")
    void testGetBankAccount() throws BankAccountNotFoundException {
        user.addBankAccount(natwestAccount);

        // By name
        assertEquals(natwestAccount, user.getBankAccount("Natwest"));

        // Invalid name should throw exception
        assertThrows(BankAccountNotFoundException.class, () -> user.getBankAccount("Barclays"));

        // By index
        assertEquals(natwestAccount, user.getBankAccount(0));

        // Invalid index should throw exception
        assertThrows(BankAccountNotFoundException.class, () -> user.getBankAccount(3));
    }

    @Test
    @DisplayName("Should confirm existence of account by name")
    void testHasAccountNamed() {
        user.addBankAccount(natwestAccount);

        assertTrue(user.hasAccountNamed("Natwest"));
        assertFalse(user.hasAccountNamed("HSBC"));
    }

    @Test
    @DisplayName("Should remove bank account successfully")
    void testRemoveBankAccount() throws BankAccountNotFoundException {
        user.addBankAccount(natwestAccount);
        assertTrue(user.hasAccountNamed("Natwest"));

        user.removeBankAccount(natwestAccount);

        assertFalse(user.hasAccountNamed("Natwest"));
        assertTrue(user.getBankAccounts().isEmpty());
    }

    @Test
    @DisplayName("Should throw when removing non-existent account")
    void testRemoveNonExistentBankAccountThrows() {
        assertThrows(BankAccountNotFoundException.class, () -> user.removeBankAccount(natwestAccount));
    }
}
