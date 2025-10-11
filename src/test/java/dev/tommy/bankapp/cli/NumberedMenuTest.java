package dev.tommy.bankapp.cli;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class NumberedMenuTest {
    @Test
    void numberedMenuTest() {
        // Setup dynamic input stream
        final PipedInputStream testIn = new PipedInputStream();
        PipedOutputStream testInputWriter;
        try {
            testInputWriter = new PipedOutputStream(testIn);
        } catch (Exception _) {
            fail();
            return;
        }

        // Setup output capturing
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(testOut);

        Menu menu = new NumberedMenu("Test Menu", false, testIn, out);

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

        // Simulate user input: select "Continue" (1), then "Exit" (2)
        new Thread(() -> {
            try {
                Thread.sleep(100); // Wait for menu to be ready
                testInputWriter.write("1\n".getBytes()); // Select CONTINUE
                testInputWriter.flush();

                Thread.sleep(100);
                testInputWriter.write("2\n".getBytes()); // Select EXIT
                testInputWriter.flush();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        menu.run();

        assertEquals(2, counter.get());
        String output = testOut.toString();
        assertTrue(output.contains("Test Menu"));
    }
}