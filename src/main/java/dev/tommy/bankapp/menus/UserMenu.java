package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.cli.*;
import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.user.User;
import dev.tommy.bankapp.cli.utils.CLIUtils;
import dev.tommy.bankapp.exceptions.bankaccount.BankAccountNotFoundException;
import dev.tommy.bankapp.exceptions.user.*;

import java.util.Optional;

public class UserMenu {
    public static void showUser(User user) {
        String title = CLIUtils.getTitle(user.getUsername());
        Menu userMenu = new NumberedMenu(title, false, System.in, System.out);

        userMenu
                .addItem("Open account", "", args -> {
                    promptOpenBankAccount(user);
                    return MenuOperation.CONTINUE;
                })

                .addItem("Create new account", "", args -> {
                    promptCreateNewAccount(user);
                    return MenuOperation.CONTINUE;

                })

                .addItem("Manage budget", "", args -> {
                    BudgetMenu.showMenu(user.budgetManager);
                    return MenuOperation.CONTINUE;
                })

                .addItem("Change username", "", args -> {
                    promptChangeUsername(user);
                    return MenuOperation.CONTINUE;

                })

                .addItem("Change password", "", args -> {
                    promptChangePassword(user);
                    return MenuOperation.CONTINUE;

                })

                .addItem("Delete account", "", args -> {
                    promptDeleteAccount(user);
                    return MenuOperation.CONTINUE;

                })

                .addItem("Delete user", "", args -> {
                    boolean userDeleted = promptDeleteUser(user);
                    return userDeleted ? MenuOperation.EXIT : MenuOperation.CONTINUE;
                })

                .addItem("Logout", "", args -> {
                    IO.println("\n" + user + " logged out.");
                    return MenuOperation.EXIT;
                });

        userMenu.run();
    }

    private static void promptOpenBankAccount(User user) {
        String title = CLIUtils.getTitle("Open Bank Account");
        Menu openAccountMenu = new NumberedMenu(title, true, System.in, System.out);

        var accounts = user.getBankAccounts();
        for (BankAccount account : accounts) {
            openAccountMenu.addItem("Open account '" + account + "'", "", args -> {
                AccountMenu.showMenu(account);
                return MenuOperation.EXIT;
            });
        }
        openAccountMenu.addItem("Exit", "", args -> {
            return MenuOperation.EXIT;
        });

        openAccountMenu.run();
    }

    private static void promptChangeUsername(User user) {
        CLIUtils.printTitle("Change Account Username");

        String startUsername = user.getUsername();

        while (true) {
            Optional<String> username = CLIUtils.promptInput("Enter username or type 'exit' to exit: ");

            if (username.isEmpty()) {
                IO.println("Username change cancelled.");
                return;
            }

            try {
                BankApp.context.userService.changeUserUsername(user, username.get());
                BankApp.context.saveUsers();
                IO.println("Username changed successfully from " + startUsername + " to " + user + ".");
                CLIUtils.pressEnterToContinue();
                return;
            } catch (UserException e) {
                IO.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean promptDeleteUser(User user) {
        CLIUtils.printTitle("Delete User");

        IO.println("Are you sure you want to delete your user '" + user + "' and all bank accounts?");
        IO.println("Enter 'DELETE' to delete permanently");
        String input = CLIUtils.scanner.nextLine();

        IO.println();
        if (input.equals("DELETE")) {
            try {
                BankApp.context.userService.removeUser(user);
                IO.println("User '" + user + "' deleted. Returning to main menu.");
            } catch (UserNotFoundException e) {
                IO.println("User '" + user + "' was not in the database to be deleted. Returning to main menu.");
            }
            BankApp.context.saveUsers();

            CLIUtils.pressEnterToContinue();
            return true;
        }

        return false;
    }

    private static void promptChangePassword(User user) {
        CLIUtils.printTitle("Change Account Password");

        Optional<String> password = CLIUtils.promptInput("Enter password or type 'exit' to exit: ");
        if (password.isEmpty()) {
            IO.println("Password change cancelled..");
            return;
        }

        user.changePassword(password.get());
        BankApp.context.saveUsers();
        IO.println("User password changed successfully.");

        CLIUtils.pressEnterToContinue();
    }

    private static void promptDeleteAccount(User user) {
        String title = CLIUtils.getTitle("Delete Bank Account");
        Menu deleteAccountMenu = new NumberedMenu(title, true, System.in, System.out);

        var accounts = user.getBankAccounts();
        for (BankAccount account : accounts) {
            deleteAccountMenu.addItem("Delete account" + account, "", args -> {
                IO.println("Are you sure you want to delete account " + account + "?");
                IO.print("Write 'DELETE' to delete: ");
                String input = CLIUtils.scanner.nextLine();
                IO.println();
                if (input.equals("DELETE")) {
                    IO.println("Account '" + account + "' deleted");
                    try {
                        user.removeBankAccount(account);
                    } catch (BankAccountNotFoundException e) {
                        IO.println("Bank account '" + account + "' was not found. Cannot delete.");
                    }
                    BankApp.context.saveUsers();

                    CLIUtils.pressEnterToContinue();
                }
                return MenuOperation.EXIT;
            });
        }
        deleteAccountMenu.addItem("Exit", "", args -> {
            return MenuOperation.EXIT;
        });

        deleteAccountMenu.run();
    }

    public static void signupUser() {
        CLIUtils.printTitle("Signup User");

        Optional<String> username = CLIUtils.promptInput("Enter username or type 'exit' to exit: ");
        if (username.isEmpty()) return;
        Optional<String> password = CLIUtils.promptInput("Enter password or type 'exit' to exit: ");
        if (password.isEmpty()) return;

        try {
            BankApp.context.userService.registerUser(username.get(), password.get());
        } catch (InvalidUsernameException | InvalidPasswordException | DuplicateUserException e) {
            IO.println("Could not sign up: " + e.getMessage());
            CLIUtils.pressEnterToContinue();
            return;
        }
        BankApp.context.saveUsers();

        IO.println();
        IO.println("User created of username: " + username.get());

        CLIUtils.pressEnterToContinue();
    }

    public static User loginUser() {
        CLIUtils.printTitle("Login User");
        IO.print("Enter username: ");
        String username = CLIUtils.scanner.nextLine().trim();

        User user;
        try {
            user = BankApp.context.userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            IO.println("No user with username.");
            CLIUtils.pressEnterToContinue();
            return null;
        }

        IO.print("Enter password: ");
        String password = CLIUtils.scanner.nextLine().trim();

        if (!user.checkPassword(password)) {
            IO.println("Invalid password.");
            CLIUtils.pressEnterToContinue();
            return null;
        }

        return user;
    }

    private static void promptCreateNewAccount(User user) {
        CLIUtils.printTitle("Create New Account");
        Optional<String> accountName = CLIUtils.promptInput("Enter new account name or type 'exit' to exit: ");
        if (accountName.isEmpty()) {
            CLIUtils.pressEnterToContinue();
            return;
        }

        Menu currencyOptionsMenu = new WordMenu("--- Currency Options ---", true, System.in, System.out);

        for (Currency currency : Currency.values()) {
            currencyOptionsMenu.addItem(currency.name(), currency.getSymbol(), args -> {
                BankAccount newAccount = new BankAccount(accountName.get(), currency);
                user.addBankAccount(newAccount);
                BankApp.context.saveUsers();
                IO.println("Account '" + accountName.get() + "' created with currency " + currency);
                CLIUtils.pressEnterToContinue();
                return MenuOperation.EXIT;
            });
        }
        currencyOptionsMenu.addItem("Cancel", "", args -> {
            IO.println("Account creation cancelled.");
            return MenuOperation.EXIT;
        });

        currencyOptionsMenu.run();
    }
}
