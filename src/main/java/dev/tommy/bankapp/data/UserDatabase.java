package dev.tommy.bankapp.data;

import java.util.Set;

public record UserDatabase(Set<User> users) {
    public void add(User user) {
        this.users.add(user);
    }

    public void remove(User user) {
        this.users.remove(user);
    }

    public User getUser(String username) {
        return this.users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public void clear() {
        this.users.clear();
    }

    public boolean hasUser(String username) {
        return getUser(username) != null;
    }
}
