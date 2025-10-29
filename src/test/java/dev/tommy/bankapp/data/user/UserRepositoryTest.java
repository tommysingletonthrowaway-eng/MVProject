package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.DuplicateUserException;
import dev.tommy.bankapp.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;
    private UUID userId;
    private Credentials credentials;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        credentials = new Credentials("testUser", "password123");
        userId = userRepository.addUser(credentials);  // Add a user to the repository
    }

    @Test
    void testAddUser() {
        // Add a new user
        assertNotNull(userId);

        // Verify the user can be retrieved by ID
        Optional<User> user = userRepository.getUserById(userId);
        assertTrue(user.isPresent());
        assertEquals(userId, user.get().getUuid());
    }

    @Test
    void testRemoveUser() throws UserNotFoundException {
        // Remove the user
        userRepository.removeUser(userId);

        // Verify the user is removed and no longer exists
        Optional<User> user = userRepository.getUserById(userId);
        assertFalse(user.isPresent());
    }

    @Test
    void testRemoveUserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();

        // Attempt to remove a non-existent user and expect an exception
        assertThrows(UserNotFoundException.class, () -> userRepository.removeUser(nonExistentUserId));
    }

    @Test
    void testUpdateUsername() throws UserNotFoundException, DuplicateUserException {
        String newUsername = "newTestUser";

        // Update the username of the user
        userRepository.updateUsername(userId, newUsername);

        // Verify that the username has been updated
        Optional<Credentials> updatedCredentials = userRepository.getCredentials(userId);
        assertTrue(updatedCredentials.isPresent());
        assertEquals(newUsername, updatedCredentials.get().getUsername());
    }

    @Test
    void testUpdateUsernameToDuplicate() throws UserNotFoundException {
        // Add a second user with a different username
        Credentials secondCredentials = new Credentials("secondUser", "password123");
        UUID secondUserId = userRepository.addUser(secondCredentials);

        // Attempt to update the first user's username to the second user's username
        assertThrows(DuplicateUserException.class, () -> userRepository.updateUsername(userId, "secondUser"));
    }

    @Test
    void testUpdateUsernameUserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();

        // Attempt to update the username of a non-existent user
        assertThrows(UserNotFoundException.class, () -> userRepository.updateUsername(nonExistentUserId, "newUsername"));
    }

    @Test
    void testGetCredentialsById() {
        // Get credentials by userId
        Optional<Credentials> retrievedCredentials = userRepository.getCredentials(userId);

        assertTrue(retrievedCredentials.isPresent());
        assertEquals(credentials.getUsername(), retrievedCredentials.get().getUsername());
    }

    @Test
    void testFindUserByUsername() {
        // Find the user by username
        Optional<UUID> foundUserId = userRepository.findUserIdByUsername(credentials.getUsername());

        assertTrue(foundUserId.isPresent());
        assertEquals(userId, foundUserId.get());
    }

    @Test
    void testFindUserByUsernameNotFound() {
        // Try finding a user by a non-existing username
        Optional<UUID> foundUserId = userRepository.findUserIdByUsername("nonExistentUsername");

        assertFalse(foundUserId.isPresent());
    }

    @Test
    void testExistsByUsername() {
        // Check if the username exists
        assertTrue(userRepository.existsByUsername(credentials.getUsername()));

        // Check for a non-existent username
        assertFalse(userRepository.existsByUsername("nonExistentUsername"));
    }

    @Test
    void testClearAllUsers() {
        // Clear all users
        userRepository.clear();

        // Verify that the repository is empty
        assertTrue(userRepository.getAllUsers().isEmpty());
    }

    @Test
    void testGetAllUsers() {
        // Add some more users
        UUID userId2 = userRepository.addUser(new Credentials("user2", "password456"));
        UUID userId3 = userRepository.addUser(new Credentials("user3", "password789"));

        // Retrieve all users
        assertEquals(3, userRepository.getAllUsers().size());
    }

    @Test
    void testExistsByUUID() {
        // Verify that an existing userId exists
        assertTrue(userRepository.existsByUUID(userId));

        // Verify that a random UUID doesn't exist
        UUID randomUUID = UUID.randomUUID();
        assertFalse(userRepository.existsByUUID(randomUUID));
    }
}
