package org.raevnos.util.stream;

import java.util.List;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.raevnos.util.iterator.AllCombosSpliterator;
import org.raevnos.util.iterator.ComboSpliterator;

/**
 * Create streams of combinations of elements of collections or
 * arrays. The lists the streams contains are immutable, and the
 * backing collection should not be modified while the stream is in
 * use.
 */
public class ComboStream {
    /**
     * Return a stream of combinations of a list
     * @param elems The elements to get combinations of
     * @param r the number of elements in each combination.
     * @return a stream of r-sized combinations of elements.
     */
    public static <T> Stream<List<T>> of(List<T> elems, int r) {
        return
            StreamSupport.stream(new ComboSpliterator<T>(elems, r),
                                 false);
    }

    /**
     * Return a stream of combinations of a list
     * @param elems The elements to get combinations of
     * @param r the number of elements in each combination.
     * @return a stream of r-sized combinations of elements.
     */
    public static <T> Stream<List<T>> of(Collection<T> elems, int r) {
        return
            StreamSupport.stream(new ComboSpliterator<T>(elems, r),
                                 false);
    }

    /**
     * Return a stream of combinations of an array
     * @param elems The elements to get combinations of
     * @param r the number of elements in each combination.
     * @return a stream of r-sized combinations of elements.
     */
    public static <T> Stream<List<T>> of(T[] elems, int r) {
        return
            StreamSupport.stream(new ComboSpliterator<T>(elems, r),
                                 false);
    }

    /**
     * Return a stream of all combinations of all possible values of
     * {@code r} of a list.
     * @param elems The elements to get combinations of
     * @return a stream of combinations of elements.
     */
    public static <T> Stream<List<T>> ofAllCombinations(List<T> elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 false);
    }

    /**
     * Return a parallel stream of all combinations of all possible values of
     * {@code r} of a list.
     * @param elems The elements to get combinations of
     * @return a parallel stream of combinations of elements.
     */
    public static <T> Stream<List<T>> parallelOfAllCombinations(List<T> elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 true);
    }

    /**
     * Return a stream of all combinations of all possible values of
     * {@code r} of a collection.
     * @param elems The elements to get combinations of
     * @return a stream of combinations of elements.
     */
    public static <T> Stream<List<T>> ofAllCombinations(Collection<T> elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 false);
    }

    /**
     * Return a parallel stream of all combinations of all possible values of
     * {@code r} of a collection.
     * @param elems The elements to get combinations of
     * @return a parallel stream of combinations of elements.
     */
    public static <T> Stream<List<T>> parallelOfAllCombinations(Collection<T> elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 true);
    }

    /**
     * Return a stream of all combinations of all possible values of
     * {@code r} of an array.
     * @param elems The elements to get combinations of
     * @return a stream of combinations of elements.
     */
    public static <T> Stream<List<T>> ofAllCombinations(T[] elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 false);
    }

    /**
     * Return a parallel stream of all combinations of all possible values of
     * {@code r} of an array
     * @param elems The elements to get combinations of
     * @return a parallel stream of combinations of elements.
     */
    public static <T> Stream<List<T>> parallelOfAllCombinations(T[] elems) {
        return
            StreamSupport.stream(new AllCombosSpliterator<T>(elems),
                                 true);
    }
}
