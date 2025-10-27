package dev.tommy.bankapp.cli;

import dev.tommy.bankapp.cli.models.MenuArguments;
import dev.tommy.bankapp.cli.utils.CLIUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WordMenuTest {
    @Test
    void wordMenuTest() {
        // Simulate CLI args after combining quotes
        String input =
                "-s 2025-10-27 10:00:00 -e 2025-10-27 15:00:00 -n 42";

        var args = CLIUtils.getSplitArgs(input);
        assertEquals("-s", args[0]);
        assertEquals("2025-10-27", args[1]);

        MenuArguments menuArgs = new MenuArguments(args);

        try {
            Optional<LocalDateTime> startOpt = menuArgs.tryGetOptionalDateTimeArgument("-s", true);
            assertTrue(startOpt.isPresent());
            assertEquals(LocalDateTime.of(2025, 10, 27, 10, 0, 0), startOpt.get());
        } catch (ArgumentParseException e) {
            fail();
        }

        try {
            Optional<LocalDateTime> endOpt = menuArgs.tryGetOptionalDateTimeArgument("-e", false);
            assertTrue(endOpt.isPresent());
            assertEquals(LocalDateTime.of(2025, 10, 27, 15, 0, 0), endOpt.get());
        } catch (ArgumentParseException e) {
            fail();
        }

        // Parse an integer argument
        Optional<Integer> numberOpt = Optional.empty();
        try {
            numberOpt = menuArgs.tryGetOptionalArgument(
                    "-n",
                    Integer::parseInt
            );
        } catch (ArgumentParseException e) {
            fail();
        }

        assertTrue(numberOpt.isPresent());
        assertEquals(42, numberOpt.get());




        // Case 2: only date, no time
        String input2 = "-s 2025-10-28 -e 2025-10-29";
        var args2 = CLIUtils.getSplitArgs(input2);
        MenuArguments menuArgs2 = new MenuArguments(args2);

        try {
            Optional<LocalDateTime> startOpt = menuArgs2.tryGetOptionalDateTimeArgument("-s", true);
            assertTrue(startOpt.isPresent());
            // Should default to 00:00:00 for start
            assertEquals(LocalDateTime.of(2025, 10, 28, 0, 0, 0), startOpt.get());

            Optional<LocalDateTime> endOpt = menuArgs2.tryGetOptionalDateTimeArgument("-e", false);
            assertTrue(endOpt.isPresent());
            // Should default to 23:59:59 for end
            assertEquals(LocalDateTime.of(2025, 10, 29, 23, 59, 59), endOpt.get());
        } catch (ArgumentParseException e) {
            fail(e);
        }
    }
}