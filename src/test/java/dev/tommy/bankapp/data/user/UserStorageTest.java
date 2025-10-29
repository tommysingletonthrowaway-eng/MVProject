package dev.tommy.bankapp.data.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.tommy.bankapp.data.user.*;
import dev.tommy.bankapp.encryption.EncryptionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserStorageTest {

    private UserStorage userStorage;
    private EncryptionStrategy encryptionStrategyMock;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        encryptionStrategyMock = mock(EncryptionStrategy.class);
        userStorage = new UserStorage("testRepo.dat", encryptionStrategyMock);
        userRepository = new UserRepository();
    }

    @Test
    void testSaveRepositorySuccess() throws Exception {
        // Given
        byte[] dummyData = "encryptedData".getBytes();
        when(encryptionStrategyMock.encrypt(any(byte[].class))).thenReturn(dummyData);

        // When
        boolean result = userStorage.saveRepository(userRepository);

        // Then
        assertTrue(result);
        verify(encryptionStrategyMock, times(1)).encrypt(any(byte[].class));
        assertTrue(Files.exists(Paths.get("testRepo.dat")));
    }

    @Test
    void testSaveRepositoryFailure() throws Exception {
        // Given
        when(encryptionStrategyMock.encrypt(any(byte[].class))).thenThrow(new RuntimeException("Encryption error"));

        // When
        boolean result = userStorage.saveRepository(userRepository);

        // Then
        assertFalse(result);
        verify(encryptionStrategyMock, times(1)).encrypt(any(byte[].class));
    }

    @Test
    void testLoadRepositorySuccess() throws Exception {
        // Given
        byte[] encryptedData = "encryptedData".getBytes();
        byte[] decryptedData = "decryptedData".getBytes();
        when(encryptionStrategyMock.decrypt(any(byte[].class))).thenReturn(decryptedData);

        // Mocking repository object to match what we are trying to load
        UserRepository repo = new UserRepository();
        // Assuming repo can be serialized and deserialized
        byte[] serializedRepo = serializeObject(repo);
        when(encryptionStrategyMock.encrypt(any(byte[].class))).thenReturn(encryptedData);

        Files.write(Paths.get("testRepo.dat"), encryptedData);

        // When
        UserRepository result = userStorage.loadRepository();

        // Then
        assertNotNull(result);
        assertEquals(UserRepository.class, result.getClass());
        verify(encryptionStrategyMock, times(1)).decrypt(any(byte[].class));
    }

    @Test
    void testLoadRepositoryFileNotExist() {
        // Given: File does not exist
        File file = new File("testRepo.dat");
        file.delete(); // Ensure the file is deleted before testing

        // When
        UserRepository result = userStorage.loadRepository();

        // Then
        assertNotNull(result);
        assertEquals(UserRepository.class, result.getClass());
    }

    @Test
    void testLoadRepositoryFailure() throws Exception {
        // Given
        byte[] encryptedData = "encryptedData".getBytes();
        when(encryptionStrategyMock.decrypt(any(byte[].class))).thenThrow(new RuntimeException("Decryption error"));

        // Write encrypted data into the file
        Files.write(Paths.get("testRepo.dat"), encryptedData);

        // When
        UserRepository result = userStorage.loadRepository();

        // Then
        assertNotNull(result);  // Default empty UserRepository should be returned on failure
        verify(encryptionStrategyMock, times(1)).decrypt(any(byte[].class));
    }

    private byte[] serializeObject(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
            out.writeObject(object);
        }
        return baos.toByteArray();
    }
}
