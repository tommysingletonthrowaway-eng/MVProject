package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.encryption.EncryptionStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

public class UserStorage {
    private final String filePath;
    private final EncryptionStrategy encryptionStrategy;

    public UserStorage(String filePath, EncryptionStrategy encryptionStrategy) {
        this.encryptionStrategy = encryptionStrategy;
        this.filePath = filePath;
    }

    public boolean saveUsers(Collection<User> users) {
        try {
            // Serialize users to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(users);
            }

            // Encrypt serialized bytes
            byte[] encryptedData = encryptionStrategy.encrypt(baos.toByteArray());

            // Write encrypted data to file
            Files.write(Paths.get(filePath), encryptedData);

            return true;
        } catch (Exception e) {
            IO.println("Failed to save users: " + e.getMessage());
        }

        return false;
    }

    public Collection<User> loadUsers() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashSet<>();
        }

        try {
            // Read encrypted bytes from file
            byte[] encryptedData = Files.readAllBytes(Paths.get(filePath));

            // Decrypt bytes
            byte[] decryptedData = encryptionStrategy.decrypt(encryptedData);

            // Deserialize bytes back to Set<User>
            ByteArrayInputStream bais = new ByteArrayInputStream(decryptedData);
            try (ObjectInputStream in = new ObjectInputStream(bais)) {
                Object obj = in.readObject();
                if (obj instanceof Collection) {
                    return (Collection<User>) obj;
                }
            }
        } catch (Exception e) {
            IO.println("Failed to load users: " + e.getMessage());
        }

        return new HashSet<>();
    }
}
