package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    private UserRepository userRepository;
    private User john;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(new HashSet<>());
        john = new User("John", "password");
    }

    // ───────────────────────────────
    // Add / Remove Tests
    // ───────────────────────────────

    @Test
    @DisplayName("Should add user to repository")
    void shouldAddUserToRepository() {
        userRepository.add(john);

        assertTrue(userRepository.getAllUsers().contains(john),
                "Added user should be present in repository");
        assertTrue(userRepository.existsByUsername("John"),
                "Added user should exist by username");
    }

    @Test
    @DisplayName("Should remove existing user from repository")
    void shouldRemoveExistingUser() throws UserNotFoundException {
        userRepository.add(john);

        userRepository.remove(john);

        assertFalse(userRepository.existsByUsername("John"),
                "Removed user should no longer exist in repository");
    }

    @Test
    @DisplayName("Should throw when removing user not in repository")
    void shouldThrowWhenRemovingNonexistentUser() {
        assertThrows(UserNotFoundException.class, () -> userRepository.remove(john),
                "Expected exception when removing a user that doesn't exist");
    }

    // ───────────────────────────────
    // Query Tests
    // ───────────────────────────────

    @Test
    @DisplayName("Should confirm user exists by username")
    void shouldConfirmUserExistsByUsername() {
        userRepository.add(john);

        assertTrue(userRepository.existsByUsername("John"));
        assertFalse(userRepository.existsByUsername("Jane"));
    }

    @Test
    @DisplayName("Should return all users correctly")
    void shouldReturnAllUsers() {
        userRepository.add(john);
        userRepository.add(new User("Alice", "1234"));

        assertEquals(2, userRepository.getAllUsers().size(),
                "Repository should contain all added users");
    }

    // ───────────────────────────────
    // Clear Tests
    // ───────────────────────────────

    @Test
    @DisplayName("Should clear all users from repository")
    void shouldClearRepository() {
        userRepository.add(john);
        assertTrue(userRepository.existsByUsername("John"));

        userRepository.clear();

        assertTrue(userRepository.getAllUsers().isEmpty(),
                "Repository should be empty after clear()");
        assertFalse(userRepository.existsByUsername("John"),
                "Cleared repository should not contain any users");
    }
}
