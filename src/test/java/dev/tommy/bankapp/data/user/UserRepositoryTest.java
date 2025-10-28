package dev.tommy.bankapp.data.user;

import dev.tommy.bankapp.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    @Test
    void add() {
        UserRepository uD = new UserRepository(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);
        assertTrue(uD.getAllUsers().contains(john));
    }

    @Test
    void remove() {
        UserRepository uD = new UserRepository(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);
        try {
            uD.remove(john);
        } catch (UserNotFoundException e) {
            fail();
        }
        assertFalse(uD.existsByUsername("John"));
    }

    @Test
    void getUser() {
        UserRepository uD = new UserRepository(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);

        assertTrue(uD.existsByUsername("John"));
    }

    @Test
    void hasUser() {
        UserRepository uD = new UserRepository(new HashSet<>());
        uD.add(new User("John", "password"));
        assertTrue(uD.existsByUsername("John"));
    }

    @Test
    void clear() {
        UserRepository uD = new UserRepository(new HashSet<>());
        uD.add(new User("John", "password"));
        assertTrue(uD.existsByUsername("John"));

        uD.clear();
        assertFalse(uD.existsByUsername("John"));
        assertTrue(uD.getAllUsers().isEmpty());
    }
}