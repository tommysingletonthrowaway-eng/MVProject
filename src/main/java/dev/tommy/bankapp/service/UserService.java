package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.exceptions.user.DuplicateUserException;
import dev.tommy.bankapp.exceptions.user.InvalidPasswordException;
import dev.tommy.bankapp.exceptions.user.InvalidUsernameException;
import dev.tommy.bankapp.exceptions.user.UserNotFoundException;

import java.util.Collection;
import java.util.Set;

public interface UserService {
    User registerUser(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException, DuplicateUserException;

    void registerUser(User user)
            throws InvalidUsernameException, InvalidPasswordException, DuplicateUserException;

    void removeUser(User user) throws UserNotFoundException;

    User getUserByUsername(String username) throws UserNotFoundException;

    void changeUserUsername(User user, String newUsername)
            throws InvalidUsernameException, DuplicateUserException;

    void changeUserPassword(User user, String newPassword) throws InvalidPasswordException;

    Collection<User> users();

    void clear();
}
