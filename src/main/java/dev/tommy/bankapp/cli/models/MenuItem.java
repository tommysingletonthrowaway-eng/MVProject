package dev.tommy.bankapp.cli.models;

import dev.tommy.bankapp.cli.MenuOperation;
import dev.tommy.bankapp.cli.interfaces.MenuAction;

public record MenuItem(String label, String description, MenuAction action) {
    public MenuOperation select(MenuArguments arguments) {
        return action.execute(arguments);
    }

    @Override
    public String toString() {
        return label;
    }
}