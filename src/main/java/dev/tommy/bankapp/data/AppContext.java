package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.user.UserRepository;
import dev.tommy.bankapp.data.user.UserStorage;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import dev.tommy.bankapp.service.UserService;
import dev.tommy.bankapp.service.UserServiceImpl;
import dev.tommy.bankapp.validator.BankAccountNameValidator;
import dev.tommy.bankapp.validator.PasswordValidator;
import dev.tommy.bankapp.validator.UsernameValidator;
import dev.tommy.bankapp.validator.Validator;

public class AppContext {
    public static final String USERS_FILE_PATH = "users.dat";

    public final UserService userService;


    public AppContext() {
        // Validators
        Validator bankAccountNameValidator = new BankAccountNameValidator();
        Validator usernameValidator = new UsernameValidator();
        Validator passwordValidator = new PasswordValidator();

        // Repositories
        UserRepository userRepository = new UserRepository();

        // Services
        UserStorage userStorage = new UserStorage(USERS_FILE_PATH, new SimpleXOREncryption("BasicKey"));
        this.userService = new UserServiceImpl(userRepository, userStorage, usernameValidator, passwordValidator);
        this.userService.load();
    }

    public boolean saveUsers() {
        return userService.save();
    }

    public void resetUsers() {
        userService.clear();
        userService.save();
    }
}

