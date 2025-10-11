package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;

import java.util.Arrays;

public class BankUtils {
    public static String formatMoney(double amount, Currency currency) {
        return formatMoney(amount, currency.getSymbol());
    }

    public static String formatMoney(double amount, String currency) {
        return String.format(currency + "%.2f", amount);
    }

    public static String getCurrencyList() {
        return String.join(",",
                Arrays.stream(Currency.values())
                        .map(Currency::toString)
                        .toArray(String[]::new)
        );
    }
}
