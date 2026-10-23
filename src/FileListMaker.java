import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {
    private static ArrayList<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFilename = "";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean quit = false;

        do {
            displayList();
            displayMenu();

            String choice = SafeInput.getRegExString(in, "Enter menu choice", "[AaDdIiOoSsCcVvQq]").toUpperCase();

            switch (choice) {
                case "A":
                    addItem(in);
                    break;
                case "D":
                    deleteItem(in);
                    break;
                case "I":
                    insertItem(in);
                    break;
                case "O":
                    openList(in);
                    break;
                case "S":
                    saveList(in);
                    break;
                case "C":
                    clearList(in);
                    break;
                case "V":
                    break;
                case "Q":
                    quit = checkQuit(in);
                    break;
            }
        } while (!quit);

        System.out.println("\nGoodbye!");
    }

    private static void displayList() {
        System.out.println("\n=========================================");
        System.out.println("CURRENT LIST" + (currentFilename.isEmpty() ? "" : " (" + currentFilename + ")") + (needsToBeSaved ? " *" : ""));
        System.out.println("=========================================");
        if (list.isEmpty()) {
            System.out.println("[ List is empty ]");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%3d. %s\n", (i + 1), list.get(i));
            }
        }
        System.out.println("=========================================");
    }

    private static void displayMenu() {
        System.out.println("Menu Options:");
        System.out.println("  A - Add an item to the list");
        System.out.println("  D - Delete an item from the list");
        System.out.println("  I - Insert an item into a specific position");
        System.out.println("  O - Open a list file from disk");
        System.out.println("  S - Save the current list file to disk");
        System.out.println("  C - Clear all items from the list");
        System.out.println("  V - View the list");
        System.out.println("  Q - Quit the program");
    }

    private static void addItem(Scanner in) {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to add");
        list.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("\nError: The list is empty. Nothing to delete.");
            return;
        }
        int itemIndex = SafeInput.getRangedInt(in, "Enter the item number to delete", 1, list.size());
        list.remove(itemIndex - 1); // convert back to 0-indexed layout
        needsToBeSaved = true;
    }

    private static void insertItem(Scanner in) {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to insert");
        int position;
        if (list.isEmpty()) {
            System.out.println("The list is empty. Inserting item at position 1.");
            position = 1;
        } else {
            int maxPosition = list.size() + 1;
            position = SafeInput.getRangedInt(in, "Enter the position number where you want to insert this item", 1, maxPosition);
        }
        list.add(position - 1, item);
        needsToBeSaved = true;
        System.out.println("\nItem successfully inserted.");
    }

    private static void openList(Scanner in) {
        if (needsToBeSaved) {
            boolean confirmDiscard = SafeInput.getYNConfirm(in, "You have unsaved changes. Overwrite current list?");
            if (!confirmDiscard) {
                System.out.println("Open aborted.");
                return;
            }
        }

        String filename = SafeInput.getNonZeroLenString(in, "Enter the filename to open");
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        Path file = Paths.get(filename);

        if (!Files.exists(file)) {
            System.out.println("\nError: File not found: " + file.toAbsolutePath());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            list.clear(); // Clear existing memory layout
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("\nList successfully loaded from " + file.getFileName());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void saveList(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("\nError: Cannot save an empty list.");
            return;
        }

        String filename;
        if (!currentFilename.isEmpty()) {
           boolean overwrite = SafeInput.getYNConfirm(in, "Save to current file (" + currentFilename + ")?");
            if (overwrite) {
                filename = currentFilename;
            } else {
                filename = SafeInput.getNonZeroLenString(in, "Enter a new filename to save as");
            }
        } else {
            filename = SafeInput.getNonZeroLenString(in, "Enter the filename to save");
        }

        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        Path file = Paths.get(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (String item : list) {
                writer.write(item);
                writer.newLine();
            }
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("\nList successfully saved to " + file.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void clearList(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("\nList is already empty.");
            return;
        }
        boolean confirm = SafeInput.getYNConfirm(in, "Are you sure you want to clear the entire list?");
        if (confirm) {
            list.clear();
            needsToBeSaved = true;
        }
    }

    private static boolean checkQuit(Scanner in) {
        if (needsToBeSaved) {
            boolean confirmQuit = SafeInput.getYNConfirm(in, "You have unsaved changes. Quit anyway?");
            return confirmQuit;
        }
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
    }
}
