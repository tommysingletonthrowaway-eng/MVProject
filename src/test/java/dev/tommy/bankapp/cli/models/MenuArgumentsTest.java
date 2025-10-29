package dev.tommy.bankapp.cli.models;

import dev.tommy.bankapp.cli.ArgumentParseException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MenuArgumentsTest {

    private MenuArguments buildMenuArguments(String... args) {
        return new MenuArguments(args);
    }

    @Test
    void testEmptyArguments() {
        MenuArguments args = buildMenuArguments();
        assertTrue(args.isEmpty());
        assertEquals(0, args.size());
        assertFalse(args.hasNext());
        assertEquals(Optional.empty(), args.peek());
    }

    @Test
    void testCursorAndSkip() {
        MenuArguments args = buildMenuArguments("first", "second");
        assertTrue(args.hasNext());
        assertEquals(Optional.of("first"), args.peek());

        args.skip();
        assertEquals(Optional.of("second"), args.peek());

        args.skip();
        assertFalse(args.hasNext());
        assertEquals(Optional.empty(), args.peek());
    }

    @Test
    void testTryGetInt() {
        MenuArguments args = buildMenuArguments("42", "abc");
        Optional<Integer> val = args.tryGetInt();
        assertTrue(val.isPresent());
        assertEquals(42, val.get());

        Optional<Integer> invalid = args.tryGetInt();
        assertTrue(invalid.isEmpty());
    }

    @Test
    void testTryGetDouble() {
        MenuArguments args = buildMenuArguments("3.14", "not-a-number");
        Optional<Double> val = args.tryGetDouble();
        assertTrue(val.isPresent());
        assertEquals(3.14, val.get());

        Optional<Double> invalid = args.tryGetDouble();
        assertTrue(invalid.isEmpty());
    }

    @Test
    void testTryGetBoolean() {
        MenuArguments args = buildMenuArguments("true", "false", "notabool");
        assertEquals(Optional.of(true), args.tryGetBoolean());
        assertEquals(Optional.of(false), args.tryGetBoolean());
        assertEquals(Optional.of(false), args.tryGetBoolean()); // parseBoolean returns false for invalid string
    }

    @Test
    void testTryGetString() {
        MenuArguments args = buildMenuArguments("hello", "world");
        assertEquals(Optional.of("hello"), args.tryGetString());
        assertEquals(Optional.of("world"), args.tryGetString());
        assertEquals(Optional.empty(), args.tryGetString());
    }

    @Test
    void testTryGetOptionalDateTimeArgumentWithTime() throws ArgumentParseException {
        MenuArguments args = buildMenuArguments("-s", "2025-10-27", "15:30:00");
        Optional<LocalDateTime> dt = args.tryGetOptionalDateTimeArgument("-s", true);
        assertTrue(dt.isPresent());
        assertEquals(LocalDateTime.of(2025, 10, 27, 15, 30, 0), dt.get());
    }

    @Test
    void testTryGetOptionalDateTimeArgumentDateOnly() throws ArgumentParseException {
        MenuArguments args = buildMenuArguments("-s", "2025-10-27");
        Optional<LocalDateTime> dtStart = args.tryGetOptionalDateTimeArgument("-s", true);
        assertTrue(dtStart.isPresent());
        assertEquals(LocalDateTime.of(2025, 10, 27, 0, 0, 0), dtStart.get());

        Optional<LocalDateTime> dtEnd = args.tryGetOptionalDateTimeArgument("-s", false);
        assertTrue(dtEnd.isPresent());
        assertEquals(LocalDateTime.of(2025, 10, 27, 23, 59, 59), dtEnd.get());
    }

    @Test
    void testTryGetOptionalDateTimeArgumentInvalidFormat() {
        MenuArguments args = buildMenuArguments("-s", "invalid-date");
        assertThrows(ArgumentParseException.class, () ->
                args.tryGetOptionalDateTimeArgument("-s", true)
        );
    }

    @Test
    void testTryGetOptionalArgumentGeneric() throws ArgumentParseException {
        MenuArguments args = buildMenuArguments("-n", "42", "-f", "3.14");
        Optional<Integer> n = args.tryGetOptionalArgument("-n", Integer::parseInt);
        assertTrue(n.isPresent());
        assertEquals(42, n.get());

        Optional<Double> f = args.tryGetOptionalArgument("-f", Double::parseDouble);
        assertTrue(f.isPresent());
        assertEquals(3.14, f.get());
    }

    @Test
    void testTryGetOptionalArgumentMissingTag() throws ArgumentParseException {
        MenuArguments args = buildMenuArguments("-x", "value");
        Optional<Integer> n = args.tryGetOptionalArgument("-n", Integer::parseInt);
        assertTrue(n.isEmpty());
    }

    @Test
    void testResetCursor() {
        MenuArguments args = buildMenuArguments("a", "b");
        assertEquals(Optional.of("a"), args.peek());
        args.skip();
        assertEquals(Optional.of("b"), args.peek());
        args.resetCursor();
        assertEquals(Optional.of("a"), args.peek());
    }
}
