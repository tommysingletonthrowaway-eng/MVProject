package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.user.IUserRepository;
import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.exceptions.user.DuplicateUserException;
import dev.tommy.bankapp.exceptions.user.InvalidPasswordException;
import dev.tommy.bankapp.exceptions.user.InvalidUsernameException;
import dev.tommy.bankapp.exceptions.user.UserNotFoundException;
import dev.tommy.bankapp.validator.Validator;

import java.util.Collection;

public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;
    private final Validator usernameValidator;
    private final Validator passwordValidator;

    public UserServiceImpl(IUserRepository userRepository, Validator usernameValidator, Validator passwordValidator) {
        this.userRepository = userRepository;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public User registerUser(String username, String password) throws InvalidUsernameException, InvalidPasswordException, DuplicateUserException {
        if (!usernameValidator.isValid(username)) {
            throw new InvalidUsernameException(usernameValidator.getErrorMessage());
        }

        if (!passwordValidator.isValid(password)) {
            throw new InvalidPasswordException(passwordValidator.getErrorMessage());
        }

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException(username);
        }

        User newUser = new User(username, password);
        userRepository.add(newUser);
        return newUser;
    }

    @Override
    public void registerUser(User user)
            throws InvalidUsernameException, InvalidPasswordException, DuplicateUserException {
        if (!usernameValidator.isValid(user.getUsername())) {
            throw new InvalidUsernameException(usernameValidator.getErrorMessage());
        }

        if (!user.validatePassword(passwordValidator)) {
            throw new InvalidPasswordException(passwordValidator.getErrorMessage());
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException(user.getUsername());
        }

        userRepository.add(user);
    }

    @Override
    public void removeUser(User user) throws UserNotFoundException {
        userRepository.remove(user);
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void changeUserUsername(User user, String newUsername) throws InvalidUsernameException, DuplicateUserException {
        if (!usernameValidator.isValid(newUsername)) {
            throw new InvalidUsernameException(usernameValidator.getErrorMessage());
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new DuplicateUserException(newUsername);
        }
        user.changeUsername(newUsername);
    }

    @Override
    public void changeUserPassword(User user, String newPassword) throws InvalidPasswordException {
        if (!passwordValidator.isValid(newPassword)) {
            throw new InvalidPasswordException(passwordValidator.getErrorMessage());
        }
        user.changePassword(newPassword);
    }

    @Override
    public Collection<User> users() {
        return userRepository.getAllUsers();
    }

    @Override
    public void clear() {
        userRepository.clear();
    }
}
