package dev.tommy.bankapp.data.user;

import java.io.Serializable;

public class Credentials implements Serializable {
    private String username;
    private String passwordHash;

    public Credentials(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
