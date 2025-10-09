import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    // TODO:
    // Understand why this is needed?
    // It seems Java will auto generate this if needed at compile time
    // And I don't really understand why it is needed
    // Especially if it just remains constant
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private final List<BankAccount> bankAccounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        bankAccounts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public BankAccount getBankAccount(int index) {
        return this.bankAccounts.get(index);
    }

    public List<BankAccount> getBankAccounts() {
        return this.bankAccounts;
    }

    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
    }

//    public boolean hasAccountNamed(String accountName) {
//        for (BankAccount bankAccount : this.bankAccounts) {
//            if (bankAccount.getIdentifier().equals(accountName)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public boolean hasAccountNamed(String accountName) {
        return this.getBankAccounts().stream()
                .anyMatch(bankAccount -> bankAccount.getIdentifier().equals(accountName));
    }

    public void deleteBankAccount(BankAccount account) {
        this.bankAccounts.remove(account);
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
