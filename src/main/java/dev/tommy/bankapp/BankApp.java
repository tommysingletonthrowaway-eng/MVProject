package dev.tommy.bankapp;

import java.util.Set;
import dev.tommy.bankapp.data.AppContext;
import dev.tommy.bankapp.data.UserStorage;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.encryption.SimpleXOREncryption;
import dev.tommy.bankapp.menus.MainMenu;

public class BankApp {
    public static final String USERS_FILE_PATH = "users.dat";
    public static final AppContext context;
    public static final UserStorage userStorage;

    static {
        userStorage = new UserStorage(new SimpleXOREncryption("BasicKey"));
        context = createAppContext();
    }

    static void main() {
        MainMenu.mainMenu();
    }

    private static AppContext createAppContext() {
        Set<User> users = userStorage.loadUsers(USERS_FILE_PATH);
        return new AppContext(users);
    }

    public static boolean saveUsers() {
        return userStorage.saveUsers(USERS_FILE_PATH, context.userDatabase.users());
    }

    public static void resetUsers() {
        context.userDatabase.clear();
        userStorage.saveUsers(USERS_FILE_PATH, context.userDatabase.users());
    }
}
