import java.util.Set;

public record UserDatabase(Set<User> users) {
    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }

    public User getUser(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public boolean hasUser(String username) {
        return getUser(username) != null;
    }
}
