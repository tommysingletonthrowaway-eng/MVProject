package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.cli.ArrowMenu;
import dev.tommy.bankapp.cli.Menu;
import dev.tommy.bankapp.cli.MenuOperation;
import dev.tommy.bankapp.cli.NumberedMenu;
import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.cli.utils.CLIUtils;

public class MainMenu {
    public static void mainMenu() {
        String title = CLIUtils.getTitle("Bank Management CLI");
        Menu mainMenu = new NumberedMenu(title, false, System.in, System.out);

        mainMenu
                .addItem("Login", "", args -> {
                    User user = UserMenu.loginUser();
                    if (user != null) {
                        UserMenu.showUser(user);
                    }
                    return MenuOperation.CONTINUE;
                })

                .addItem("Signup", "", args -> {
                    UserMenu.signupUser();
                    return MenuOperation.CONTINUE;
                })

                .addItem("List Users", "", args -> {
                    printAllUsers();
                    return MenuOperation.CONTINUE;
                })

                .addItem("Reset All Data", "", args -> {
                    promptResetData();
                    return MenuOperation.CONTINUE;
                })

                .addItem("Exit", "", args -> {
                    IO.println();
                    IO.println("Thank you for using Bank Management CLI!");
                    IO.println("See you next time!");
                    return MenuOperation.EXIT;
                });

        mainMenu.run();
    }

    private static void promptResetData() {
        String input;

        CLIUtils.printTitle("Reset All Data");
        IO.println("Are you sure you want to reset all data?");
        IO.print("Enter 'RESET' to reset: ");
        input = CLIUtils.scanner.nextLine();
        IO.println();
        if (input.equals("RESET")) {
            BankApp.context.resetUsers();
            IO.println("All data reset.");

            CLIUtils.pressEnterToContinue();
            return;
        }

        IO.println("Reset cancelled.");
    }

    private static void printAllUsers() {
        CLIUtils.printTitle("All Users");
        for (String username : BankApp.context.userService.getUsernames()) {
            IO.println("- " + username);
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void onExitProgram() {
        CLIUtils.printTitle("Exiting Program");

        boolean saved = BankApp.context.saveUsers();
        if (!saved) {
            IO.println("Users could not be saved.");
        }

        IO.println("Thank you for using Bank Management System - see you next time!");
    }
}
