package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.cli.*;
import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.cli.utils.CLIUtils;

public class UserMenu {
    public static void showUser(User user) {
        String title = CLIUtils.getTitle(user.getUsername());
        Menu userMenu = new NumberedMenu(title, false, System.in, System.out);

        userMenu
                .addItem("Open account", "" , args -> {
                    promptOpenBankAccount(user);
                    return MenuOperation.CONTINUE;
                })
                .addItem("Create new account", "" , args -> {
                    promptCreateNewAccount(user);
                    return MenuOperation.CONTINUE;

                }).addItem("Change username", "" , args -> {
                    promptChangeUsername(user);
                    return MenuOperation.CONTINUE;

                }).addItem("Change password", "" , args -> {
                    promptChangePassword(user);
                    return MenuOperation.CONTINUE;

                }).addItem("Delete account", "" , args -> {
                    promptDeleteAccount(user);
                    return MenuOperation.CONTINUE;

                }).addItem("Delete user", "" , args -> {
                    boolean userDeleted = promptDeleteUser(user);
                    return userDeleted ? MenuOperation.EXIT : MenuOperation.CONTINUE;
                }).addItem("Logout", "" , args -> {
                    IO.println("\n"+ user + " logged out.");
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
        String username = promptUsername();
        if (username == null) {
            return;
        }

        user.changeUsername(username);
        BankApp.saveUsers();
        IO.println("Username changed successfully from " + startUsername + " to " + user + ".");

        CLIUtils.pressEnterToContinue();
    }

    private static boolean promptDeleteUser(User user) {
        CLIUtils.printTitle("Delete User");

        IO.println("Are you sure you want to delete your user '" + user + "' and all bank accounts?");
        IO.println("Enter 'DELETE' to delete permanently");
        String input = CLIUtils.scanner.nextLine();

        IO.println();
        if (input.equals("DELETE")) {
            IO.println("User '" + user + "' deleted. Returning to main menu.");
            BankApp.context.userDatabase.remove(user);
            BankApp.saveUsers();

            CLIUtils.pressEnterToContinue();
            return true;
        }

        return false;
    }

    private static void promptChangePassword(User user) {
        CLIUtils.printTitle("Change Account Password");

        String password = promptPassword();
        if (password == null) {
            return;
        }

        user.changePassword(password);
        BankApp.saveUsers();
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
                    user.deleteBankAccount(account);
                    BankApp.saveUsers();

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

        String username = promptUsername();
        if (username == null) return;
        String password = promptPassword();
        if (password == null) return;

        User user = new User(username, password);
        BankApp.context.userDatabase.add(user);
        BankApp.saveUsers();

        IO.println();
        IO.println("User created of username: " + username);

        CLIUtils.pressEnterToContinue();
    }

    public static User loginUser() {
        CLIUtils.printTitle("Login User");
        IO.print("Enter username: ");
        String username = CLIUtils.scanner.nextLine();

        if (!BankApp.context.userDatabase.hasUser(username)) {
            IO.println("No user with username.");
            CLIUtils.pressEnterToContinue();
            return null;
        }

        IO.print("Enter password: ");
        String password = CLIUtils.scanner.nextLine();

        User user = BankApp.context.userDatabase.getUser(username);
        if (!user.checkPassword(password)) {
            IO.println("Invalid password.");
            CLIUtils.pressEnterToContinue();
            return null;
        }

        return user;
    }

    private static String promptUsername() {
        while (true) {
            IO.print("Enter username or type 'exit' to exit: ");
            String username = CLIUtils.scanner.nextLine();
            String errorMessage;
            int maxCharacters = 15;
            int minCharacters = 3;

            if (username.equalsIgnoreCase("exit")) {
                return null;
            } else if (username.contains(" ")) {
                errorMessage = "Username cannot contain spaces.";
            } else if (BankApp.context.userDatabase.hasUser(username)) {
                errorMessage = "Username already exists.";
            } else if (username.length() < minCharacters) {
                errorMessage = "Username must have more than " + minCharacters + " characters.";
            } else if (username.length() > maxCharacters) {
                errorMessage = "Username must have less than " + maxCharacters + " characters.";
            } else {
                return username;
            }

            IO.println(errorMessage);
        }
    }

    private static String promptPassword() {
        while (true) {
            IO.print("Enter password or type 'exit' to exit: ");
            String password = CLIUtils.scanner.nextLine();
            String errorMessage;
            int maxCharacters = 15;
            int minCharacters = 3;

            if (password.equalsIgnoreCase("exit")) {
                return null;
            } else if (password.contains(" ")) {
                errorMessage = "Password cannot contain spaces.";
            } else if (password.length() < minCharacters) {
                errorMessage = "Password must have more than " + minCharacters + " characters.";
            } else if (password.length() > maxCharacters) {
                errorMessage = "Password must have less than " + maxCharacters + " characters.";
            } else {
                return password;
            }

            IO.println(errorMessage);
        }
    }

    private static String promptNewAccountName(User user) {
        IO.print("Choose name for new account: ");
        String accountName = CLIUtils.scanner.nextLine();
        String errorMessage;
        int maxCharacters = 15;
        int minCharacters = 3;

        if (accountName.contains(" ")) {
            errorMessage = "Account name cannot contain spaces.";
        } else if (accountName.length() < minCharacters) {
            errorMessage = "Account name must have more than " + minCharacters + " characters.";
        } else if (accountName.length() > maxCharacters) {
            errorMessage = "Account name must have less than " + maxCharacters + " characters.";
        } else if (user.hasAccountNamed(accountName)) {
            errorMessage = "Account name must have less than " + maxCharacters + " characters.";
        } else {
            return accountName;
        }

        IO.println(errorMessage);
        return null;
    }

    private static void promptCreateNewAccount(User user) {
        String input;

        CLIUtils.printTitle("Create New Account");
        String accountName = promptNewAccountName(user);
        if (accountName == null) {
            CLIUtils.pressEnterToContinue();
            return;
        }


        Menu currencyOptionsMenu = new WordMenu("--- Currency Options ---", true, System.in, System.out);

        for (Currency currency : Currency.values()) {
            currencyOptionsMenu.addItem(currency.name(), currency.getSymbol(), args -> {
                BankAccount newAccount = new BankAccount(accountName, currency);
                user.addBankAccount(newAccount);
                BankApp.saveUsers();
                IO.println("Account '" + accountName + "' created with currency " + currency);
                CLIUtils.pressEnterToContinue();
                return MenuOperation.EXIT;
            });
        }

        currencyOptionsMenu.run();
    }
}
