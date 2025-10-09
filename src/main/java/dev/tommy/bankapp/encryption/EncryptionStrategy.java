package dev.tommy.bankapp.encryption;

public interface EncryptionStrategy {
    byte[] encrypt(byte[] data) throws Exception;
    byte[] decrypt(byte[] data) throws Exception;
}
