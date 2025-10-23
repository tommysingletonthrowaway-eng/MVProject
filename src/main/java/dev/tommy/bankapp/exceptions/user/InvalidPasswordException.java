package dev.tommy.bankapp.exceptions.user;

public class InvalidPasswordException extends UserException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}


