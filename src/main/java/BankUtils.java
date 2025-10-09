public class BankUtils {
    public static String formatMoney(double amount, Currency currency) {
        return formatMoney(amount, currency.getSymbol());
    }

    public static String formatMoney(double amount, String currency) {
        return String.format(currency + "%.2f", amount);
    }

}
