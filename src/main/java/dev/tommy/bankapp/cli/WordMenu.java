package dev.tommy.bankapp.cli;

import dev.tommy.bankapp.utils.CLIUtils;

import java.io.InputStream;
import java.io.PrintStream;

public class WordMenu extends Menu {
    public WordMenu(String title, InputStream in, PrintStream out) {
        super(title, in, out);
    }

    @Override
    public void display() {
        out.println();
        out.println("===== " + title + " =====");
        out.println("Enter your command (use 'help' for a list of commands)");
    }

    @Override
    public MenuOperation handleInput() {
        out.print("> ");

        String line;
        try {
            line = scanner.nextLine();
        } catch (Exception _) {
            return MenuOperation.CONTINUE;
        }

        String[] args = CLIUtils.getSplitArgs(line);
        if (args.length == 0) {
            // No input entered, just continue
            return MenuOperation.CONTINUE;
        }

        String command = args[0];
        String[] commandArgs = new String[args.length - 1];
        if (args.length > 1) {
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
        }

        if (command.equalsIgnoreCase("help")) {
            printHelp();
            return MenuOperation.CONTINUE;
        }

        MenuItem selectedItem = getItem(command);
        if (selectedItem != null) {
            return selectedItem.select(new MenuArguments(commandArgs));
        }

        out.println("Invalid command. Try again.");
        return MenuOperation.CONTINUE;
    }

    private void printHelp() {
        out.println();
        out.println("Available commands: ");
        for (MenuItem item : items) {
            out.println("  - " + item + ": " + item.description());
        }
    }

    private MenuItem getItem(String label) {
        for (MenuItem item : items) {
            if (item.label().equalsIgnoreCase(label)) {
                return item;
            }
        }

        return null;
    }
}

