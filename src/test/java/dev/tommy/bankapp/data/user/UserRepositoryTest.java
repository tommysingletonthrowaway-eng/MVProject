package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.DuplicateUserException;
import dev.tommy.bankapp.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    private UserRepository repo;
    private User sampleUser;
    private Credentials sampleCreds;

    @BeforeEach
    void setUp() {
        repo = new UserRepository();
        sampleUser = new User(); // assuming User has default constructor
        sampleCreds = new Credentials("john_doe", "password123");
    }

    @Test
    @DisplayName("Should add user successfully and generate UUID")
    void testAddUser() {
        UUID userId = repo.addUser(sampleUser, sampleCreds);

        assertNotNull(userId, "UUID should not be null");
        assertTrue(repo.existsByUUID(userId), "User should exist by UUID");
        assertTrue(repo.existsByUsername("john_doe"), "Username should be mapped");
        assertEquals(sampleUser, repo.getUserById(userId).orElseThrow());
        assertEquals(sampleCreds, repo.getCredentials(userId).orElseThrow());
    }

    @Test
    @DisplayName("Should remove existing user successfully")
    void testRemoveUser_Success() throws Exception {
        UUID userId = repo.addUser(sampleUser, sampleCreds);
        repo.removeUser(userId);

        assertFalse(repo.existsByUUID(userId));
        assertFalse(repo.existsByUsername("john_doe"));
        assertTrue(repo.getUserById(userId).isEmpty());
        assertTrue(repo.getCredentials(userId).isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent user")
    void testRemoveUser_NotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(UserNotFoundException.class, () -> repo.removeUser(fakeId));
    }

    @Test
    @DisplayName("Should find user ID by username")
    void testFindUserIdByUsername() {
        UUID userId = repo.addUser(sampleUser, sampleCreds);
        Optional<UUID> found = repo.findUserIdByUsername("john_doe");

        assertTrue(found.isPresent());
        assertEquals(userId, found.get());
    }

    @Test
    @DisplayName("Should update username correctly")
    void testUpdateUsername_Success() throws Exception {
        UUID userId = repo.addUser(sampleUser, sampleCreds);
        repo.updateUsername(userId, "new_john");

        Optional<Credentials> updatedCreds = repo.getCredentials(userId);
        assertEquals("new_john", updatedCreds.orElseThrow().getUsername());
        assertTrue(repo.existsByUsername("new_john"));
        assertFalse(repo.existsByUsername("john_doe"));
    }

    @Test
    @DisplayName("Should throw DuplicateUserException on username conflict")
    void testUpdateUsername_Duplicate() throws Exception {
        UUID id1 = repo.addUser(new User(), new Credentials("john_doe", "pw1"));
        UUID id2 = repo.addUser(new User(), new Credentials("jane_doe", "pw2"));

        assertThrows(DuplicateUserException.class, () -> repo.updateUsername(id2, "john_doe"));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException on username update of missing user")
    void testUpdateUsername_UserNotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(UserNotFoundException.class, () -> repo.updateUsername(fakeId, "new_name"));
    }

    @Test
    @DisplayName("Should clear all users and mappings")
    void testClear() {
        repo.addUser(sampleUser, sampleCreds);
        repo.clear();

        assertTrue(repo.getAllUsers().isEmpty());
        assertFalse(repo.existsByUsername("john_doe"));
    }

    @Test
    @DisplayName("Should return correct user and credentials after multiple inserts")
    void testMultipleUsersConsistency() {
        UUID id1 = repo.addUser(new User(), new Credentials("user1", "pass1"));
        UUID id2 = repo.addUser(new User(), new Credentials("user2", "pass2"));

        assertNotEquals(id1, id2);
        assertEquals(2, repo.getAllUsers().size());
        assertTrue(repo.findUserIdByUsername("user1").isPresent());
        assertTrue(repo.findUserIdByUsername("user2").isPresent());
    }

    @Test
    @DisplayName("Should return false for non-existing username")
    void testExistsByUsername_False() {
        assertFalse(repo.existsByUsername("missing_user"));
    }

    @Test
    @DisplayName("Should return empty Optional for non-existing user ID")
    void testGetUserById_NotFound() {
        Optional<User> user = repo.getUserById(UUID.randomUUID());
        assertTrue(user.isEmpty());
    }
}
