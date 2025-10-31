package dev.tommy.bankapp.cli;

import dev.tommy.bankapp.cli.interfaces.MenuAction;
import dev.tommy.bankapp.cli.models.MenuItem;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public abstract class Menu {
    protected Supplier<String> titleSupplier;
    protected boolean autoExit;
    protected List<MenuItem> items = new ArrayList<>();
    protected Scanner scanner;
    protected PrintStream out;

    public Menu(Supplier<String> titleSupplier, boolean autoExit, InputStream in, PrintStream out) {
        this.titleSupplier = titleSupplier;
        this.autoExit = autoExit;
        this.scanner = new Scanner(in);
        this.out = out;
    }

    protected abstract void display();
    protected abstract MenuOperation handleInput();

    public Menu addItem(String name, String description, MenuAction action) {
        return addItem(new MenuItem(name, description, action));
    }

    public Menu addItem(MenuItem item) {
        items.add(item);
        return this;
    }

    public void run() {
        while(true) {
            display();
            MenuOperation operation = handleInput();

            if (operation == MenuOperation.EXIT) {
                return;
            }
        }
    }

    protected String getTitle() {
        return titleSupplier.get();
    }
}
