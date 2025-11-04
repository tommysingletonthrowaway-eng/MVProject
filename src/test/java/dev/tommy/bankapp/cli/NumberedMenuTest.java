package dev.tommy.bankapp.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class NumberedMenuTest {

    private ByteArrayOutputStream testOut;
    private PrintStream out;

    @BeforeEach
    void setUpStreams() {
        testOut = new ByteArrayOutputStream();
        out = new PrintStream(testOut);
    }

    private Menu createMenu(String input) {
        // Setup input stream for this specific test
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        return new NumberedMenu(() -> "Test Menu", false, testIn, out);
    }

    @Test
    void menuRunsContinueAndExit() {
        // Simulate user input: "1" for CONTINUE, "2" for EXIT
        Menu menu = createMenu("1\n2\n");

        AtomicInteger counter = new AtomicInteger(0);

        menu
                .addItem("continue", "", args -> {
                    counter.incrementAndGet();
                    return MenuOperation.CONTINUE;
                })
                .addItem("exit", "", args -> {
                    counter.incrementAndGet();
                    return MenuOperation.EXIT;
                });

        menu.run();

        // Verify both menu items were selected
        assertEquals(2, counter.get());

        String output = testOut.toString();
        assertTrue(output.contains("Test Menu"));
    }

    @Test
    void menuHandlesInvalidSelection() {
        // Simulate invalid input followed by valid input
        Menu menu = createMenu("999\n2\n");

        AtomicInteger counter = new AtomicInteger(0);

        menu
                .addItem("continue", "", args -> {
                    counter.incrementAndGet();
                    return MenuOperation.CONTINUE;
                })
                .addItem("exit", "", args -> {
                    counter.incrementAndGet();
                    return MenuOperation.EXIT;
                });

        menu.run();

        // Only the valid selection increments counter
        assertEquals(1, counter.get());
    }

    @Test
    void menuSingleExit() {
        Menu menu = createMenu("1\n");

        AtomicInteger counter = new AtomicInteger(0);

        menu.addItem("exit", "", args -> {
            counter.incrementAndGet();
            return MenuOperation.EXIT;
        });

        menu.run();

        assertEquals(1, counter.get());
    }
}
