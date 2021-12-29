package org.raevnos.util.stream;

import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.RandomAccess;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Provides {@code Spliterator}, {@code Iterator} and {@code Iterable}
 * interfaces to combinations of {@code r} elements of a
 * collection. Use {@code ComboStream} methods for creating streams of
 * combinations.
 *
 * The backing collection should not be modified while the
 * stream/spliterator/etc. is being used. The lists provided are
 * immutable.
*/
public class ComboSpliterator<T>
    implements Spliterator<List<T>>, Iterator<List<T>>, Iterable<List<T>> {
    private List<T> elems;
    private int r, flags;
    private final int n;
    private Iterator<int[]> iter;
    private long nCr;

    /**
     * Create a spliterator that iterates over combinations of {@code r}
     * elements of the list.
     * @param elems The list of elements to get combinations of.
     * @param r the number of elements of each combination
     * @throws NullPointerException if passed a null list.
     */
    public ComboSpliterator(List<T> elems, int r) {
        Objects.requireNonNull(elems);
        this.flags = NONNULL;
        if (elems instanceof RandomAccess) {
            this.elems = elems;
        } else {
            this.elems = new ArrayList<T>(elems);
            this.flags |= IMMUTABLE;
        }
        this.r = r;
        this.n = this.elems.size();
        this.iter =
            CombinatoricsUtils.combinationsIterator(n, r);
        try {
            nCr = CombinatoricsUtils.binomialCoefficient(n, r);
            flags |= SIZED;
        } catch (ArithmeticException e) {
            nCr = Long.MAX_VALUE;
        }
    }

    /**
     * Create a spliterator that iterates over combinations of {@code r}
     * elements of the collection.
     * @param elems A collection of elements to get combinations of.
     * @param r the number of elements of each combination
     * @throws NullPointerException if passed a null collection.
     */
    public ComboSpliterator(Collection<T> elems, int r) {
        this(new ArrayList<T>(elems), r);
        flags |= IMMUTABLE;
    }

    /**
     * Create a spliterator that iterates over combinations of {@code r}
     * elements of an array.
     * @param elems The array of elements to get combinations of.
     * @param r the number of elements of each combination
     * @throws NullPointerException if passed a null array argument.
     */
    public ComboSpliterator(T[] elems, int r) {
        this(Arrays.stream(elems)
             .collect(Collectors.toCollection(ArrayList<T>::new)),
             r);
        flags |= IMMUTABLE;
    }

    @Override
    public boolean hasNext() { return iter.hasNext(); }

    @Override
    public List<T> next() {
        int[] indexes = iter.next();
        List<T> combo = new ArrayList<T>(r);
        for (int i : indexes) {
            combo.add(elems.get(i));
        }
        return combo;
    }

    @Override
    public int characteristics() { return flags; }

    @Override
    public long estimateSize() { return nCr; }

    @Override
    public void forEachRemaining(Consumer<? super List<T>> f) {
        if (elems.size() != n) {
            throw new ConcurrentModificationException();
        }
        var combo = new ArrayList<T>(r);
        for (int i = 0; i < r; i++) {
            combo.add(null);
        }
        while (iter.hasNext()) {
            int[] indexes = iter.next();
            for (int i = 0; i < r; i++) {
                combo.set(i, elems.get(indexes[i]));
            }
            f.accept(Collections.unmodifiableList(combo));
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> f) {
        if (elems.size() != n) {
            throw new ConcurrentModificationException();
        }
        if (!iter.hasNext()) {
            return false;
        }
        int[] indexes = iter.next();
        List<T> combo = new ArrayList<T>(r);
        for (int i : indexes) {
            combo.add(elems.get(i));
        }
        f.accept(Collections.unmodifiableList(combo));
        return true;
    }

    @Override
    public Spliterator<List<T>> trySplit() { return null; }

    @Override
    public Iterator<List<T>> iterator() { return this; }

    @Override
    public Spliterator<List<T>> spliterator() { return this; }
}
