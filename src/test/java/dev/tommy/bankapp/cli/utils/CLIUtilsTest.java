package dev.tommy.bankapp.cli.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CLIUtilsTest {

    @BeforeEach
    void setup() {
        // Reset scanner before each test
        CLIUtils.scanner = new java.util.Scanner(System.in);
    }

    @Test
    void testGetSplitArgs() {
        String line = "command -a 42 \"hello world\"";
        String[] result = CLIUtils.getSplitArgs(line);
        assertArrayEquals(new String[]{"command", "-a", "42", "hello world"}, result);

        assertArrayEquals(new String[0], CLIUtils.getSplitArgs(""));
        assertArrayEquals(new String[]{"single"}, CLIUtils.getSplitArgs("single"));
        assertArrayEquals(new String[]{"quoted string"}, CLIUtils.getSplitArgs("\"quoted string\""));
        assertArrayEquals(new String[]{"a", "b c", "d"}, CLIUtils.getSplitArgs("a \"b c\" d"));
    }

    @Test
    void testGetTitle() {
        assertEquals("===== My Menu =====", CLIUtils.getTitle("My Menu"));
    }

    @Test
    void testPromptInputString() {
        String simulatedInput = "hello\n";
        CLIUtils.scanner = new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        Optional<String> result = CLIUtils.promptInput("Prompt: ");
        assertTrue(result.isPresent());
        assertEquals("hello", result.get());
    }

    @Test
    void testPromptInputStringExit() {
        String simulatedInput = "exit\n";
        CLIUtils.scanner = new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        Optional<String> result = CLIUtils.promptInput("Prompt: ");
        assertTrue(result.isEmpty());
    }

    @Test
    void testPromptInputWithParser() {
        String simulatedInput = "42\n";
        CLIUtils.scanner = new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        Optional<Integer> result = CLIUtils.promptInput("Enter number: ", Integer::parseInt);
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    void testPromptInputWithParserInvalid() {
        String simulatedInput = "notanumber\n";
        CLIUtils.scanner = new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        Optional<Integer> result = CLIUtils.promptInput("Enter number: ", Integer::parseInt);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPromptInputWithParserExit() {
        String simulatedInput = "exit\n";
        CLIUtils.scanner = new java.util.Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        Optional<Integer> result = CLIUtils.promptInput("Enter number: ", Integer::parseInt);
        assertTrue(result.isEmpty());
    }
}
