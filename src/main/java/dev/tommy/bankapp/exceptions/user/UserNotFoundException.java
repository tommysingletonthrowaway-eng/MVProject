package dev.tommy.bankapp.exceptions.user;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String userId) {
        super("User with ID '" + userId + "' was not found.");
    }
}