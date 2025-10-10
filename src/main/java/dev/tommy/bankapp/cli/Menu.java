package dev.tommy.bankapp.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Menu {
    protected String title;
    protected List<MenuItem> items = new ArrayList<>();
    protected Scanner scanner;
    protected PrintStream out;

    public Menu(String title, InputStream in, PrintStream out) {
        this.title = title;
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public abstract void display();
    public abstract MenuOperation handleInput();

    public void addItem(MenuItem item) {
        items.add(item);
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
}
