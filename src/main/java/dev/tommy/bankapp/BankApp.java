package dev.tommy.bankapp;

import dev.tommy.bankapp.data.AppContext;
import dev.tommy.bankapp.menus.MainMenu;

public class BankApp {
    public static final AppContext context;

    static {
        context = new AppContext();
    }

    static void main() {
        MainMenu.mainMenu();
    }
}
