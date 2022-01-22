package org.raevnos.util.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * An iterator and spliterator over the characters of a {@code CharSequence}
 */

public class CharIterator implements Iterator<Character>, Spliterator<Character> {
    private final CharSequence source;
    private int i;
    private final int endIdx;

    /** Make a character iterator
     * @param source The sequence to iterate over
     * @throws NullPointerException if given a null argument
     */
    public CharIterator(CharSequence source) {
        Objects.requireNonNull(source);
        this.source = source;
        this.i = 0;
        this.endIdx = source.length();
    }

    /** Make a character iterator over a range.
     * @param source The sequence to iterate over
     * @param begIdxInclusive The starting index of the range.
     * @param endIdxExclusive One past the ending index of the range.
     * @throws NullPointerException if given a null source.
     * @throws IndexOutOfBoundsException if the range is invalid for the given source.
     */
    public CharIterator(CharSequence source, int begIdxInclusive, int endIdxExclusive) {
        Objects.requireNonNull(source);
        Objects.checkFromToIndex(begIdxInclusive, endIdxExclusive, source.length());
        this.source = source;
        this.i = begIdxInclusive;
        this.endIdx = endIdxExclusive;
    }

    @Override public boolean hasNext() {
        return i < endIdx;
    }

    @Override public Character next() {
        return Character.valueOf(source.charAt(i++));
    }

    @Override public int characteristics() {
        return IMMUTABLE | NONNULL | ORDERED | SIZED | SUBSIZED;
    }

    @Override public long estimateSize() {
        return endIdx - i;
    }

    @Override public long getExactSizeIfKnown() {
        return endIdx - i;
    }

    @Override public boolean tryAdvance(Consumer<? super Character> action) {
        if (i < endIdx) {
            action.accept(Character.valueOf(source.charAt(i++)));
            return true;
        } else {
            return false;
        }
    }

    @Override public void forEachRemaining(Consumer<? super Character> action) {
        while (i < endIdx) {
            action.accept(Character.valueOf(source.charAt(i++)));
        }
    }

    @Override public Spliterator<Character> trySplit() {
        // Return a new spliterator covering the front half of the
        // current range of characters, and adjust this one to cover
        // the back half.
        int begIdx = i;
        int splitIdx = ((endIdx - i) / 2) + i;
        // Don't break in the middle of a surrogate pair
        if (Character.isLowSurrogate(source.charAt(splitIdx))) {
            splitIdx -= 1;
        }
        i = splitIdx;
        return  new CharIterator(source, begIdx, splitIdx);
    }
}
