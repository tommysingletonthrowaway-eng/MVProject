package dev.tommy.bankapp.utils;

import dev.tommy.bankapp.data.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankUtils Tests")
class BankUtilsTest {

    @ParameterizedTest(name = "Should format {1} correctly as {2}")
    @CsvSource({
            "500, EUR, €500.00",
            "500, GBP, £500.00",
            "500, USD, $500.00",
            "-123.45, USD, $-123.45"
    })
    void shouldFormatMoneyUsingCurrency(double amount, Currency currency, String expectedFormatted) {
        String result = BankUtils.formatMoney(amount, currency);
        assertEquals(expectedFormatted, result);
    }

    @Test
    @DisplayName("Should format money when symbol is passed directly")
    void shouldFormatMoneyUsingStringSymbol() {
        assertEquals("$100.00", BankUtils.formatMoney(100, "$"));
        assertEquals("€99.99", BankUtils.formatMoney(99.99, "€"));
    }

    @Test
    @DisplayName("Should return comma-separated list of all currency codes")
    void shouldReturnCurrencyList() {
        String result = BankUtils.getCurrencyList();
        assertEquals("USD,EUR,GBP", result, "Currency list should match enum order");
    }
}
