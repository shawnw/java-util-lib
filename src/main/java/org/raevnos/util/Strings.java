package org.raevnos.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Various static methods to manipulate {@code String} and {@code
 * CharSequence} objects.
 */
public class Strings {
    /**
     * Get a stream of {@code Character} objects from a {@code CharSequence} object.
     * @param s The sequence to get a stream from.
     * @return A {@code Stream<Character>} object.
     * @throws NullPointerException if passed a null argument
     */
    static public Stream<Character> charStream(CharSequence s) {
        Objects.requireNonNull(s);
        return s.chars().mapToObj(c -> Character.valueOf((char) c));
    }

    /**
     * Converts a {@code CharSequence} object to a list of characters.
     * No guarantees are made about the list's implementation, mutability, etc.
     * For more control, use the {@code explode(CharSequence, Supplier)} version.
     * @param s the string to convert
     * @return A {@code List<Character>} representing the string's contents
     * @throws NullPointerException if passed a null argument
     */
    static public List<Character> explode(CharSequence s) {
        return charStream(s).collect(Collectors.toList());
    }

    /**
     * Converts a {@code CharSequence} object to a user-provided
     * collection of characters.
     * @param s the string to convert
     * @param collectionFactory a supplier providing a new empty
     * {@code Collection} into which the results will be inserted
     * @return a collection of the characters of the string.
     * @throws NullPointerException if passed a null string
     */
    static public <C extends Collection<Character>> C
        explode(CharSequence s, Supplier<C> collectionFactory) {
        return charStream(s).collect(Collectors.toCollection(collectionFactory));
    }

    /**
     * Converts a {@code Collection<Character>} object into a string.
     * @return A {@code String} made from iterating over the elements
     * of the collection.
     * @throws NullPointerException if passed a null argument.
     */
    static public String implode(Collection<Character> chars) {
        Objects.requireNonNull(chars);
        StringBuilder sb = new StringBuilder(chars.size());
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
