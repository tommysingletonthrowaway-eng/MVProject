package dev.tommy.bankapp.cli.interfaces;

import dev.tommy.bankapp.cli.models.MenuArguments;
import dev.tommy.bankapp.cli.MenuOperation;

@FunctionalInterface
public interface MenuAction {
    MenuOperation execute(MenuArguments args);
}

