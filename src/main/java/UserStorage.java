import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class UserStorage {
    private static String FILE_PATH = "users.dat";

    public static boolean saveUsers(Set<User> users) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(users);
            return true;
        } catch (Exception e) {
            IO.println("Failed to save users: " + e.getMessage());
        }

        return false;
    }

    public static Set<User> loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashSet<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = in.readObject();
            if (obj instanceof Set) {
                return (Set<User>) obj;
            }
        } catch (Exception e) {
            IO.println("Failed to load users: " + e.getMessage());
        }

        return new HashSet<>();
    }
}
