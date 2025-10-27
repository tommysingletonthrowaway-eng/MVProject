package dev.tommy.bankapp.cli;

public class ArgumentParseException extends Exception{
    public ArgumentParseException(String message) {
        super(message);
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
