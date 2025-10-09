package dev.tommy.bankapp.utils;

import java.util.Scanner;

public class CLIUtils {
    public static Scanner scanner = new Scanner(System.in);

    public static void pressEnterToContinue() {
        IO.println("Press enter to continue...");
        scanner.nextLine();
    }
}
