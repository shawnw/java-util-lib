package org.raevnos.util.iterator;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.io.Reader;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.CharacterCodingException;

/**
 * A {@code Spliterator.OfInt} used to iterate over codepoints read from a file.
 * Use the methods in {@code CodePointStream} class to create streams based on it.
 */
public class CPSpliterator
    implements Spliterator.OfInt, Closeable {
    private final Reader input;

    /**
     * Create a new spliterator.
     * @param input The {@code Reader} to get codepoints from.
     */
    public CPSpliterator(Reader input) {
        this.input = Objects.requireNonNull(input);
    }

    /**
     * Fetch the next codepoint from the underlying stream, accounting for
     * surrogate pairs.
     * @return a codepoint, or -1 on end of file.
     * @throws UncheckedIOException on input errors.
     */
    private int nextCP() {
        try {
            int first_char = input.read();
            if (first_char == -1) {
                return -1;
            } else if (Character.isHighSurrogate((char)first_char)) {
                int second_char = input.read();
                if (second_char == -1
                    || !Character.isLowSurrogate((char)second_char)) {
                    // Hopefully shouldn't happen; caught by Reader first.
                    throw new CharacterCodingException();
                } else {
                    return Character.toCodePoint((char)first_char, (char)second_char);
                }
            } else {
                return first_char;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int characteristics() { return ORDERED | NONNULL; }

    @Override
    public long estimateSize() { return Long.MAX_VALUE; }

    @Override
    public void forEachRemaining(IntConsumer f) {
        int cp;
        while ((cp = nextCP()) != -1) {
            f.accept(cp);
        }
    }

    @Override
    public boolean tryAdvance(IntConsumer f) {
        int cp = nextCP();
        if (cp != -1) {
            f.accept(cp);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator.OfInt trySplit() { return null; }

    @Override
    public void close() throws IOException { input.close(); }
}
