/*
 * File: DictionaryUtil
 * Created By: Fwaad Ahmad
 * Created On: 11-02-2024
 */

import java.util.stream.IntStream;

/**
 * Utility class for Dictionary
 */
public class DictionaryUtil {

    public static final Integer MAX_DIFFERENCE_DISTANCE = 2;

    /**
     * private constructor so a Utility class cannot be initialized.
     */
    private DictionaryUtil() {
    }

    /**
     * this method checks the edit distance between two strings.
     *
     * @param inputWord {@link String}
     * @param word      {@link String}
     * @return {@link Boolean} representing if the edit distance is less than the max difference.
     */
    public static boolean isSimilar(String inputWord, String word) {
        return calculateEditDistance(inputWord, word) <= MAX_DIFFERENCE_DISTANCE;
    }

    /**
     * This method is used to calculate the edit distance between two strings.
     * This method follows Levenshtein Distance Computation Algorithm.
     * This implementation of Levenshtein Distance Computation Algorithm is known as Wagner-Fischer algorithm
     *
     * @param word1 {@link String}
     * @param word2 {@link String}
     * @return {@link Integer} edit distance between the two strings.
     */
    private static int calculateEditDistance(String word1, String word2) {
        // Create a 2-dimensional array to store the distances between substrings of word1 and word2
        int[][] distanceTable = new int[word1.length() + 1][word2.length() + 1];

        // Initialize the 1st row and column of the array
        // The first row represents the case when word1 is empty and word2 has characters
        // The first column represents the case when word2 is empty and word1 has characters
        IntStream.rangeClosed(0, word1.length()).forEach(i -> distanceTable[i][0] = i);
        IntStream.rangeClosed(0, word2.length()).forEach(j -> distanceTable[0][j] = j);

        // Fill in the matrix to compute the edit distances
        for (int row = 1; row <= word1.length(); row++)
            for (int col = 1; col <= word2.length(); col++) {
                // If the characters at the current positions are equal,
                // the distance is the same as the distance without considering these characters
                if (word1.charAt(row - 1) == word2.charAt(col - 1)) {
                    distanceTable[row][col] = distanceTable[row - 1][col - 1];
                } else {
                    // If the characters are different, compute the minimum distance considering three cases:
                    // 1. Replace the character in word1 to match the character in word2
                    // 2. Insert the character from word2 into word1
                    // 3. Delete the character from word1
                    distanceTable[row][col] = Math.min(distanceTable[row - 1][col - 1],
                                                   Math.min(distanceTable[row][col - 1], distanceTable[row - 1][col])) + 1;
                }
            }

        // Return the edit distance between word1 and word2, which is stored at the bottom-right cell of the matrix
        return distanceTable[word1.length()][word2.length()];
    }

}
