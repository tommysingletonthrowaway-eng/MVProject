package dev.tommy.bankapp;

import java.util.Set;
import dev.tommy.bankapp.data.AppContext;
import dev.tommy.bankapp.data.UserStorage;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.menus.MainMenu;

// TODO:

//  Test classes:
// Test CLI work by pressing inputs and expecting things
// Test saving works
// Test banking works with correct values
// Check conversion is correct
// Check user account stuff => usernames cant repeat, transactions appear correct,
// cant have multiple bank accounts with the same name, cant login with wrong password

public class BankApp {
    public static final String USERS_FILE_PATH = "users.dat";
    public static AppContext Context;

    static void main() {
        Context = createAppContext();
        MainMenu.mainMenu();
    }

    private static AppContext createAppContext() {
        Set<User> users = UserStorage.loadUsers(USERS_FILE_PATH);
        return new AppContext(users);
    }

    public static boolean saveUsers() {
        return UserStorage.saveUsers(USERS_FILE_PATH, BankApp.Context.userDatabase.users());
    }
}
