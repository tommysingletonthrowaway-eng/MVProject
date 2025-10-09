package dev.tommy.bankapp.data;

public enum Currency {
    USD("$"),
    EUR("€"),
    GBP("£");

    // Same as readonly in C#
    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
