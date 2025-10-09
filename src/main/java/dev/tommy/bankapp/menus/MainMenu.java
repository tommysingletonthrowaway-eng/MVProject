package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.utils.CLIUtils;

public class MainMenu {
    public static void mainMenu() {
        while (true) {
            CLIUtils.printTitle("Bank Management CLI");

            IO.println("1. Login");
            IO.println("2. Signup");
            IO.println("3. List Users");
            IO.println("4. Exit");
            IO.println("");
            IO.print("Enter your choice: ");

            String input = CLIUtils.scanner.nextLine();

            switch (input) {
                case "1":
                    User user = UserMenu.loginUser();
                    if (user != null) {
                        UserMenu.showUser(user);
                    }
                    break;
                case "2":
                    UserMenu.signupUser();
                    break;
                case "3":
                    printAllUsers();
                    break;
                case "-1":
                case "4":
                    onExitProgram();
                    return;
                default:
                    IO.println("Invalid choice.");
            }
        }
    }

    private static void printAllUsers() {
        CLIUtils.printTitle("All Users");
        for (User user : BankApp.Context.userDatabase.users()) {
            IO.println(user);
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void onExitProgram() {
        CLIUtils.printTitle("Exiting Program");

        boolean saved = BankApp.saveUsers();
        if (!saved) {
            IO.println("Users could not be saved.");
        }

        IO.println("Thank you for using Bank Management System - see you next time!");
    }
}
