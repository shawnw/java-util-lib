package org.raevnos.util;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/** Assorted extra Comparator generators */
public class Comparators {
    private Comparators() {}

    /**
     * Returns a new Comparator that sorts Optionals with empty ones first, and others
     * according to a given Comparator.
     * @param cmp the Comparator to sort the contents of Optionals by.
     * @return a new Comparator that sorts Optionals.
     * @throws NullPointerException if passed a null object.
     */
    public static <T>
        Comparator<Optional<? extends T>> emptyFirst(Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return new Comparator<Optional<? extends T>>() {
            @Override
            public int compare(Optional<? extends T> a, Optional<? extends T> b) {
                if (a.isEmpty()) {
                    return b.isEmpty() ? 0 : -1;
                } else if (b.isEmpty()) {
                    return 1;
                } else {
                    return cmp.compare(a.orElseThrow(), b.orElseThrow());
                }
            }
        };
    }


    /**
     * Returns a new Comparator that sorts Optionals with empty ones last, and others
     * according to a given Comparator.
     * @param cmp the Comparator to sort the contents of Optionals by.
     * @return a new Comparator that sorts Optionals.
     * @throws NullPointerException if passed a null object.
     */
    public static <T>
        Comparator<Optional<? extends T>> emptyLast(Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return new Comparator<Optional<? extends T>>() {
            @Override
            public int compare(Optional<? extends T> a, Optional<? extends T> b) {
                if (a.isEmpty()) {
                    return b.isEmpty() ? 0 : 1;
                } else if (b.isEmpty()) {
                    return -1;
                } else {
                    return cmp.compare(a.orElseThrow(), b.orElseThrow());
                }
            }
        };
    }
}
