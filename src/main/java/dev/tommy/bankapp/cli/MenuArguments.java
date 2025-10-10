package dev.tommy.bankapp.cli;

import java.util.Optional;

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

    public Optional<String> peek() {
        if (!hasNext()) return Optional.empty();
        return Optional.of(rawArgs[cursor]);
    }
}
