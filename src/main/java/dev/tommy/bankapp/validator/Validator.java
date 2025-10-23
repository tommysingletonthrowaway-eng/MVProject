package dev.tommy.bankapp.validator;

public interface Validator {
    boolean isValid(String input);
    String getErrorMessage();
}
