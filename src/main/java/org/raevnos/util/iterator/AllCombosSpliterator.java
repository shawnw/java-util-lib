package org.raevnos.util.iterator;

import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.RandomAccess;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Implements a {@code Spliterator} that iterates over all possible
 * combinations of a collection, for {@code r} = 1 to its number of
 * elements.
 *
 * Use {@code ComboStream} methods to create streams of all
 * combinations. The backing collection should not be modified while
 * this spliterator is in use. The lists provided by the spliterator
 * are immutable.
 */

public class AllCombosSpliterator<T>
    implements Spliterator<List<T>> {
    private List<T> elems;
    private int r, flags;
    private final int n;
    private Iterator<int[]> iter;
    private long nCr;

    /**
     * Create a new spliterator using the given list.
     */
    public AllCombosSpliterator(List<T> elems) {
        Objects.requireNonNull(elems);
        this.flags = NONNULL;
        if (elems instanceof RandomAccess) {
            this.elems = elems;
        } else {
            this.elems = new ArrayList<T>(elems);
            this.flags |= IMMUTABLE;
        }
        this.r = 0;
        this.n = this.elems.size();
        try {
            nCr = 0;
            for (int k = 1; k < n; k++) {
                nCr =
                    Math.addExact(nCr,
                                  CombinatoricsUtils.binomialCoefficient(n,k));
            }
            flags |= SIZED | SUBSIZED;
        } catch (ArithmeticException e) {
            nCr = Long.MAX_VALUE;
        }
    }

    /**
     * Create a new spliterator using the given collection.
     */
    public AllCombosSpliterator(Collection<T> elems) {
        this(new ArrayList<T>(elems));
        flags |= IMMUTABLE;
    }

    /**
     * Create a new spliterator using the given array.
     */
    public AllCombosSpliterator(T[] elems) {
        this(Arrays.stream(elems)
             .collect(Collectors.toCollection(ArrayList<T>::new)));
        flags |= IMMUTABLE;
    }

    @Override
    public int characteristics() { return flags; }

    @Override
    public long estimateSize() { return nCr; }

    @Override
    public void forEachRemaining(Consumer<? super List<T>> f) {
        var combo = new ArrayList<T>();
        if (iter != null) {
            combo.ensureCapacity(r);
            for (int i = 0; i < r; i++) {
                combo.add(null);
            }
            while (iter.hasNext()) {
                if (elems.size() != n) {
                    throw new ConcurrentModificationException();
                }
                int[] indexes = iter.next();
                for (int i = 0; i < indexes.length; i++) {
                    combo.set(i, elems.get(indexes[i]));
                }
                f.accept(Collections.unmodifiableList(combo));
            }
        }
        while (++r <= elems.size()) {
            iter =
                CombinatoricsUtils.combinationsIterator(n, r);
            for (int i = combo.size(); i < r; i++) {
                combo.add(null);
            }
            while (iter.hasNext()) {
                if (elems.size() != n) {
                    throw new ConcurrentModificationException();
                }
                int[] indexes = iter.next();
                for (int i = 0; i < indexes.length; i++) {
                    combo.set(i, elems.get(indexes[i]));
                }
                f.accept(Collections.unmodifiableList(combo));
            }
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> f) {
        if (elems.size() != n) {
            throw new ConcurrentModificationException();
        }
        if (iter != null && iter.hasNext()) {
            int[] indexes = iter.next();
            List<T> combo = new ArrayList<T>(indexes.length);
            for (int i : indexes) {
                combo.add(elems.get(i));
            }
            f.accept(Collections.unmodifiableList(combo));
            return true;
        }
        if (++r > elems.size()) {
            return false;
        }
        iter =
            CombinatoricsUtils.combinationsIterator(n, r);
        return tryAdvance(f);
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        if (++r > elems.size()) {
            return null;
        }
        return new ComboSpliterator<T>(elems, r);
    }
}
