package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.user.*;
import dev.tommy.bankapp.encryption.NoEncryption;
import dev.tommy.bankapp.exceptions.user.*;
import dev.tommy.bankapp.validator.PasswordValidator;
import dev.tommy.bankapp.validator.UsernameValidator;
import dev.tommy.bankapp.validator.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        Path tempFile = tempDir.resolve("userRepoTest.json");
        UserStorage userStorage = new UserStorage(tempFile.toString(), new NoEncryption());
        Validator usernameValidator = new UsernameValidator();
        Validator passwordValidator = new PasswordValidator();
        userRepository = new UserRepository();
        userService = new UserServiceImpl(userRepository, userStorage, usernameValidator, passwordValidator);
    }

    // --- REGISTER TESTS ---
    @Test
    void registerUser_ShouldCreateNewUser() throws Exception {
        userService.registerUser("tommy", "secure123");

        assertTrue(userRepository.existsByUsername("tommy"));
        assertEquals(1, userRepository.getUsernames().size());
    }

    @Test
    void registerUser_ShouldThrowDuplicateUserException() throws Exception {
        userService.registerUser("tommy", "secure123");
        assertThrows(DuplicateUserException.class,
                () -> userService.registerUser("tommy", "anotherpass"));
    }

    @Test
    void registerUser_ShouldThrowInvalidUsernameException() {
        assertThrows(InvalidUsernameException.class,
                () -> userService.registerUser("ab", "secure123"));
    }

    @Test
    void registerUser_ShouldThrowInvalidPasswordException() {
        assertThrows(InvalidPasswordException.class,
                () -> userService.registerUser("tommy", "12"));
    }

    // --- LOGIN TESTS ---
    @Test
    void login_ShouldReturnUser_WhenCredentialsCorrect() throws Exception {
        userService.registerUser("tommy", "secure123");
        User loggedIn = userService.login("tommy", "secure123");

        assertNotNull(loggedIn);
        assertEquals("tommy", userService.getUsername(loggedIn.getUuid()));
    }

    @Test
    void login_ShouldThrowInvalidUsernameException_WhenUserNotFound() {
        assertThrows(InvalidUsernameException.class,
                () -> userService.login("ghost", "password"));
    }

    @Test
    void login_ShouldThrowInvalidPasswordException_WhenPasswordWrong() throws Exception {
        userService.registerUser("tommy", "secure123");
        assertThrows(InvalidPasswordException.class,
                () -> userService.login("tommy", "wrongpass"));
    }

    // --- CHANGE USERNAME ---
    @Test
    void changeUserUsername_ShouldUpdateUsername() throws Exception {
        userService.registerUser("tommy", "secure123");
        UUID userId = userRepository.findUserIdByUsername("tommy").get();

        userService.changeUserUsername(userId, "newname");
        assertTrue(userRepository.existsByUsername("newname"));
    }

    @Test
    void changeUserUsername_ShouldThrowDuplicateUserException() throws Exception {
        userService.registerUser("tommy", "secure123");
        userService.registerUser("bob", "secure123");

        UUID tommyId = userRepository.findUserIdByUsername("tommy").get();
        assertThrows(DuplicateUserException.class,
                () -> userService.changeUserUsername(tommyId, "bob"));
    }

    // --- CHANGE PASSWORD ---
    @Test
    void changeUserPassword_ShouldUpdatePassword() throws Exception {
        userService.registerUser("tommy", "secure123");
        UUID userId = userRepository.findUserIdByUsername("tommy").get();

        userService.changeUserPassword(userId, "newpassword123");
        assertDoesNotThrow(() -> userService.login("tommy", "newpassword123"));
    }

    // --- REMOVE USER ---
    @Test
    void removeUser_ShouldDeleteUser() throws Exception {
        userService.registerUser("tommy", "secure123");
        UUID id = userRepository.findUserIdByUsername("tommy").get();
        userService.removeUser(id);

        assertFalse(userRepository.existsByUsername("tommy"));
    }

    // --- SAVE / LOAD ---
    @Test
    void saveAndLoad_ShouldPreserveUsers() throws Exception {
        userService.registerUser("tommy", "secure123");
        userService.save();

        userRepository.clear();
        assertFalse(userRepository.existsByUsername("tommy"));

        userService.load();
        assertTrue(userService.existsByUsername("tommy"));
    }
}
