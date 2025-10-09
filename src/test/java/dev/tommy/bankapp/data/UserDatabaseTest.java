package dev.tommy.bankapp.data;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseTest {
    @Test
    void add() {
        UserDatabase uD = new UserDatabase(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);
        assertTrue(uD.users().contains(john));
    }

    @Test
    void remove() {
        UserDatabase uD = new UserDatabase(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);
        uD.remove(john);
        assertFalse(uD.users().contains(john));
    }

    @Test
    void getUser() {
        UserDatabase uD = new UserDatabase(new HashSet<>());
        User john = new User("John", "password");
        uD.add(john);

        assertEquals(john, uD.getUser("John"));
    }

    @Test
    void hasUser() {
        UserDatabase uD = new UserDatabase(new HashSet<>());
        uD.add(new User("John", "password"));
        assertTrue(uD.hasUser("John"));
    }

    @Test
    void clear() {
        UserDatabase uD = new UserDatabase(new HashSet<>());
        uD.add(new User("John", "password"));
        assertTrue(uD.hasUser("John"));

        uD.clear();
        assertFalse(uD.hasUser("John"));
        assertTrue(uD.users().isEmpty());
    }
}