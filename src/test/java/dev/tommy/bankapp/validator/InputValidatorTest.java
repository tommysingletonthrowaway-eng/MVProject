package dev.tommy.bankapp.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator usernameValidator;
    private InputValidator passwordValidator;

    @BeforeEach
    void setUp() {
        usernameValidator = new InputValidator(3, 12, "^[a-zA-Z0-9]+$", "Username");
        passwordValidator = new InputValidator(5, 20, "^[a-zA-Z0-9!@#$%^&*()_+=-]+$", "Password");
    }

    // --- NULL or EMPTY ---
    @Test
    void shouldRejectNullInput() {
        assertFalse(usernameValidator.isValid(null));
        assertEquals("Username cannot be empty.", usernameValidator.getErrorMessage());
    }

    @Test
    void shouldRejectEmptyInput() {
        assertFalse(passwordValidator.isValid(""));
        assertEquals("Password cannot be empty.", passwordValidator.getErrorMessage());
    }

    // --- SPACES ---
    @Test
    void shouldRejectInputWithSpaces() {
        assertFalse(usernameValidator.isValid("john doe"));
        assertEquals("Username cannot contain spaces.", usernameValidator.getErrorMessage());
    }

    // --- TOO SHORT ---
    @Test
    void shouldRejectTooShortUsername() {
        assertFalse(usernameValidator.isValid("ab"));
        assertEquals("Username must be at least 3 characters.", usernameValidator.getErrorMessage());
    }

    @Test
    void shouldRejectTooShortPassword() {
        assertFalse(passwordValidator.isValid("abc"));
        assertEquals("Password must be at least 5 characters.", passwordValidator.getErrorMessage());
    }

    // --- TOO LONG ---
    @Test
    void shouldRejectTooLongUsername() {
        assertFalse(usernameValidator.isValid("abcdefghijklmnop"));
        assertEquals("Username must be at most 12 characters.", usernameValidator.getErrorMessage());
    }

    @Test
    void shouldRejectTooLongPassword() {
        String longPass = "a".repeat(25);
        assertFalse(passwordValidator.isValid(longPass));
        assertEquals("Password must be at most 20 characters.", passwordValidator.getErrorMessage());
    }

    // --- INVALID CHARACTERS ---
    @Test
    void shouldRejectInvalidCharactersInUsername() {
        assertFalse(usernameValidator.isValid("john_doe!"));
        assertEquals("Username contains invalid characters.", usernameValidator.getErrorMessage());
    }

    @Test
    void shouldRejectInvalidCharactersInPassword() {
        assertFalse(passwordValidator.isValid("pass word"));
        assertEquals("Password cannot contain spaces.", passwordValidator.getErrorMessage());
    }

    // --- VALID INPUTS ---
    @Test
    void shouldAcceptValidUsername() {
        assertTrue(usernameValidator.isValid("Tommy123"));
        assertNull(usernameValidator.getErrorMessage(), "No error expected for valid username");
    }

    @Test
    void shouldAcceptValidPassword() {
        assertTrue(passwordValidator.isValid("Pass@1234"));
        assertNull(passwordValidator.getErrorMessage(), "No error expected for valid password");
    }

    // --- BOUNDARY CASES ---
    @Test
    void shouldAcceptMinimumLengthUsername() {
        assertTrue(usernameValidator.isValid("abc"));
    }

    @Test
    void shouldAcceptMaximumLengthUsername() {
        assertTrue(usernameValidator.isValid("abcdefgh1234"));
    }

    @Test
    void shouldRejectUsernameOverMaxLength() {
        assertFalse(usernameValidator.isValid("abcdefgh12345"));
    }
}
