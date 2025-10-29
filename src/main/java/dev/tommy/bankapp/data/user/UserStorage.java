package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.encryption.EncryptionStrategy;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserStorage {
    private final String filePath;
    private final EncryptionStrategy encryptionStrategy;

    public UserStorage(String filePath, EncryptionStrategy encryptionStrategy) {
        this.filePath = filePath;
        this.encryptionStrategy = encryptionStrategy;
    }

    public boolean saveRepository(UserRepository repository) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(repository);
            }

            byte[] encrypted = encryptionStrategy.encrypt(baos.toByteArray());
            Files.write(Paths.get(filePath), encrypted);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to save repository: " + e.getMessage());
            return false;
        }
    }

    public UserRepository loadRepository() {
        File file = new File(filePath);
        if (!file.exists()) return new UserRepository();

        try {
            byte[] encrypted = Files.readAllBytes(Paths.get(filePath));
            byte[] decrypted = encryptionStrategy.decrypt(encrypted);

            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decrypted))) {
                Object obj = in.readObject();
                if (obj instanceof UserRepository repo) {
                    return repo;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load repository: " + e.getMessage());
        }

        return new UserRepository();
    }
}
