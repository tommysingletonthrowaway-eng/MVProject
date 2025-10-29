package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.DuplicateUserException;
import dev.tommy.bankapp.exceptions.user.UserNotFoundException;

import java.io.Serializable;
import java.util.*;

public class UserRepository implements Serializable {

    private final Map<UUID, User> usersById = new HashMap<>();
    private final Map<UUID, Credentials> credentialsById = new HashMap<>();
    private final Map<String, UUID> usernameToId = new HashMap<>();

    // Add user
    public UUID addUser(Credentials credentials) {
        UUID id = UUID.randomUUID();
        usersById.put(id, new User(id));
        credentialsById.put(id, credentials);
        usernameToId.put(credentials.getUsername(), id);
        return id;
    }

    // Remove user
    public void removeUser(UUID userId) throws UserNotFoundException {
        User removedUser = usersById.remove(userId);
        Credentials removedCreds = credentialsById.remove(userId);
        if (removedUser == null || removedCreds == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        usernameToId.remove(removedCreds.getUsername());
    }

    // Get user by UUID
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    public void updateUsername(UUID userId, String newUsername) throws UserNotFoundException, DuplicateUserException {
        if (usernameToId.containsKey(newUsername)) {
            throw new DuplicateUserException("Username already exists: " + newUsername);
        }

        Credentials creds = credentialsById.get(userId);
        if (creds == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }

        String oldUsername = creds.getUsername();
        creds.setUsername(newUsername);

        usernameToId.remove(oldUsername);
        usernameToId.put(newUsername, userId);
    }


    // Get credentials by UUID
    public Optional<Credentials> getCredentials(UUID userId) {
        return Optional.ofNullable(credentialsById.get(userId));
    }

    // Find userId by username
    public Optional<UUID> findUserIdByUsername(String username) {
        return Optional.ofNullable(usernameToId.get(username));
    }

    // Check if username exists
    public boolean existsByUsername(String username) {
        return usernameToId.containsKey(username);
    }

    // Clear all
    public void clear() {
        usersById.clear();
        credentialsById.clear();
        usernameToId.clear();
    }

    // Get all users
    public Collection<User> getAllUsers() {
        return List.copyOf(usersById.values());
    }

    public boolean existsByUUID(UUID userId) {
        return usersById.containsKey(userId);
    }

    public Collection<String> getUsernames() {
        return usernameToId.keySet();
    }
}
