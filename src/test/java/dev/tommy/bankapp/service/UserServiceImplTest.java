package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.data.user.UserRepository;
import dev.tommy.bankapp.data.user.UserStorage;
import dev.tommy.bankapp.exceptions.user.*;
import dev.tommy.bankapp.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    private UserRepository userRepository;
    private Validator usernameValidator;
    private Validator passwordValidator;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        UserStorage userStorage = mock(UserStorage.class);
        usernameValidator = mock(Validator.class);
        passwordValidator = mock(Validator.class);
        userService = new UserServiceImpl(userRepository, userStorage, usernameValidator, passwordValidator);
    }

    @Test
    void registerUserShouldSucceed() throws Exception {
        String username = "John";
        String password = "pass123";

        when(usernameValidator.isValid(username)).thenReturn(true);
        when(passwordValidator.isValid(password)).thenReturn(true);

        userService.registerUser(username, password);
    }

    @Test
    void registerUserInvalidUsernameShouldThrow() {
        String username = "invalid!";
        String password = "pass123";

        when(usernameValidator.isValid(username)).thenReturn(false);
        when(usernameValidator.getErrorMessage()).thenReturn("Invalid username");

        InvalidUsernameException exception = assertThrows(
                InvalidUsernameException.class,
                () -> userService.registerUser(username, password)
        );

        assertEquals("Invalid username", exception.getMessage());
    }

    @Test
    void registerUserInvalidPasswordShouldThrow() {
        String username = "John";
        String password = "short";

        when(usernameValidator.isValid(username)).thenReturn(true);
        when(passwordValidator.isValid(password)).thenReturn(false);
        when(passwordValidator.getErrorMessage()).thenReturn("Invalid password");

        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> userService.registerUser(username, password)
        );

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void registerDuplicateUserShouldThrow() {
        String username = "John";
        String password = "pass123";

        when(usernameValidator.isValid(username)).thenReturn(true);
        when(passwordValidator.isValid(password)).thenReturn(true);
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.registerUser(username, password));
    }

    @Test
    void removeUserShouldCallRepository() throws UserNotFoundException {
        User user = new User("John", "pass123");
        userService.removeUser(user);
        verify(userRepository).remove(user);
    }

    @Test
    void getUserByUsernameShouldReturnUser() throws UserNotFoundException {
        User user = new User("John", "pass123");
        when(userRepository.findByUsername("John")).thenReturn(Optional.of(user));

        User found = userService.getUserByUsername("John");
        assertEquals(user, found);
    }

    @Test
    void getUserByUsernameNotFoundShouldThrow() {
        when(userRepository.findByUsername("John")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("John"));
    }

    @Test
    void changeUserUsernameShouldSucceed() throws Exception {
        User user = new User("John", "pass123");
        String newUsername = "Alice";

        when(usernameValidator.isValid(newUsername)).thenReturn(true);
        when(userRepository.existsByUsername(newUsername)).thenReturn(false);

        userService.changeUserUsername(user, newUsername);
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    void changeUserUsernameInvalidShouldThrow() {
        User user = new User("John", "pass123");
        String newUsername = "invalid!";

        when(usernameValidator.isValid(newUsername)).thenReturn(false);
        when(usernameValidator.getErrorMessage()).thenReturn("Invalid username");

        assertThrows(InvalidUsernameException.class, () -> userService.changeUserUsername(user, newUsername));
    }

    @Test
    void changeUserUsernameDuplicateShouldThrow() {
        User user = new User("John", "pass123");
        String newUsername = "Alice";

        when(usernameValidator.isValid(newUsername)).thenReturn(true);
        when(userRepository.existsByUsername(newUsername)).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.changeUserUsername(user, newUsername));
    }

    @Test
    void changeUserPasswordShouldSucceed() throws InvalidPasswordException {
        User user = new User("John", "oldPass");
        String newPassword = "newPass123";

        when(passwordValidator.isValid(newPassword)).thenReturn(true);

        userService.changeUserPassword(user, newPassword);
        assertTrue(user.checkPassword(newPassword));
    }

    @Test
    void changeUserPasswordInvalidShouldThrow() {
        User user = new User("John", "oldPass");
        String newPassword = "123";

        when(passwordValidator.isValid(newPassword)).thenReturn(false);
        when(passwordValidator.getErrorMessage()).thenReturn("Invalid password");

        assertThrows(InvalidPasswordException.class, () -> userService.changeUserPassword(user, newPassword));
    }

    @Test
    void usersShouldReturnAllUsers() {
        when(userRepository.getAllUsers()).thenReturn(Collections.emptyList());
        assertNotNull(userService.users());
    }

    @Test
    void clearShouldCallRepositoryClear() {
        userService.clear();
        verify(userRepository).clear();
    }
}
