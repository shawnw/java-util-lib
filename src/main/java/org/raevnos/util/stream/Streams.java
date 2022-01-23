package org.raevnos.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.*;

import org.raevnos.util.iterator.StrideAdaptorSpliterator;

/**
 * Extra stream operations.
 */

public class Streams {
    /**
     * Build a stream that returns every Nth element of the original
     * starting with the first, in encounter order.
     *
     * This is a stateful intermediate operation.
     *
     * {@code Streams.stride(original, 2)} returns a stream of every other element,
     * {@code Streams.stride(original, 3)} every third, and so on.
     *
     * @param s the stream to adapt
     * @param n the size of the strde to take.
     * @return a new stream
     * @throws NullPointerException if passed a null stream.
     * @throws IndexOutOfBoundsException if {@code n <= 0}
     */
    static public <T> Stream<T> stride(Stream<T> s, int n) {
        Objects.requireNonNull(s);
        if (n <= 0) {
            throw new IndexOutOfBoundsException("stride must be greater than 0");
        }
        return StreamSupport.stream(new StrideAdaptorSpliterator<T>(s.spliterator(), n),
                                    false);
    }

    /**
     * Build a stream that returns every Nth element of the original
     * starting with the first, in encounter order.
     *
     * This is a stateful intermediate operation.
     *
     * @param s the stream to adapt
     * @param n the size of the strde to take.
     * @return a new stream
     * @throws NullPointerException if passed a null stream.
     * @throws IndexOutOfBoundsException if {@code n <= 0}
     */
    static public DoubleStream strideDouble(DoubleStream s, int n) {
        Objects.requireNonNull(s);
        if (n <= 0) {
            throw new IndexOutOfBoundsException("stride must be greater than 0");
        }
        return StreamSupport.doubleStream(new StrideAdaptorSpliterator.OfDouble(s.spliterator(), n),
                                          false);
    }

        /**
     * Build a stream that returns every Nth element of the original
     * starting with the first, in encounter order.
     *
     * This is a stateful intermediate operation.
     *
     * @param s the stream to adapt
     * @param n the size of the strde to take.
     * @return a new stream
     * @throws NullPointerException if passed a null stream.
     * @throws IndexOutOfBoundsException if {@code n <= 0}
     */
    static public IntStream strideInt(IntStream s, int n) {
        Objects.requireNonNull(s);
        if (n <= 0) {
            throw new IndexOutOfBoundsException("stride must be greater than 0");
        }
        return StreamSupport.intStream(new StrideAdaptorSpliterator.OfInt(s.spliterator(), n),
                                       false);
    }

        /**
     * Build a stream that returns every Nth element of the original
     * starting with the first, in encounter order.
     *
     * This is a stateful intermediate operation.
     *     *
     * @param s the stream to adapt
     * @param n the size of the strde to take.
     * @return a new stream
     * @throws NullPointerException if passed a null stream.
     * @throws IndexOutOfBoundsException if {@code n <= 0}
     */
    static public LongStream strideLong(LongStream s, int n) {
        Objects.requireNonNull(s);
        if (n <= 0) {
            throw new IndexOutOfBoundsException("stride must be greater than 0");
        }
        return StreamSupport.longStream(new StrideAdaptorSpliterator.OfLong(s.spliterator(), n),
                                        false);
    }


}
