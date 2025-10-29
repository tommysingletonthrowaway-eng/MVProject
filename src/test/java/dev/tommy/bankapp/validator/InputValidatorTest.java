package dev.tommy.bankapp.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator usernameValidator;
    private InputValidator passwordValidator;

    @BeforeEach
    void setUp() {
        usernameValidator = new InputValidator(3, 10, "^[a-zA-Z0-9]*$", "Username");
        passwordValidator = new InputValidator(6, 12, "^[a-zA-Z0-9@#$%^&+=]*$", "Password");
    }

    @Test
    void nullOrEmptyInputShouldBeInvalid() {
        assertFalse(usernameValidator.isValid(null));
        assertEquals("Username cannot be empty.", usernameValidator.getErrorMessage());

        assertFalse(passwordValidator.isValid(""));
        assertEquals("Password cannot be empty.", passwordValidator.getErrorMessage());
    }

    @Test
    void inputWithSpacesShouldBeInvalid() {
        assertFalse(usernameValidator.isValid("John Doe"));
        assertEquals("Username cannot contain spaces.", usernameValidator.getErrorMessage());

        assertFalse(passwordValidator.isValid("pass word"));
        assertEquals("Password cannot contain spaces.", passwordValidator.getErrorMessage());
    }

    @Test
    void inputTooShortShouldBeInvalid() {
        assertFalse(usernameValidator.isValid("Jo"));
        assertEquals("Username must be at least 3 characters.", usernameValidator.getErrorMessage());

        assertFalse(passwordValidator.isValid("12345"));
        assertEquals("Password must be at least 6 characters.", passwordValidator.getErrorMessage());
    }

    @Test
    void inputTooLongShouldBeInvalid() {
        assertFalse(usernameValidator.isValid("ThisUsernameIsTooLong"));
        assertEquals("Username must be at most 10 characters.", usernameValidator.getErrorMessage());

        assertFalse(passwordValidator.isValid("ThisPasswordIsWayTooLong"));
        assertEquals("Password must be at most 12 characters.", passwordValidator.getErrorMessage());
    }

    @Test
    void inputWithInvalidCharactersShouldBeInvalid() {
        assertFalse(usernameValidator.isValid("John!"));
        assertEquals("Username contains invalid characters.", usernameValidator.getErrorMessage());

        assertFalse(passwordValidator.isValid("pass*word"));
        assertEquals("Password contains invalid characters.", passwordValidator.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Alice123", "Bob7"})
    void validUsernamesShouldPass(String input) {
        assertTrue(usernameValidator.isValid(input));
        assertNull(usernameValidator.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"password1", "Pass@123", "My$ecret"})
    void validPasswordsShouldPass(String input) {
        assertTrue(passwordValidator.isValid(input));
        assertNull(passwordValidator.getErrorMessage());
    }
}
