package dev.tommy.bankapp.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class WordMenuTest {

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setupStreams() {
        outContent = new ByteArrayOutputStream();
    }

    private WordMenu buildMenuWithInput(String input, boolean autoExit, AtomicInteger counter) {
        InputStream testIn = new ByteArrayInputStream(input.getBytes());
        WordMenu menu = new WordMenu(() -> "Test Menu", autoExit, testIn, new PrintStream(outContent));

        menu.addItem("run", "Increment counter", args -> {
            counter.incrementAndGet();
            return MenuOperation.CONTINUE;
        });

        menu.addItem("exit", "Exit menu", args -> MenuOperation.EXIT);

        return menu;
    }

    @Test
    void testValidCommand() {
        AtomicInteger counter = new AtomicInteger(0);
        WordMenu menu = buildMenuWithInput("run\nexit\n", false, counter);

        // Run menu until exit
        MenuOperation op;
        do {
            menu.display();
            op = menu.handleInput();
        } while (op != MenuOperation.EXIT);

        assertEquals(1, counter.get());
        String output = outContent.toString();
        assertTrue(output.contains("Test Menu"));
        assertTrue(output.contains("> "));
    }

    @Test
    void testHelpCommand() {
        AtomicInteger counter = new AtomicInteger(0);
        WordMenu menu = buildMenuWithInput("help\nexit\n", false, counter);

        // Run first input (help)
        menu.display();
        MenuOperation op1 = menu.handleInput();
        assertEquals(MenuOperation.CONTINUE, op1);

        String output = outContent.toString();
        assertTrue(output.contains("Available commands"));
        assertTrue(output.contains("run"));
        assertTrue(output.contains("exit"));

        // Run exit command
        menu.display();
        MenuOperation op2 = menu.handleInput();
        assertEquals(MenuOperation.EXIT, op2);
    }

    @Test
    void testInvalidCommandAutoExitFalse() {
        AtomicInteger counter = new AtomicInteger(0);
        WordMenu menu = buildMenuWithInput("invalid\nexit\n", false, counter);

        // First input: invalid
        menu.display();
        MenuOperation op1 = menu.handleInput();
        assertEquals(MenuOperation.CONTINUE, op1);
        assertTrue(outContent.toString().contains("Invalid command"));

        // Second input: exit
        menu.display();
        MenuOperation op2 = menu.handleInput();
        assertEquals(MenuOperation.EXIT, op2);
    }

    @Test
    void testInvalidCommandAutoExitTrue() {
        AtomicInteger counter = new AtomicInteger(0);
        WordMenu menu = buildMenuWithInput("invalid\n", true, counter);

        menu.display();
        MenuOperation op = menu.handleInput();
        assertEquals(MenuOperation.EXIT, op);
        assertTrue(outContent.toString().contains("Invalid command"));
    }
}
