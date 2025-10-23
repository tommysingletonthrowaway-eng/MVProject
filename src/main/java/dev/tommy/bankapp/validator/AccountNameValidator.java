package dev.tommy.bankapp.validator;

public class AccountNameValidator extends InputValidator {

    public AccountNameValidator() {
        super(
                3,
                15,
                "^[a-zA-Z0-9._@-]+$",
                "Account Name"
        );
    }
}


