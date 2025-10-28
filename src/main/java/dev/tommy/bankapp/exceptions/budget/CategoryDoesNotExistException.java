package dev.tommy.bankapp.exceptions.budget;

public class CategoryDoesNotExistException extends BudgetException {
    public CategoryDoesNotExistException(int index) {
        super("Category at index " + index + " does not exist");
    }
}
