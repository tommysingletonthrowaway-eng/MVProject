package dev.tommy.bankapp.menus;

import dev.tommy.bankapp.BankApp;
import dev.tommy.bankapp.cli.Menu;
import dev.tommy.bankapp.cli.MenuOperation;
import dev.tommy.bankapp.cli.NumberedMenu;
import dev.tommy.bankapp.cli.utils.CLIUtils;
import dev.tommy.bankapp.data.budget.BudgetEntry;
import dev.tommy.bankapp.data.budget.BudgetManager;
import dev.tommy.bankapp.exceptions.budget.CategoryAlreadyExistsException;
import dev.tommy.bankapp.exceptions.budget.CategoryDoesNotExistException;

import java.util.Optional;

public class BudgetMenu {
    public static void showMenu(BudgetManager budgetManager) {
        String title = CLIUtils.getTitle("Manage Your Budget");
        Menu budgetMenu = new NumberedMenu(title, false, System.in, System.out);

        budgetMenu
                .addItem("Generate budget report", "", args -> {
                    IO.println();
                    budgetManager.printSummary();
                    CLIUtils.pressEnterToContinue();
                    return MenuOperation.CONTINUE;
                })

                .addItem("Add budget entry", "", args -> {
                    promptAddBudgetEntry(budgetManager);
                    return MenuOperation.CONTINUE;
                })

                .addItem("Remove budget entry", "", args -> {
                    promptRemoveBudgetEntry(budgetManager);
                    return MenuOperation.CONTINUE;
                })

                .addItem("View all budget entries", "", args -> {
                    IO.println();
                    budgetManager.printAllEntries();
                    IO.println();
                    CLIUtils.pressEnterToContinue();
                    return MenuOperation.CONTINUE;
                })

                .addItem("Delete all budget entries", "", args -> {
                    promptRemoveAllBudgetEntries(budgetManager);
                    return MenuOperation.CONTINUE;
                })

                .addItem("Back", "", args -> {
                    IO.println("Returning to User menu");
                    return MenuOperation.EXIT;
                });

        budgetMenu.run();
    }

    private static void promptRemoveAllBudgetEntries(BudgetManager budgetManager) {
        CLIUtils.printTitle("Remove budget entry");
        var input = CLIUtils.promptInput("Type 'DELETE' to confirm removal: ");
        if (input.isPresent() && input.get().equalsIgnoreCase("DELETE")) {
            budgetManager.removeAllEntries();
            BankApp.context.saveUsers();
            IO.println("All budget entries have been removed.");
            CLIUtils.pressEnterToContinue();
        }
    }

    private static void promptRemoveBudgetEntry(BudgetManager budgetManager) {
        var title = CLIUtils.getTitle("Remove budget entry");
        Menu removeEntryMenu = new NumberedMenu(title, true, System.in, System.out );

        for (int i = 1; i < budgetManager.getEntries().size() + 1; i++) {
            var entry = budgetManager.getEntries().get(i - 1);
            final int indexToRemove = i - 1;

            removeEntryMenu.addItem(entry.category(), "", args -> {
                try {
                    budgetManager.removeEntry(indexToRemove);
                    BankApp.context.saveUsers();
                    IO.println("Entry '" + entry.category() + "' removed.");
                } catch (CategoryDoesNotExistException e) {
                    IO.println("Entry does not exist to be removed.");
                }
                CLIUtils.pressEnterToContinue();
                return MenuOperation.EXIT;
            });
        }

        removeEntryMenu.addItem("Cancel", "", args -> {
            IO.println("Returning to User menu");
            return MenuOperation.EXIT;
        });

        removeEntryMenu.run();
    }

    private static void promptAddBudgetEntry(BudgetManager budgetManager) {
        var category = promptGetCategory(budgetManager);
        if (category.isEmpty()) { return; }
        if (budgetManager.hasEntryWithCategory(category.get())) {
            IO.println("Entry with category '" + category.get() + "' already exists.");
            CLIUtils.pressEnterToContinue();
            return;
        }
        var type = promptGetBudgetEntryType();
        if (type.isEmpty()) { return; }
        var amount = promptGetAmount();
        if (amount.isEmpty()) { return; }
        if (amount.get() <= 0) {
            IO.println("Amount must be greater than zero.");
            CLIUtils.pressEnterToContinue();
            return;
        }
        var notes = promptGetNotes();

        IO.println();

        var entry = new BudgetEntry(category.get(), amount.get(), type.get(), notes.orElse(""));
        try {
            budgetManager.addEntry(entry);
            BankApp.context.saveUsers();
            IO.println("Entry '" + entry.category() + "' added.");
        } catch (CategoryAlreadyExistsException e) {
            IO.println("Category " + category.get() + " already exists.");
        }
        CLIUtils.pressEnterToContinue();
    }

    private static Optional<String> promptGetNotes() {
        return CLIUtils.promptInput("Add a note to your budget entry: ");
    }

    private static Optional<BudgetEntry.Type> promptGetBudgetEntryType() {
        IO.println("Type '1' for INCOME or '2' for EXPENSE: ");

        var index = CLIUtils.promptInput("Enter your choice: ", Integer::parseInt);
        if (index.isEmpty()) return Optional.empty();

        return switch (index.get()) {
            case 1 -> Optional.of(BudgetEntry.Type.INCOME);
            case 2 -> Optional.of(BudgetEntry.Type.EXPENSE);
            default -> {
                IO.println("Invalid option. Please choose 1 or 2.");
                yield Optional.empty();
            }
        };
    }


    private static Optional<Double> promptGetAmount() {
        return CLIUtils.promptInput("Enter amount: ", Double::parseDouble);
    }

    private static Optional<String> promptGetCategory(BudgetManager budgetManager) {
        return CLIUtils.promptInput("Category name for the entry (eg. food/rent): ");
    }
}
