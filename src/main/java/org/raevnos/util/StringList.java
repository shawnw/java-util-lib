package org.raevnos.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.io.Serializable;

import org.raevnos.util.iterator.CharIterator;

/**
 * An unmodifiable, space-efficient {@code List<Character>} view of a
 * backing {@code String}.
 */
public class StringList extends AbstractList<Character>
    implements RandomAccess, Serializable {
    private final String source;

    /**
     * Create a {@code StringList} backed by the given {@code String}.
     * @throws NullPointerException if passed a {@code null} argument.
     */
    public StringList(String source) {
        super();
        Objects.requireNonNull(source);
        this.source = source;
    }

    @Override public Character get(int i) {
        return Character.valueOf(source.charAt(i));
    }

    @Override public int size() {
        return source.length();
    }

    /**
     * Look for the first matching {@code Character} in the list.
     * @param o The character to look for.
     * @return The index of the character or -1 if not found.
     * @throw ClassCastException if the argument is not a {@code Character}
     * @throw NullPointerException if the argument is {@code null}
     */
    @Override public int indexOf(Object o) {
        Objects.requireNonNull(o);
        if (o instanceof Character) {
            char c = (Character)o;
            return source.indexOf(c);
        } else {
            throw new ClassCastException("Argument must be a Character");
        }
    }

    /**
     * Look for the first matching codepoint in the list like {@code
     * String.indexOf(int)}
     * @return The index, or -1 if not found
     */
    public int indexOf(int c) {
        return source.indexOf(c);
    }

    /**
     * Look for the last matching {@code Character} in the list.
     * @param o The character to look for.
     * @return The index of the character or -1 if not found.
     * @throw ClassCastException if the argument is not a {@code Character}
     * @throw NullPointerException if the argument is {@code null}
     */
    @Override public int lastIndexOf(Object o) {
        Objects.requireNonNull(o);
        if (o instanceof Character) {
            char c = (Character)o;
            return source.lastIndexOf(c);
        } else {
            throw new ClassCastException("Argument must be a Character");
        }
    }

    /**
     * Look for the last matching codepoint in the list like {@code
     * String.lastIndexOf(int)}
     * @return The index, or -1 if not found
     */
    public int lastIndexOf(int c) {
        return source.lastIndexOf(c);
    }

    @Override
    public Iterator<Character> iterator() {
        return new CharIterator(source);
    }

    @Override
    public Spliterator<Character> spliterator() {
        return new CharIterator(source);
    }

    static private Character intToCharacter(int c) {
        return Character.valueOf((char) c);
    }

    @Override public Stream<Character> stream() {
        return source.chars().mapToObj(StringList::intToCharacter);
    }

    @Override public Stream<Character> parallelStream() {
        return source.chars().parallel().mapToObj(StringList::intToCharacter);
    }

    @Override public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override public boolean equals(Object o) {
        StringList other = (StringList)o;
        return source.equals(other.source);
    }

    @Override public int hashCode() {
        return source.hashCode();
    }
}
