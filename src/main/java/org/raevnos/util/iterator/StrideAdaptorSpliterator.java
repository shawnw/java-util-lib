package org.raevnos.util.iterator;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * Adapt an existing spliterator into one that returns every nth element.
 */
public class StrideAdaptorSpliterator<T> implements Spliterator<T> {
    private final Spliterator<T> source;
    private final int strideLen;

    /**
     * Construct a new adaptor.
     * @param s the spliterator to adapt
     * @param n the size of the strde to take.
     * @throws NullPointerException if passed a null spliterator
     * @throws IndexOutOfBoundsException if {@code n <= 0}
     */
    public StrideAdaptorSpliterator(Spliterator<T> s, int n) {
        Objects.requireNonNull(s);
        if (n <= 0) {
            throw new IndexOutOfBoundsException("stride must be greater than 0");
        }
        this.source = s;
        this.strideLen = n;
    }

    @Override
    public int characteristics() {
        return source.characteristics() & (DISTINCT | IMMUTABLE | NONNULL | ORDERED | SORTED);
    }

    @Override
    public long estimateSize() {
        long size = source.estimateSize();
        if (size == Long.MAX_VALUE) {
            return size;
        } else {
            return size / strideLen;
        }
    }

    static private <T> void discard(T unused) {}

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (source.tryAdvance(action)) {
            for (int i = 1; i < strideLen; i++) {
                if (!source.tryAdvance(StrideAdaptorSpliterator::discard)) {
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    static public class OfDouble implements Spliterator.OfDouble {
        static private final DoubleConsumer discard = x -> {};
        private final Spliterator.OfDouble source;
        private final int strideLen;

        /**
         * Construct a new adaptor.
         * @param s the spliterator to adapt
         * @param n the size of the strde to take.
         * @throws NullPointerException if passed a null spliterator
         * @throws IndexOutOfBoundsException if {@code n <= 0}
         */
        public OfDouble(Spliterator.OfDouble s, int n) {
            Objects.requireNonNull(s);
            if (n <= 0) {
                throw new IndexOutOfBoundsException("stride must be greater than 0");
            }
            this.source = s;
            this.strideLen = n;
        }

        @Override
        public int characteristics() {
            return source.characteristics()
                & (DISTINCT | IMMUTABLE | NONNULL | ORDERED | SORTED);
        }

        @Override
        public long estimateSize() {
            long size = source.estimateSize();
            if (size == Long.MAX_VALUE) {
                return size;
            } else {
                return size / strideLen;
            }
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if (source.tryAdvance(action)) {
                for (int i = 1; i < strideLen; i++) {
                    if (!source.tryAdvance(discard)) {
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Spliterator.OfDouble trySplit() {
            return null;
        }
    }

    static public class OfInt implements Spliterator.OfInt {
        static private final IntConsumer discard = x -> {};
        private final Spliterator.OfInt source;
        private final int strideLen;

        /**
         * Construct a new adaptor.
         * @param s the spliterator to adapt
         * @param n the size of the strde to take.
         * @throws NullPointerException if passed a null spliterator
         * @throws IndexOutOfBoundsException if {@code n <= 0}
         */
        public OfInt(Spliterator.OfInt s, int n) {
            Objects.requireNonNull(s);
            if (n <= 0) {
                throw new IndexOutOfBoundsException("stride must be greater than 0");
            }
            this.source = s;
            this.strideLen = n;
        }

        @Override
        public int characteristics() {
            return source.characteristics()
                & (DISTINCT | IMMUTABLE | NONNULL | ORDERED | SORTED);
        }

        @Override
        public long estimateSize() {
            long size = source.estimateSize();
            if (size == Long.MAX_VALUE) {
                return size;
            } else {
                return size / strideLen;
            }
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (source.tryAdvance(action)) {
                for (int i = 1; i < strideLen; i++) {
                    if (!source.tryAdvance(discard)) {
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Spliterator.OfInt trySplit() {
            return null;
        }
    }

    static public class OfLong implements Spliterator.OfLong {
        static private final LongConsumer discard = x -> {};
        private final Spliterator.OfLong source;
        private final int strideLen;

        /**
         * Construct a new adaptor.
         * @param s the spliterator to adapt
         * @param n the size of the strde to take.
         * @throws NullPointerException if passed a null spliterator
         * @throws IndexOutOfBoundsException if {@code n <= 0}
         */
        public OfLong(Spliterator.OfLong s, int n) {
            Objects.requireNonNull(s);
            if (n <= 0) {
                throw new IndexOutOfBoundsException("stride must be greater than 0");
            }
            this.source = s;
            this.strideLen = n;
        }

        @Override
        public int characteristics() {
            return source.characteristics()
                & (DISTINCT | IMMUTABLE | NONNULL | ORDERED | SORTED);
        }

        @Override
        public long estimateSize() {
            long size = source.estimateSize();
            if (size == Long.MAX_VALUE) {
                return size;
            } else {
                return size / strideLen;
            }
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            if (source.tryAdvance(action)) {
                for (int i = 1; i < strideLen; i++) {
                    if (!source.tryAdvance(discard)) {
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Spliterator.OfLong trySplit() {
            return null;
        }
    }
}
