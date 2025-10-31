package dev.tommy.bankapp.service;

import dev.tommy.bankapp.data.user.*;
import dev.tommy.bankapp.exceptions.user.*;
import dev.tommy.bankapp.validator.Validator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private final Validator usernameValidator;
    private final Validator passwordValidator;
    private final UserStorage userStorage;

    public UserServiceImpl(UserRepository userRepository, UserStorage userStorage, Validator usernameValidator, Validator passwordValidator) {
        this.userRepository = userRepository;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
        this.userStorage = userStorage;
    }

    // Register user
    public void registerUser(String username, String password)
            throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException {

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username already exists: " + username);
        }

        if (!validateUsername(username))
            throw new InvalidUsernameException("Invalid username: " + usernameValidator.getErrorMessage());
        if (!validatePassword(password))
            throw new InvalidPasswordException("Invalid password: " + passwordValidator.getErrorMessage());

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Credentials credentials = new Credentials(username, hashedPassword);
        userRepository.addUser(credentials);
    }

    // Login
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        Optional<UUID> userIdOpt = userRepository.findUserIdByUsername(username);
        if (userIdOpt.isEmpty()) {
            throw new InvalidUsernameException("Username not found: " + username);
        }

        UUID userId = userIdOpt.get();
        Credentials creds = userRepository.getCredentials(userId)
                .orElseThrow(() -> new InvalidUsernameException("Credentials not found"));

        if (!BCrypt.checkpw(password, creds.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return userRepository.getUserById(userId)
                .orElseThrow(() -> new InvalidUsernameException("User not found after authentication"));
    }

    // Remove user
    public void removeUser(UUID userId) throws UserNotFoundException {
        userRepository.removeUser(userId);
    }

    // Change username
    public void changeUserUsername(UUID userId, String newUsername)
            throws UserNotFoundException, DuplicateUserException, InvalidUsernameException {

        if (userRepository.existsByUsername(newUsername)) {
            throw new DuplicateUserException("Username already exists: " + newUsername);
        }

        boolean exists = userRepository.existsByUUID(userId);
        if (!exists)
            throw new UserNotFoundException("User does not exist");

        if (!validateUsername(newUsername))
            throw new InvalidUsernameException("Invalid username: " + usernameValidator.getErrorMessage());

        userRepository.updateUsername(userId, newUsername);
    }

    // Change password
    public void changeUserPassword(UUID userId, String newPassword) throws InvalidPasswordException, UserNotFoundException {
        if (!validatePassword(newPassword))
            throw new InvalidPasswordException("Invalid username: " + passwordValidator.getErrorMessage());

        Credentials creds = userRepository.getCredentials(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        creds.setPasswordHash(hashedPassword);
    }

    @Override
    public String getUsername(UUID uuid) throws UserNotFoundException {
        Credentials creds = userRepository.getCredentials(uuid)
                .orElseThrow(() -> new UserNotFoundException("No user found"));
        return creds.getUsername();
    }

    @Override
    public Collection<String> getUsernames() {
        return userRepository.getUsernames();
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    private boolean validateUsername(String username) {
        return this.usernameValidator.isValid(username);
    }

    private boolean validatePassword(String username) {
        return this.passwordValidator.isValid(username);
    }

    // Clear all
    public void clear() {
        userRepository.clear();
    }

    @Override
    public boolean save() {
        return userStorage.saveRepository(userRepository);
    }

    @Override
    public void load() {
        userRepository = userStorage.loadRepository();
    }
}
