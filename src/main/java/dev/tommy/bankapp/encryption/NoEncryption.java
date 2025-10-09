package dev.tommy.bankapp.encryption;

public class NoEncryption implements EncryptionStrategy {
    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return data;
    }
}
