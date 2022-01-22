package org.raevnos.util.iterator;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Collections;
import java.util.function.Consumer;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Provides {@code Spliterator} and {@code Iterable} interfaces to
 * permutations of the elements of a collection. Use {@code
 * PermutationStream} methods for creating streams of permutations.
 *
 * The backing collection should not be modified while the
 * stream/spliterator/etc. is being used.
 */

public class PermutationSpliterator<T>
    implements Spliterator<List<T>>,
               Iterable<List<T>> {
    private PermutationIterator<T> it;
    private int flags;
    private long nPermutations;

    /**
     * Create a new spliterator to iterate over the permutations of the collection.
     * @param elems The elements to permute.
     * @throws NullPointerException if passed a null object.
     */
    public PermutationSpliterator(Collection<T> elems) {
        this.it = new PermutationIterator<T>(elems);
        this.flags = NONNULL;
        try {
            this.nPermutations = CombinatoricsUtils.factorial(elems.size());
            this.flags |= SIZED;
        } catch (ArithmeticException e) {
            this.nPermutations = Long.MAX_VALUE;
        }
    }

    @Override
    public int characteristics() { return flags; }

    @Override
    public long estimateSize() { return nPermutations; }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> f) {
        if (it.hasNext()) {
            f.accept(Collections.unmodifiableList(it.next()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super List<T>> f) {
        while (it.hasNext()) {
            f.accept(Collections.unmodifiableList(it.next()));
        }
    }

    @Override
    public Spliterator<List<T>> trySplit() { return null; }

    @Override
    public Iterator<List<T>> iterator() { return it; }

    @Override
    public Spliterator<List<T>> spliterator() { return this; }
}
