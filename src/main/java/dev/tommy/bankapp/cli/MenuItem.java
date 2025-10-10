package dev.tommy.bankapp.cli;

public record MenuItem(String label, String description, MenuAction action) {
    public MenuOperation select(MenuArguments arguments) {
        return action.execute(arguments);
    }

    @Override
    public String toString() {
        return label;
    }
}