import java.util.Set;

public class AppContext {
    public final UserDatabase userDatabase;

    public AppContext(Set<User> users) {
        this.userDatabase = new UserDatabase(users);
    }
}

