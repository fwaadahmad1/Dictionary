import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Dictionary dictionary = Dictionary.getInstance();
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.print("~~~~~ Dictionary in Java ~~~~~\n\n");


        while (true) {
            System.out.println("Please chose from the following options:");
            System.out.println("1. Lookup\n2. Add\n3. Remove\n4. List All\n5. Exit");
            System.out.print("Option: ");
            String option = input.nextLine();
            try {
                handleChoice(option);
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    /**
     * This method is used to handle the choice made by the user
     *
     * @param option {@link String} choice text entered by user
     */
    private static void handleChoice(String option) {
        switch (option) {
            // Look-up Case
            case "1" -> {
                System.out.print("Enter the word to lookup: ");
                String word = input.nextLine();
                // if the word is null or only blank spaces. break out of the case
                if (word == null || word.isBlank()) {
                    System.out.println("Please enter a valid input");
                    break;
                }
                // trying to get the word exactly. if not found then finding similar words
                try {
                    DictionaryEntry dictionaryEntry = dictionary.get(word);
                    System.out.printf("%s: %s\n", dictionaryEntry.word(), dictionaryEntry.description());
                } catch (NoSuchElementException e) {
                    // finding similar words
                    List<DictionaryEntry> dictionaryEntryList = dictionary.fuzzySearch(word);
                    if (dictionaryEntryList.isEmpty()) System.out.println(word + " was not found in the dictionary.");
                    else {
                        System.out.println("Did you mean?");
                        dictionaryEntryList.forEach(dictionaryEntry -> System.out.println(dictionaryEntry.word()));
                    }
                }
            }
            // Adding word in dictionary
            case "2" -> {
                System.out.print("Enter the word to add: ");
                String word = input.nextLine();
                System.out.print("Enter definition for the word: ");
                String definition = input.nextLine();
                // if the word or definition is null or blank space. showing invalid input.
                if (word == null || word.isBlank() || definition == null || definition.isBlank())
                    System.out.println("Please enter a valid input");
                // adding to dictionary if all checks pass
                dictionary.add(new DictionaryEntry(word, definition));
                System.out.println("Word was added!");
            }
            // Removing word from dictionary
            case "3" -> {
                System.out.print("Enter the word to remove: ");
                String word = input.nextLine();
                // if the word is null or blank space. showing invalid input.
                if (word == null || word.isBlank()) System.out.println("Please enter a valid input");
                // removing from dictionary if all checks pass
                dictionary.remove(word);
                System.out.println("Word was removed!");
            }
            // Listing all the words in the dictionary
            case "4" -> dictionary.listAll();
            // Exiting the application
            case "5" -> System.exit(0);
            // Invalid input
            case null, default -> System.out.println("Please enter a valid input.");
        }
    }
}