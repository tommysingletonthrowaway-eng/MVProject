package dev.tommy.bankapp.validator;

public class UsernameValidator extends InputValidator {

    public UsernameValidator() {
        super(
                3,
                15,
                "^[a-zA-Z0-9._@-]+$",
                "Username"
        );
    }
}
