package dev.tommy.bankapp.exceptions.budget;

public class CategoryAlreadyExistsException extends BudgetException {
    public CategoryAlreadyExistsException(String categoryName) {
        super("Category with name " + categoryName + " already exists");
    }
}
