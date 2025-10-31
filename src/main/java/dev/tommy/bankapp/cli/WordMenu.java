package dev.tommy.bankapp.cli;

import dev.tommy.bankapp.cli.models.MenuArguments;
import dev.tommy.bankapp.cli.models.MenuItem;
import dev.tommy.bankapp.cli.utils.CLIUtils;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Supplier;

public class WordMenu extends Menu {
    public WordMenu(Supplier<String> titleProvider, boolean autoExit, InputStream in, PrintStream out) {
        super(titleProvider, autoExit, in, out);
    }

    @Override
    public void display() {
        out.println();
        out.println(getTitle());
        out.println("Enter your command (use 'help' for a list of commands)");
    }

    @Override
    public MenuOperation handleInput() {
        out.print("> ");

        String line;
        try {
            line = scanner.nextLine();
        } catch (Exception _) {
            return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE;
        }

        String[] args = CLIUtils.getSplitArgs(line);
        if (args.length == 0) {
            // No input entered
            return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE;
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

        out.println("Invalid command.");
        return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE;
    }

    private void printHelp() {
        out.println();

        out.println();
        out.println("Available commands:");

        int maxLabelLength = getMaxItemLabelLength();
        for (MenuItem item : items) {
            String paddedLabel = String.format("%-" + maxLabelLength + "s", item.label());
            out.println("  - " + paddedLabel + " : " + item.description());
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

    private int getMaxItemLabelLength() {
        return items.stream()
                .mapToInt(item -> item.label().length())
                .max()
                .orElse(0);
    }
}

