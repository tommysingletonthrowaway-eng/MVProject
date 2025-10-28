package dev.tommy.bankapp.data;

import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.data.user.UserRepository;
import dev.tommy.bankapp.data.user.UserStorage;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import dev.tommy.bankapp.service.BankAccountService;
import dev.tommy.bankapp.service.BankAccountServiceImpl;
import dev.tommy.bankapp.service.UserService;
import dev.tommy.bankapp.service.UserServiceImpl;
import dev.tommy.bankapp.validator.BankAccountNameValidator;
import dev.tommy.bankapp.validator.PasswordValidator;
import dev.tommy.bankapp.validator.UsernameValidator;
import dev.tommy.bankapp.validator.Validator;

import java.util.Collection;

public class AppContext {
    public static final String USERS_FILE_PATH = "users.dat";

    public final BankAccountService bankService;
    public final UserService userService;
    public final UserStorage userStorage;


    public AppContext() {
        // Serialisation
        userStorage = new UserStorage(USERS_FILE_PATH, new SimpleXOREncryption("BasicKey"));
        Collection<User> users = userStorage.loadUsers();

        // Validators
        Validator bankAccountNameValidator = new BankAccountNameValidator();
        Validator usernameValidator = new UsernameValidator();
        Validator passwordValidator = new PasswordValidator();

        // Repositories
        UserRepository userRepository = new UserRepository(users);

        // Services
        this.userService = new UserServiceImpl(userRepository, usernameValidator, passwordValidator);
        this.bankService = new BankAccountServiceImpl(bankAccountNameValidator);
    }

    public boolean saveUsers() {
        return userStorage.saveUsers(userService.users());
    }

    public void resetUsers() {
        userService.clear();
        userStorage.saveUsers(userService.users());
    }
}

