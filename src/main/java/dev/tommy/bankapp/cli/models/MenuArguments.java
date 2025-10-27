package dev.tommy.bankapp.cli.models;

import dev.tommy.bankapp.cli.ArgumentParseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

public class MenuArguments {
    private final String[] rawArgs;
    private int cursor = 0;

    public MenuArguments() {
        this(new String[0]);
    }

    public MenuArguments(String[] rawArgs) {
        this.rawArgs = rawArgs;
    }

    public int size() {
        return rawArgs.length;
    }

    public boolean isEmpty() {
        return rawArgs.length == 0;
    }

    public void resetCursor() {
        cursor = 0;
    }

    public void skip() {
        if (hasNext()) cursor++;
    }

    public boolean hasNext() {
        return cursor < rawArgs.length;
    }

    public Optional<Integer> tryGetInt() {
        if (!hasNext()) return Optional.empty();
        String token = rawArgs[cursor++];
        try {
            return Optional.of(Integer.parseInt(token));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Double> tryGetDouble() {
        if (!hasNext()) return Optional.empty();
        String token = rawArgs[cursor++];
        try {
            return Optional.of(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<String> tryGetString() {
        if (!hasNext()) return Optional.empty();
        return Optional.of(rawArgs[cursor++]);
    }

    public Optional<Boolean> tryGetBoolean() {
        if (!hasNext()) return Optional.empty();
        String token = rawArgs[cursor++];
        return Optional.of(Boolean.parseBoolean(token));
    }

    public Optional<LocalDateTime> tryGetOptionalDateTimeArgument(String tag, boolean isStart) throws ArgumentParseException {
        for (int i = 0; i < rawArgs.length; i++) {
            if (rawArgs[i].equals(tag)) {
                if (i + 1 >= rawArgs.length) {
                    throw new ArgumentParseException("Expected date after " + tag);
                }
                String datePart = rawArgs[i + 1];
                String dateTimeString;

                // Check if time is provided
                if (i + 2 < rawArgs.length && rawArgs[i + 2].matches("\\d{2}:\\d{2}:\\d{2}")) {
                    dateTimeString = datePart + " " + rawArgs[i + 2];
                    i += 2; // skip date + time
                } else {
                    // Only date provided
                    dateTimeString = datePart + (isStart ? " 00:00:00" : " 23:59:59");
                    i += 1; // skip only date
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return Optional.of(LocalDateTime.parse(dateTimeString, formatter));
                } catch (DateTimeParseException e) {
                    throw new ArgumentParseException("Invalid datetime format: " + dateTimeString, e);
                }
            }
        }
        return Optional.empty();
    }

    // Examples:
    // Optional<Integer> number = args2.tryGetOptionalArgument("-n", Integer::parseInt);
    public <T> Optional<T> tryGetOptionalArgument(String optionalTag, Function<String, T> parser) throws ArgumentParseException {
        for (int i = 0; i < rawArgs.length - 1; i++) {
            if (rawArgs[i].equals(optionalTag)) {
                try {
                    T value = parser.apply(rawArgs[i + 1]);
                    return Optional.of(value);
                } catch (Exception e) {
                    throw new ArgumentParseException(
                            "Failed to parse argument for tag " + optionalTag + ": " + rawArgs[i + 1], e);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<String> peek() {
        if (!hasNext()) return Optional.empty();
        return Optional.of(rawArgs[cursor]);
    }
}
