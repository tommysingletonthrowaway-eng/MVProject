package dev.tommy.bankapp.exceptions.user;

public class InvalidUsernameException extends UserException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
