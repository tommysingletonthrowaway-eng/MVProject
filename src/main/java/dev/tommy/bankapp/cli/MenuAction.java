package dev.tommy.bankapp.cli;

@FunctionalInterface
public interface MenuAction {
    MenuOperation execute(MenuArguments args);
}

