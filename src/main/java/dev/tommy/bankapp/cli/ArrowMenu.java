package dev.tommy.bankapp.cli;

import dev.tommy.bankapp.cli.models.MenuArguments;
import dev.tommy.bankapp.cli.utils.CLIUtils;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Supplier;

public class ArrowMenu extends Menu {
    private boolean firstRender = true;
    private int selectedIndex = 0;

    public ArrowMenu(Supplier<String> titleProvider, boolean autoExit, InputStream in, PrintStream out) {
        super(titleProvider, autoExit, in, out);
    }

    @Override
    public void display() {
        if (firstRender) {
            firstRender = false;
        } else {
            moveCursorToTopOfMenu();
        }

        out.println();
        out.println(getTitle());
        for (int i = 0; i < items.size(); i++) {
            out.println((i == selectedIndex ? "  ->" : "    ") + items.get(i));
        }
    }

    private void moveCursorToTopOfMenu() {
        int linesToMoveUp = items.size() + 2;
        for (int i = 0; i < linesToMoveUp; i++) {
            out.print("\033[F"); // move cursor up one line
        }
    }

    @Override
    public MenuOperation handleInput() {
        CLIUtils.KeyPress key = CLIUtils.KeyPress.UNKNOWN;

        try {
            key = CLIUtils.readKeyRaw();
        } catch (Exception _) { }

        switch (key) {
            case LEFT, RIGHT -> { }
            case UP -> selectedIndex = Math.max(0, selectedIndex - 1);
            case DOWN -> selectedIndex = Math.min(items.size() - 1, selectedIndex + 1);
            case ENTER -> { return items.get(selectedIndex).select(new MenuArguments()); }
            default -> { return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE; }
        }

        return MenuOperation.CONTINUE;
    }
}
