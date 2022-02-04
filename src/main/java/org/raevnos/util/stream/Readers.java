package org.raevnos.util.stream;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.Closeable;
import java.io.Reader;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.raevnos.util.iterator.CPSpliterator;
import org.raevnos.util.iterator.DoubleReaderSpliterator;
import org.raevnos.util.iterator.IntReaderSpliterator;
import org.raevnos.util.iterator.LongReaderSpliterator;

/**
 * Create streams from files.
 */
public class Readers {
    private Readers() {}

    static private Runnable makeCloser(Closeable c) {
        return () -> {
            try {
                c.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    static private IntStream make(IntReaderSpliterator sp) {
        return StreamSupport.intStream(sp, false).onClose(makeCloser(sp));
    }

    static private LongStream make(LongReaderSpliterator sp) {
        return StreamSupport.longStream(sp, false).onClose(makeCloser(sp));
    }

    static private DoubleStream make(DoubleReaderSpliterator sp) {
        return StreamSupport.doubleStream(sp, false).onClose(makeCloser(sp));
    }

    /**
     * @return a new {@code IntStream} that reads from the given reader.
     */
    static public IntStream intStream(Readable r) {
        return make(new IntReaderSpliterator(r));
    }

    /**
     * @return a new {@code IntStream} that reads from the given file
     * using the default character encoding. The stream should be
     * closed when finished with.
     */
    static public IntStream intStream(Path p) throws IOException {
        return make(new IntReaderSpliterator(p));
    }

    /**
     * @return a new {@code IntStream} that reads from the given file
     * using the given character encoding. The stream should be
     * closed when finished with.
     */
    static public IntStream intStream(Path p, Charset cs) throws IOException {
        return make(new IntReaderSpliterator(p, cs));
    }

    /**
     * @return a new {@code LongStream} that reads from the given reader.
     */
    static public LongStream longStream(Readable r) {
        return make(new LongReaderSpliterator(r));
    }

    /**
     * @return a new {@code LongStream} that reads from the given file
     * using the default character encoding. The stream should be
     * closed when finished with.
     * @throws IOException on errors opening the file.
     */
    static public LongStream longStream(Path p) throws IOException {
        return make(new LongReaderSpliterator(p));
    }

    /**
     * @return a new {@code LongStream} that reads from the given file
     * using the given character encoding. The stream should be
     * closed when finished with.
     */
    static public LongStream longStream(Path p, Charset cs) throws IOException {
        return make(new LongReaderSpliterator(p, cs));
    }

    /**
     * @return a new {@code DoubleStream} that reads from the given reader.
     */
    static public DoubleStream doubleStream(Readable r) {
        return make(new DoubleReaderSpliterator(r));
    }

    /**
     * @return a new {@code DoubleStream} that reads from the given file
     * using the default character encoding. The stream should be
     * closed when finished with.
     * @throws IOException on errors opening the file.
     */
    static public DoubleStream doubleStream(Path p) throws IOException {
        return make(new DoubleReaderSpliterator(p));
    }

    /**
     * @return a new {@code DoubleStream} that reads from the given file
     * using the given character encoding. The stream should be
     * closed when finished with.
     */
    static public DoubleStream doubleStream(Path p, Charset cs) throws IOException {
        return make(new DoubleReaderSpliterator(p, cs));
    }

    /**
     * Create a new {@code IntStream} of codepoints read from the given {@code Reader},
     * which ideally should be a {@code BufferedReader}. Nothing else should read from
     * it while this stream is active. Closing the stream closes the underlying source.
     * @param r The backing {@code Reader} source.
    */
    public static IntStream codePoints(Reader r) {
        return StreamSupport.intStream(new CPSpliterator(r), false)
            .onClose(makeCloser(r));
    }

    /**
     * Create a new {@code IntStream} of code points read from the given {@code Path}.
     * The stream should be closed when done with it.
     * @param path {@code Path} of the file to read using the default character encoding.
     * @throws IOException on error opening the file.
     */
    public static IntStream codePoints(Path path) throws IOException {
        return codePoints(path, Charset.defaultCharset());
    }

    /**
     * Create a new {@code IntStream} of code points read from the given {@code Path}.
     * The stream should be closed when done with it.
     * @param path {@code Path} of the file to read using the given character encoding.
     * @param cs The character encoding of the file.
     * @throws IOException on error opening the file.
     */
    public static IntStream codePoints(Path path, Charset cs) throws IOException {
        var br = Files.newBufferedReader(path, cs);
        return StreamSupport.intStream(new CPSpliterator(br), false)
            .onClose(makeCloser(br));
    }
}
