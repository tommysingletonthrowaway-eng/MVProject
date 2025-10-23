package dev.tommy.bankapp.cli.utils;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class CLIUtils {
    public static Scanner scanner = new Scanner(System.in);

    public static void pressEnterToContinue() {
        IO.println("Press enter to continue...");
        scanner.nextLine();
    }

    public static Optional<String> promptInput(String prompt) {
        IO.print(prompt);
        String input = CLIUtils.scanner.nextLine().trim();

        if (input.isEmpty() || input.equalsIgnoreCase("exit")) {
            return Optional.empty();
        }

        return Optional.of(input);
    }

    public static String getTitle(String str) {
        return "===== " + str + " =====";
    }

    public static void printTitle(String str) {
        IO.println("\n" + getTitle(str));
    }

    public static void printSeparator() {
        IO.println("------------------------------");
    }

    public static String[] getSplitArgs(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        return line.trim().split("\\s+");
    }

    public static KeyPress readKeyRaw() {
        enableRawMode();
        try {
            return readKey();
        } catch (IOException e) {
            return KeyPress.UNKNOWN;
        } finally {
            disableRawMode();
        }
    }

    private static KeyPress readKey() throws IOException {
        int first = System.in.read();

        if (first == 27) { // ESC
            int second = System.in.read();
            if (second == 91) {
                int third = System.in.read();
                return switch (third) {
                    case 65 -> KeyPress.UP;
                    case 66 -> KeyPress.DOWN;
                    case 67 -> KeyPress.RIGHT;
                    case 68 -> KeyPress.LEFT;
                    default -> KeyPress.UNKNOWN;
                };
            }
        } else if (first == 10 || first == 13) { // Enter (LF or CR)
            return KeyPress.ENTER;
        } else if (first == 'q' || first == 'Q') {
            return KeyPress.Q;
        } else if (first == 'h' || first == 'H') {
            return KeyPress.H;
        }

        return KeyPress.UNKNOWN;
    }

    public static void enableRawMode() {
        try {
            new ProcessBuilder("/bin/sh", "-c", "stty -echo raw < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Failed to enable raw mode: " + e.getMessage());
        }
    }


    public static void disableRawMode() {
        try {
            new ProcessBuilder("/bin/sh", "-c", "stty echo cooked < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Failed to disable raw mode: " + e.getMessage());
        }
    }

    public enum KeyPress {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ENTER,
        Q,
        H,
        UNKNOWN
    }
}
