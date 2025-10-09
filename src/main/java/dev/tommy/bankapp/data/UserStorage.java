package dev.tommy.bankapp.data;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class UserStorage {
    public static boolean saveUsers(String filePath, Set<User> users) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(users);
            return true;
        } catch (Exception e) {
            IO.println("Failed to save users: " + e.getMessage());
        }

        return false;
    }

    public static Set<User> loadUsers(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashSet<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
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
