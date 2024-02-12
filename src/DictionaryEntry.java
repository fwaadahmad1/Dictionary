import java.util.Objects;

/**
 * this record class is used to hold data for a single entry in the dictionary
 *
 * @param word
 * @param description
 */
public record DictionaryEntry(String word, String description) {
    public DictionaryEntry {
        Objects.requireNonNull(word);
        Objects.requireNonNull(description);
    }
}
