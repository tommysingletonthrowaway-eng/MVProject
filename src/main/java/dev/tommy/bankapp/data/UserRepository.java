package dev.tommy.bankapp.data;

import dev.tommy.bankapp.exceptions.user.UserNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepository {
    private final Set<User> users;

    public UserRepository() {
        this.users = new HashSet<>();
    }

    public UserRepository(Set<User> users) {
        this.users = users;
    }

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) throws UserNotFoundException {
        if (!users.remove(user)) {
            throw new UserNotFoundException(user.getUsername());
        }
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public void clear() {
        users.clear();
    }

    public Set<User> getAllUsers() {
        return Set.copyOf(users);
    }
}
