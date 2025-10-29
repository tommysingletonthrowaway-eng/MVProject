package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

    @ParameterizedTest
    @EnumSource(Currency.class)
    void convertSameCurrencyShouldReturnSameAmount(Currency currency) {
        double amount = 100.0;

        assertEquals(amount, CurrencyConverter.convert(amount, currency, currency),
                    "Conversion of " + currency + " to itself should return the same amount");
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void convertDifferentCurrenciesShouldBeCorrect(Currency from) {
        double amount = 100.0;

        for (Currency to : Currency.values()) {
            double expected = amount * CurrencyConverter.getConversionRate(from, to);
            double actual = CurrencyConverter.convert(amount, from, to);
            assertEquals(expected, actual, 1e-6,
                    "Conversion from " + from + " to " + to + " should be correct");
        }
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void getConversionRateShouldMatchToUSDRatio(Currency from) {
        for (Currency to : Currency.values()) {
            double expected = CurrencyConverter.getToUSD(to) / CurrencyConverter.getToUSD(from);
            double actual = CurrencyConverter.getConversionRate(from, to);
            assertEquals(expected, actual, 1e-6,
                    "Conversion rate from " + from + " to " + to + " should match USD ratio");
        }
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void getToUSDShouldReturnPositive(Currency currency) {
        assertTrue(CurrencyConverter.getToUSD(currency) > 0,
                "Conversion rate to USD should be positive for " + currency);
    }

    @Test
    void convertHandlesZeroAndNegative() {
        assertEquals(0.0, CurrencyConverter.convert(0.0, Currency.USD, Currency.GBP));
        assertEquals(-50.0 * 0.81, CurrencyConverter.convert(-50.0, Currency.USD, Currency.GBP));
    }
}
