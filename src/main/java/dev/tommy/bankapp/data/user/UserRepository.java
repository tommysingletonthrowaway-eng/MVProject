package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public UserRepository(Collection<User> users) {
        this.users = users.stream()
                .collect(Collectors.toMap(
                        User::getUsername, // key
                        user -> user       // value
                ));
    }

    public void add(User user) {
        users.put(user.getUsername(), user);
    }

    public void remove(User user) throws UserNotFoundException {
        if (users.remove(user.getUsername()) == null) {
            throw new UserNotFoundException(user.getUsername());
        }
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public void clear() {
        users.clear();
    }

    public Collection<User> getAllUsers() {
        return List.copyOf(users.values());
    }
}
