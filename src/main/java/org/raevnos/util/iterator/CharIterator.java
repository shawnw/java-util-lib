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

    public CharIterator(CharSequence source) {
        Objects.requireNonNull(source);
        this.source = source;
        this.i = 0;
    }

    @Override public boolean hasNext() {
        return i < source.length();
    }

    @Override public Character next() {
        return Character.valueOf(source.charAt(i++));
    }

    @Override public int characteristics() {
        return IMMUTABLE | NONNULL | ORDERED | SIZED;
    }

    @Override public long estimateSize() {
        return source.length();
    }

    @Override public long getExactSizeIfKnown() {
        return source.length();
    }

    @Override public boolean tryAdvance(Consumer<? super Character> action) {
        if (i < source.length()) {
            action.accept(Character.valueOf(source.charAt(i++)));
            return true;
        } else {
            return false;
        }
    }

    @Override public void forEachRemaining(Consumer<? super Character> action) {
        int len = source.length();
        while (i < len) {
            action.accept(Character.valueOf(source.charAt(i++)));
        }
    }

    @Override public Spliterator<Character> trySplit() {
        return null;
    }
}
