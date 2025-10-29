package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.UserNotFoundException;

import java.util.Collection;
import java.util.Optional;

public interface IUserRepository {

    void add(User user);

    void remove(User user) throws UserNotFoundException;

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void clear();

    Collection<User> getAllUsers();
}
