package dev.tommy.bankapp.validator;

public class PasswordValidator extends InputValidator {

    public PasswordValidator() {
        super(
                8,
                15,
                "^[a-zA-Z0-9._@-]+$",
                "Password"
        );
    }
}

