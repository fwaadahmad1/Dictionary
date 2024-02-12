/*
 * File: Dictionary
 * Created By: Fwaad Ahmad
 * Created On: 11-02-2024
 */

import java.util.*;

public class Dictionary {
    public static final int NEARBY_BUCKETS_TO_FETCH = 1;
    private static Dictionary dictionaryInstance = null;

    private final Map<Integer, HashMap<String, String>> dictionary = new HashMap<>();

    /**
     * constructor is declared private to create a singleton class
     */
    private Dictionary() {
    }

    /**
     * this method returns a singleton instance of Dictionary Class
     *
     * @return {@link Dictionary}
     */
    public static Dictionary getInstance() {
        if (dictionaryInstance == null) dictionaryInstance = new Dictionary();
        return dictionaryInstance;
    }

    /**
     * This method is used to hash the given word in buckets
     *
     * @param word {@link String} word to be hashed
     * @return {@link Long} hashed value
     */
    private int hashWord(String word) {
        return word.length() % 10;
    }

    /**
     * this method is used to get the bucket of words from the Map
     *
     * @param word {@link String} word for which bucket should be fetched
     * @return {@link HashMap} hashmap containing all the words and definitions in that bucket
     */
    private HashMap<String, String> getBucket(String word) {
        HashMap<String, String> toReturn = new HashMap<>();
        int hash = hashWord(word);
        // get the bucket and nearby buckets
        for (int i = -NEARBY_BUCKETS_TO_FETCH; i <= NEARBY_BUCKETS_TO_FETCH; i++) {
            int newHash = hash + i;
            // if newHash < 0, go to 9, 8 ,7....
            if (newHash < 0) newHash = 10 - newHash;
            toReturn.putAll(dictionary.getOrDefault(newHash, new HashMap<>()));
        }
        return toReturn;
    }

    /**
     * this method is used to get the provided word and definition from the dictionary
     *
     * @param word {@link String} word to lookup.
     * @return {@link DictionaryEntry} class containing the word and its definition
     * @throws NoSuchElementException if no word found in the dictionary
     */
    public DictionaryEntry get(String word) {
        String result = getBucket(word.toLowerCase()).get(word.toLowerCase());
        if (result == null) throw new NoSuchElementException(word + " was not found in dictionary.");
        return new DictionaryEntry(word, result);
    }

    /**
     * this method searched for the words in dictionary that are similar to given word
     *
     * @param inputWord {@link String} word for which similar words are to be found
     * @return {@link List<DictionaryEntry>} list of objects containing similar words and their definitions
     */
    public List<DictionaryEntry> fuzzySearch(String inputWord) {
        HashMap<String, String> bucket = getBucket(inputWord.toLowerCase());
        return findSimilarWords(inputWord.toLowerCase(), bucket);
    }

    /**
     * this method is used to find similar words to inputWord from given hashMap
     *
     * @param inputWord {@link String} word for which to find similar words
     * @param pairs     {@link HashMap} hashmap from which similar words are to be found
     * @return {@link List<DictionaryEntry>} list of all the similar words
     */
    private List<DictionaryEntry> findSimilarWords(String inputWord, HashMap<String, String> pairs) {
        List<DictionaryEntry> similarPairs = new ArrayList<>();
        pairs.forEach((key, value) -> {
            if (DictionaryUtil.isSimilar(inputWord, key)) {
                similarPairs.add(new DictionaryEntry(key, value));
            }
        });
        return similarPairs;
    }

    /**
     * this method is used to add new words to the dictionary.
     *
     * @param dictionaryEntry {@link DictionaryEntry} Object containing word and definition to be added
     * @throws IllegalArgumentException if word or definition is null or blank
     */
    public void add(DictionaryEntry dictionaryEntry) {
        if (dictionaryEntry.word().isBlank() || dictionaryEntry.description().isBlank())
            throw new IllegalArgumentException("Word/Description cannot be blank");
        int hash = this.hashWord(dictionaryEntry.word());
        // creating the bucket if it does not exist, then adding word to that bucket.
        dictionary.computeIfAbsent(hash, k -> new HashMap<>())
                  .put(dictionaryEntry.word().toLowerCase(), dictionaryEntry.description());
    }

    /**
     * this method is used to remove words from the dictionary.
     *
     * @param word {@link String} word to be removed
     */
    public void remove(String word) {
        if (dictionary.getOrDefault(hashWord(word), new HashMap<>()).remove(word.toLowerCase()) == null)
            throw new IllegalArgumentException("No word " + word + " was found in the dictionary.");
    }

    /**
     * this method is used to list all words in the dictionary.
     */
    public void listAll() {
        System.out.println("Listing all Words in the dictionary");
        boolean isEmpty = dictionary.entrySet()
                                    .stream()
                                    .flatMap(bucket -> bucket.getValue().entrySet().stream())
                                    .sorted(Map.Entry.comparingByKey())
                                    .peek(entry -> System.out.printf("%s: %s\n", entry.getKey(), entry.getValue()))
                                    .count() == 0;

        if (isEmpty) System.out.println("\nNo items in dictionary");
    }
}
