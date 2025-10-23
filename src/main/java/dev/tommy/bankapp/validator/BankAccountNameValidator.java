package dev.tommy.bankapp.validator;

public class BankAccountNameValidator extends InputValidator {

    public BankAccountNameValidator() {
        super(
                3,
                15,
                "^[a-zA-Z0-9._@-]+$",
                "Bank Account Name"
        );
    }
}
