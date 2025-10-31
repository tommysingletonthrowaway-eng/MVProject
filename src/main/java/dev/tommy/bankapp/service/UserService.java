package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.exceptions.user.*;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    void registerUser(String username, String password) throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException;
    User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException;
    void removeUser(UUID userId) throws UserNotFoundException;
    void changeUserUsername(UUID userId, String newUsername) throws UserNotFoundException, DuplicateUserException, InvalidUsernameException;
    void changeUserPassword(UUID userId, String newPassword) throws UserNotFoundException, InvalidPasswordException;
    String getUsername(UUID uuid) throws UserNotFoundException;
    Collection<String> getUsernames();
    boolean existsByUsername(String username);
    void clear();

    void load();
    boolean save();

}
