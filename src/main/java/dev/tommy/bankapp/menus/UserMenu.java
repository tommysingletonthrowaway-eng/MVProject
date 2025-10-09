package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.User;
import dev.tommy.bankapp.utils.CLIUtils;

public class UserMenu {
    public static void showUser(User user) {
        while (true) {
            CLIUtils.printTitle("Welcome user: " + user.getUsername());

            IO.println(1 + ". Open account");
            IO.println(2 + ". Create new account");
            IO.println(3 + ". Change username");
            IO.println(4 + ". Change password");
            IO.println(5 + ". Delete account");
            IO.println(6 + ". Delete user");
            IO.println(7 + ". Logout");

            IO.println();
            IO.print("Enter your choice: ");

            String input = CLIUtils.scanner.nextLine();
            int option;
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println();
                IO.println("Invalid input.");
                continue;
            }

            switch (option) {
                case 1:
                    promptOpenBankAccount(user);
                    break;
                case 2:
                    promptCreateNewAccount(user);
                    break;
                case 3:
                    promptChangeUsername(user);
                    break;
                case 4:
                    promptChangePassword(user);
                    break;
                case 5:
                    promptDeleteAccount(user);
                    break;
                case 6:
                    boolean userDeleted = promptDeleteUser(user);
                    if (userDeleted) {
                        return;
                    }
                    break;
                case -1:
                case 7:
                    IO.println();
                    IO.println(user + " logged out.");
                    return;
                default:
                    break;
            }
        }
    }


    private static void promptOpenBankAccount(User user) {
        CLIUtils.printTitle("Open Bank Account");

        var accounts = user.getBankAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            IO.println((i + 1) + ". Open account '" + accounts.get(i).getIdentifier() + "'");
        }
        IO.println(accounts.size() + 1 + ". Exit");
        IO.println();

        IO.print("Enter your choice: ");
        String input = CLIUtils.scanner.nextLine();
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            IO.println();
            IO.println("Invalid input.");
            CLIUtils.pressEnterToContinue();
            return;
        }

        if (option >= 1 && option <= accounts.size()) {
            AccountMenu.showMenu(user.getBankAccount(option - 1));
        } else if (option == accounts.size() + 1 || option == -1) {
            IO.println();
        }
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
            BankApp.Context.userDatabase.remove(user);
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
        CLIUtils.printTitle("Delete Account");

        var accounts = user.getBankAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            IO.println((i + 1) + ". Delete account '" + accounts.get(i).getIdentifier() + "'");
        }
        IO.println((accounts.size() + 1) + ". Cancel");

        IO.println();
        IO.print("Select an account to delete: ");
        String input = CLIUtils.scanner.nextLine();
        IO.println();
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            option = -1;
        }

        if (option >= 1 && option <= accounts.size()) {
            BankAccount account = user.getBankAccount(option - 1);
            IO.println("Are you sure you want to delete account " + account + "?");
            IO.print("Enter 'DELETE' to delete: ");
            input = CLIUtils.scanner.nextLine();
            IO.println();
            if (input.equals("DELETE")) {
                IO.println("Account '" + account + "' deleted");
                user.deleteBankAccount(account);
                BankApp.saveUsers();

                CLIUtils.pressEnterToContinue();
                return;
            }
        }

        // IO.println("Cancel account deletion.");
    }

    public static void signupUser() {
        CLIUtils.printTitle("Signup User");

        String username = promptUsername();
        if (username == null) return;
        String password = promptPassword();
        if (password == null) return;

        User user = new User(username, password);
        BankApp.Context.userDatabase.add(user);
        BankApp.saveUsers();

        IO.println();
        IO.println("User created of username: " + username);

        CLIUtils.pressEnterToContinue();
    }

    public static User loginUser() {
        CLIUtils.printTitle("Login User");
        IO.print("Enter username: ");
        String username = CLIUtils.scanner.nextLine();

        if (!BankApp.Context.userDatabase.hasUser(username)) {
            IO.println("No user with username.");
            CLIUtils.pressEnterToContinue();
            return null;
        }

        IO.print("Enter password: ");
        String password = CLIUtils.scanner.nextLine();

        User user = BankApp.Context.userDatabase.getUser(username);
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
            } else if (BankApp.Context.userDatabase.hasUser(username)) {
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

        IO.println();
        IO.println("=== Create new account ===");
        String accountName = promptNewAccountName(user);
        if (accountName == null) {
            CLIUtils.pressEnterToContinue();
            return;
        }

        IO.println();
        IO.println("--- Currency Options ---");
        for (Currency currency : Currency.values()) {
            IO.println("- " + currency.name());
        }

        IO.println();
        IO.print("Choose currency type for new account: ");
        input = CLIUtils.scanner.nextLine().toUpperCase();
        IO.println();

        try {
            Currency currencySelected = Currency.valueOf(input);
            BankAccount newAccount = new BankAccount(accountName, currencySelected);
            user.addBankAccount(newAccount);
            BankApp.saveUsers();
            IO.println("Account '" + accountName + "' created with currency " + currencySelected);
            CLIUtils.pressEnterToContinue();
        } catch (IllegalArgumentException e) {
            IO.println("Currency invalid. Cancelling account creation.");
            CLIUtils.pressEnterToContinue();
        }
    }
}
