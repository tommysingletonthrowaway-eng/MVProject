package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {
    @Test
    void testUsdToEurConversion() {
        double amount = 100;
        double result = CurrencyConverter.convert(amount, Currency.USD, Currency.EUR);
        assertEquals(95, result, 0.01); // assuming 1 USD = 0.85 EUR
    }

    @Test
    void testEurToGbpConversion() {
        double amount = 100;
        Currency from = Currency.EUR;
        Currency to = Currency.GBP;
        double result = CurrencyConverter.convert(amount, from, to);

        double usdAmount = amount / CurrencyConverter.getToUSD(from);
        double expectedResult = usdAmount * CurrencyConverter.getToUSD(to);
        assertEquals(expectedResult, result, 0.01); // assuming 1 USD = 0.85 EUR
    }
}