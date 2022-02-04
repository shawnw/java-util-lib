package org.raevnos.util.stream;

import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.raevnos.util.iterator.PermutationSpliterator;

/**
 * Create streams of all permutations of a collection.  The lists the
 * stream contains are immutable, and the backing collection should
 * not be modified while the stream is being used.
 */
public class PermutationStream {
    private PermutationStream() {}

    /*
     * Return a stream of all permutations of the elements of a collection.
     * @param elems The elements to permute
     * @return A stream of permutations.
     */
    public static <T> Stream<List<T>> of(Collection<T> elems) {
        return
            StreamSupport.stream(new PermutationSpliterator<T>(elems),
                                 false);
    }

    /*
     * Return a stream of all permutations of the elements of an array
     * @param elems The elements to permute
     * @return A stream of permutations.
     */
    public static <T> Stream<List<T>> of(T[] elems) {
        Objects.requireNonNull(elems);
        return
            StreamSupport.stream(new PermutationSpliterator<T>(Arrays.asList(elems)),
                                 false);
    }

}
