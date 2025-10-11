package dev.tommy.bankapp.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;

public class NumberedMenu extends Menu {
    public NumberedMenu(String title, boolean autoExit, InputStream in, PrintStream out) {
        super(title, autoExit, in, out);
    }

    @Override
    public void display() {
        out.println();
        out.println(title);
        for (int i = 0; i < items.size(); i++) {
            out.println("  " + (i + 1) + ". " + items.get(i));
        }
    }

    @Override
    public MenuOperation handleInput() {
        out.print("Enter number: ");

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception _) {
            return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE;
        } finally {
            scanner.nextLine();     // Ensure \n is cleared
        }


        if(choice == 0) {
            return MenuOperation.EXIT;
        }

        if (choice < 1 || choice > items.size()) {
            out.println("Invalid choice");
            return autoExit ? MenuOperation.EXIT : MenuOperation.CONTINUE;
        }

        MenuItem item = items.get(choice - 1);
        return item.select(new MenuArguments());
    }
}
