package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.cli.ArgumentParseException;
import dev.tommy.bankapp.cli.Menu;
import dev.tommy.bankapp.cli.MenuOperation;
import dev.tommy.bankapp.cli.WordMenu;
import dev.tommy.bankapp.data.BankAccount;
import dev.tommy.bankapp.data.Currency;
import dev.tommy.bankapp.utils.BankUtils;
import dev.tommy.bankapp.cli.utils.CLIUtils;
import dev.tommy.bankapp.utils.CurrencyConverter;

import java.time.LocalDateTime;
import java.util.Optional;

public class AccountMenu {
    public static void showMenu(BankAccount account) {
        Menu accountMenu = new WordMenu(() -> CLIUtils.getTitle(account + " - Account Menu"), false, System.in, System.out);

        accountMenu
                .addItem("balance", "Check the balance in the account", args -> {
                    printBalance(account);
                    return MenuOperation.CONTINUE;
                })

                .addItem("deposit", "Deposit an amount into account using 'deposit d' where d is amount", args -> {
                    Optional<Double> amount = args.tryGetDouble();
                    amount.ifPresent(aDouble -> promptDeposit(account, aDouble));
                    return MenuOperation.CONTINUE;
                })

                .addItem("withdraw", "Withdraw an amount from your account using 'withdraw d' where d is amount", args -> {
                    Optional<Double> amount = args.tryGetDouble();
                    amount.ifPresent(aDouble -> promptWithdrawal(account, aDouble));
                    return MenuOperation.CONTINUE;
                })

                .addItem("currency", "Show current currency type of the account", args -> {
                    IO.println("Currency of account is " + account.getCurrency() + ".");
                    CLIUtils.pressEnterToContinue();
                    return MenuOperation.CONTINUE;
                })

                .addItem("setcurrency", "Set currency to different type using 'setcurrency c $b'" +
                                "\n         c - currency type of '" + BankUtils.getCurrencyList() + "'" +
                                "\n         $b - optional auto conversion of balance to the new currency type ('y' or 'n')",
                        args -> {
                            Optional<String> currencyStr = args.tryGetString();
                            if (currencyStr.isEmpty()) {
                                return MenuOperation.CONTINUE;
                            }
                            Currency newCurrency = Currency.valueOf(currencyStr.get());

                            Optional<String> autoConvertStr = args.tryGetString();
                            autoConvertStr.ifPresentOrElse(
                                    value -> {
                                        boolean autoConvertFlag = value.equalsIgnoreCase("y");
                                        promptChangeCurrency(account, newCurrency, autoConvertFlag);
                                    },
                                    () -> promptChangeCurrency(account, newCurrency)
                            );

                            return MenuOperation.CONTINUE;
                        })

                .addItem("transactions",
                        """
                                View your transaction history.
                                         Usage: 'transactions [-s startDateTime] [-e endDateTime]'
                                                  -s startDateTime : optional start filter, format 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm:ss'
                                                  -e endDateTime   : optional end filter, format 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm:ss'
                                         Examples:
                                                  transactions                     # shows all transactions
                                                  transactions -s 2025-10-27       # shows transactions from 2025-10-27 00:00:00
                                                  transactions -s 2025-10-27 10:00:00 -e 2025-10-27 15:00:00  # filters by datetime range""", args -> {
                    Optional<LocalDateTime> startTime = Optional.empty();
                    try {
                        startTime = args.tryGetOptionalDateTimeArgument("-s", true);
                    } catch (ArgumentParseException e) {
                        IO.println("Start date-time could not be parsed. Will default to max start date. " + e.getMessage());
                    }

                    Optional<LocalDateTime> endTime = Optional.empty();
                    try {
                        endTime = args.tryGetOptionalDateTimeArgument("-e", false);
                    } catch (ArgumentParseException e) {
                        IO.println("End date-time could not be parsed. Will default to max start date. " + e.getMessage());
                    }

                    showTransactionHistory(account, startTime.orElse(null), endTime.orElse(null));
                    return MenuOperation.CONTINUE;
                })

                .addItem("logout", "Logout of this bank account", args -> {
                    IO.println("Logged out of account " + account + ".");
                    return MenuOperation.EXIT;
                })

                .run();
    }


    private static void showTransactionHistory(BankAccount account, LocalDateTime startTime, LocalDateTime endTime) {
        CLIUtils.printTitle("Transaction History: ");

        var filteredHistory = account.filterTransactionsByDateTime(startTime, endTime);
        if (filteredHistory.isEmpty()) {
            IO.println("No transaction history found for time period.");
        } else {
            for (var transactionEntry : account.filterTransactionsByDateTime(startTime, endTime)) {
                IO.println("    " + transactionEntry.getValue());
            }
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void promptChangeCurrency(BankAccount account, Currency newCurrency) {
        Currency currentCurrency = account.getCurrency();
        double currentBalance = account.getBalance();
        double convertedBalance = CurrencyConverter.convert(currentBalance, currentCurrency, newCurrency);
        String currentBalanceStr = BankUtils.formatMoney(currentBalance, currentCurrency);
        String convertedBalanceStr = BankUtils.formatMoney(convertedBalance, newCurrency);

        IO.println("Do you want to auto convert your bank balance from " + currentBalanceStr + " to " + convertedBalanceStr);
        IO.print("y/n: ");
        boolean shouldConvertBalance = CLIUtils.scanner.nextLine().equalsIgnoreCase("y");
        IO.print("");

        promptChangeCurrency(account, newCurrency, shouldConvertBalance);
    }

    private static void promptChangeCurrency(BankAccount account, Currency newCurrency, boolean autoConvert) {
        account.setCurrency(newCurrency, autoConvert);
        BankApp.context.saveUsers();

        IO.println();
        IO.println("Currency updated successfully" + (autoConvert ? " with balance conversion." : "."));
        CLIUtils.pressEnterToContinue();
    }

    private static void promptWithdrawal(BankAccount account, double amount) {
        boolean withdrawSuccessful = account.withdraw(amount);

        if (withdrawSuccessful) {
            BankApp.context.saveUsers();
            IO.println("Withdrawn: " + BankUtils.formatMoney(amount, account.getCurrency())
                    + ". New balance: " + account.getFormattedBalance());
        } else {
            if (account.getBalance() < amount) {
                IO.println("Withdrawal unsuccessful. Not enough balance for the withdrawal.");
            } else if (amount < 0) {
                IO.println("Withdrawal unsuccessful. Cannot withdraw negative amount.");
            } else {
                IO.println("Withdrawal unsuccessful.");
            }
        }

        CLIUtils.pressEnterToContinue();
    }

    private static void promptDeposit(BankAccount account, double amount) {
        boolean depositSuccessful = account.deposit(amount);

        if (depositSuccessful) {
            BankApp.context.saveUsers();
            IO.println("Deposited: " + BankUtils.formatMoney(amount, account.getCurrency())
                    + ". New balance: " + account.getFormattedBalance());
        } else {
            if (amount < 0) {
                IO.println("Deposit unsuccessful. Cannot deposit negative amount.");
            } else {
                IO.println("Deposit unsuccessful.");
            }
        }
        CLIUtils.pressEnterToContinue();
    }


    private static void printBalance(BankAccount account) {
        CLIUtils.printTitle("Balance");

        IO.println("Balance: " + account.getFormattedBalance());
        CLIUtils.pressEnterToContinue();
    }
}
