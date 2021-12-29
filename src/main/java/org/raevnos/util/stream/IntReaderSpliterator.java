package org.raevnos.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Scanner;
import java.util.function.IntConsumer;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.Charset;

/**
 * Spliterator that reads ints from a file source. Use {@code Readers.intStream()}
 * to create streams using it. Closing the Spliterator closes the underlying I/O source.
 */
public class IntReaderSpliterator implements Spliterator.OfInt, Closeable {
    private final Scanner scanner;

    /**
     * Create a spliterator from an open reader.
     * @param r the {@code Readable} to use as a data source.
     */
    public IntReaderSpliterator(Readable r) {
        Objects.requireNonNull(r);
        this.scanner = new Scanner(r);
    }

    /**
     * Create a spliterator from a file path. The stream/spliterator
     * should be closed when done.
    */
    public IntReaderSpliterator(Path p) throws IOException {
        Objects.requireNonNull(p);
        this.scanner = new Scanner(p);
    }

    /**
     * Create a spliterator from a file path. The stream/spliterator
     * should be closed when done.
    */
    public IntReaderSpliterator(Path p, Charset cs) throws IOException {
        Objects.requireNonNull(p);
        Objects.requireNonNull(cs);
        this.scanner = new Scanner(p, cs);
    }


    @Override
    public int characteristics() { return NONNULL | ORDERED; }

    @Override
    public long estimateSize() { return Long.MAX_VALUE; }

    @Override
    public boolean tryAdvance(IntConsumer f) {
        if (scanner.hasNextInt()) {
            f.accept(scanner.nextInt());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(IntConsumer f) {
        while (scanner.hasNextInt()) {
            f.accept(scanner.nextInt());
        }
    }

    @Override
    public Spliterator.OfInt trySplit() { return null; }

    @Override
    public void close() throws IOException { scanner.close(); }
}
