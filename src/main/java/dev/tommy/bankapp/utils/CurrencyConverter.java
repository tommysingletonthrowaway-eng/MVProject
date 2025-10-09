package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;

import java.util.HashMap;

public class CurrencyConverter {
    private static final HashMap<Currency, Double> toUSD = new HashMap<>();

    static {
        toUSD.put(Currency.USD, 1.0);
        toUSD.put(Currency.GBP, 0.81);
        toUSD.put(Currency.EUR, 0.95);
    }

    public static double convert(double amount, Currency from, Currency to) {
        if (from == to)
            return amount;

        return amount * getConversionRate(from, to);
    }

    public static double getConversionRate(Currency from, Currency to) {
        return toUSD.get(to) * (1.0 / toUSD.get(from));
    }

    public static double getToUSD(Currency currency) {
        return toUSD.get(currency);
    }
}
