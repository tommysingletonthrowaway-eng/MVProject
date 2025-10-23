package dev.tommy.bankapp.validator;

public class InputValidator implements Validator {

    private final int minLength;
    private final int maxLength;
    private final String allowedCharsRegex;
    private final String typeName; // "Username" or "Password" for messages

    private String errorMessage;

    public InputValidator(int minLength, int maxLength, String allowedCharsRegex, String typeName) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.allowedCharsRegex = allowedCharsRegex;
        this.typeName = typeName;
    }

    @Override
    public boolean isValid(String input) {
        if (input == null || input.isEmpty()) {
            errorMessage = typeName + " cannot be empty.";
            return false;
        }
        if (input.contains(" ")) {
            errorMessage = typeName + " cannot contain spaces.";
            return false;
        }
        if (input.length() < minLength) {
            errorMessage = typeName + " must be at least " + minLength + " characters.";
            return false;
        }
        if (input.length() > maxLength) {
            errorMessage = typeName + " must be at most " + maxLength + " characters.";
            return false;
        }
        if (!input.matches(allowedCharsRegex)) {
            errorMessage = typeName + " contains invalid characters.";
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
