import java.util.Set;

public class BankApp {
    public static AppContext Context;

    static void main() {
        Context = createAppContext();
        MainMenu.mainMenu();
    }

    private static AppContext createAppContext() {
        Set<User> users = UserStorage.loadUsers();
        return new AppContext(users);
    }

    public static boolean saveUsers() {
        return UserStorage.saveUsers(BankApp.Context.userDatabase.users());
    }
}
