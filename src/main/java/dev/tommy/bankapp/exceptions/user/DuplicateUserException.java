package dev.tommy.bankapp.exceptions.user;

public class DuplicateUserException extends UserException {
    public DuplicateUserException(String userId) {
        super("User with ID '" + userId + "' already exists.");
    }
}

