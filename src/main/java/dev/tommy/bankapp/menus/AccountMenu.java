package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.data.Transaction;
import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.utils.CLIUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;

public class AccountMenu {
    public static void showMenu(BankAccount account) {
        while (true) {
            IO.println();
            IO.println("=== " + account + " - Account Menu ===");

            IO.println("1. Check balance");
            IO.println("2. Deposit");
            IO.println("3. Withdraw");
            IO.println("4. Change currency");
            IO.println("5. Show transaction history");
            IO.println("6. Exit");
            IO.println();

            IO.print("Enter your choice: ");
            String input = CLIUtils.scanner.nextLine();

            switch (input) {
                case "1":
                    printBalance(account);
                    break;
                case "2":
                    promptDeposit(account);
                    break;
                case "3":
                    promptWithdraw(account);
                    break;
                case "4":
                    promptChangeCurrency(account);
                    break;
                case "5":
                    showTransactionHistory(account);
                    break;
                case "-1":
                case "6":
                    return;
                default:
                    IO.println("Invalid option.");
            }
        }
    }

    private static void showTransactionHistory(BankAccount account) {
        IO.println();
        IO.println("=== Transaction History ===");

        for (Transaction transaction : account.getTransactionHistory()) {
            IO.println("    " + transaction);
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void promptChangeCurrency(BankAccount account) {
        IO.println();
        IO.println("=== Change Currency ===");
        IO.println();
        IO.println("--- Currency Options ---");
        for (Currency currency : Currency.values()) {
            IO.println("- " + currency.name());
        }

        IO.println();
        IO.print("Choose new currency: ");
        String input = CLIUtils.scanner.nextLine().toUpperCase();
        IO.print("");

        Currency currentCurrency = account.getCurrency();
        Currency selectedCurrency;
        try {
            selectedCurrency = Currency.valueOf(input);
        } catch (IllegalArgumentException e) {
            IO.println("Currency invalid. Cancelling currency conversion.");
            CLIUtils.pressEnterToContinue();
            return;
        }

        double currentBalance = account.getBalance();
        double convertedBalance = CurrencyConverter.convert(currentBalance, currentCurrency, selectedCurrency);
        String currentBalanceStr = BankUtils.formatMoney(currentBalance, currentCurrency);
        String convertedBalanceStr = BankUtils.formatMoney(convertedBalance, selectedCurrency);

        IO.println("Do you want to auto convert your bank balance from " + currentBalanceStr + " to " + convertedBalanceStr);
        IO.print("y/n: ");
        boolean shouldConvertBalance = CLIUtils.scanner.nextLine().equalsIgnoreCase("y");
        IO.print("");

        account.setCurrency(selectedCurrency, shouldConvertBalance);
        BankApp.saveUsers();

        IO.println();
        IO.println("Currency updated successfully" + (shouldConvertBalance ? " with balance conversion." : "."));
        CLIUtils.pressEnterToContinue();
    }

    private static void promptWithdraw(BankAccount account) {
        IO.println();
        IO.println("=== Withdraw ===");
        IO.print("Enter withdrawal amount: ");
        try {
            double withdrawalAmount = CLIUtils.scanner.nextDouble();
            CLIUtils.scanner.nextLine();    // Consume the \n
            boolean withdrawSuccessful = account.withdraw(withdrawalAmount);

            if (withdrawSuccessful) {
                BankApp.saveUsers();
                IO.println("Withdrawn: " + BankUtils.formatMoney(withdrawalAmount, account.getCurrency())
                        + ". New balance: " + account.getFormattedBalance());
            } else {
                if (account.getBalance() < withdrawalAmount) {
                    IO.println("Withdrawal unsuccessful. Not enough balance for the withdrawal.");
                } else if (withdrawalAmount < 0) {
                    IO.println("Withdrawal unsuccessful. Cannot withdraw negative amount.");
                } else {
                    IO.println("Withdrawal unsuccessful.");
                }
            }
        } catch (NumberFormatException e) {
            IO.println("Invalid amount.");
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void promptDeposit(BankAccount account) {
        IO.println();
        IO.println("=== Deposit ===");
        IO.print("Enter deposit amount: ");
        try {
            double depositAmount = CLIUtils.scanner.nextDouble();
            CLIUtils.scanner.nextLine();    // Consume the \n
            boolean depositSuccessful = account.deposit(depositAmount);

            if (depositSuccessful) {
                BankApp.saveUsers();
                IO.println("Deposited: " + BankUtils.formatMoney(depositAmount, account.getCurrency())
                        + ". New balance: " + account.getFormattedBalance());
            } else {
                if (depositAmount < 0) {
                    IO.println("Deposit unsuccessful. Cannot withdraw negative amount.");
                } else {
                    IO.println("Deposit unsuccessful.");
                }
            }
        } catch (NumberFormatException e) {
            IO.println("Invalid amount.");
        }

        CLIUtils.pressEnterToContinue();
    }


    private static void printBalance(BankAccount account) {
        IO.println();
        IO.println("=== Balance ===");
        IO.println("Balance: " + account.getFormattedBalance());
        CLIUtils.pressEnterToContinue();
    }
}
