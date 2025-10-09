package dev.tommy.bankapp.encryption;

public class SimpleXOREncryption implements EncryptionStrategy {
    private final byte[] key;

    public SimpleXOREncryption(String keyStr) {
        this.key = keyStr.getBytes();
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return xorWithKey(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return xorWithKey(data);  // symmetric operation
    }

    private byte[] xorWithKey(byte[] data) {
        byte[] output = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = (byte)(data[i] ^ key[i % key.length]);
        }
        return output;
    }
}