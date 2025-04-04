package co.edu.uptc.controller;
import java.util.Scanner;
public class InputUtils {
    public static final int Errorisnotanumber = -1;
    public static final int Errorbecauseisoutoftherange = -2;

    public static int validateChoice(Scanner scanner, int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                if (choice >= min && choice <= max) {
                    return choice; 
                }
                return Errorbecauseisoutoftherange; 
            }
            scanner.nextLine();
            return Errorisnotanumber; 
        }
    }
}
