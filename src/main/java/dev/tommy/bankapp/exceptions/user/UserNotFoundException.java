package dev.tommy.bankapp.exceptions.user;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(message);
    }
}