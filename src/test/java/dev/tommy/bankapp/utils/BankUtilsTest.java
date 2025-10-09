package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankUtilsTest {
    @Test
    void testFormatCurrency() {
        double amount = 500;
        Currency currency;
        String formattedAmount;

        currency = Currency.EUR;
        formattedAmount = BankUtils.formatMoney(amount, currency);
        assertEquals("€500.00", formattedAmount);

        currency = Currency.GBP;
        formattedAmount = BankUtils.formatMoney(500, currency);
        assertEquals("£500.00", formattedAmount);

        currency = Currency.USD;
        formattedAmount = BankUtils.formatMoney(500, currency);
        assertEquals("$500.00", formattedAmount);
    }
}