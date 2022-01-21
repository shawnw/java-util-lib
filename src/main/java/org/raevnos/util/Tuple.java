import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A pair of values that has a natural order and can be serialized.
 */

public record Tuple<A extends Object & Comparable<? super A>,
                              B extends Object & Comparable<? super B>>
    (A first, B second) implements Comparable<Tuple<A, B>>, Serializable {

    /**
     * @return a {@code Comparator} to compare tuples in their
     * natural ordering.
     */
    public Comparator<Tuple<A, B>>
        comparator() {
        return Comparator.comparing(Tuple<A,B>::first)
            .thenComparing(Tuple<A,B>::second);
    }

    /**
     * Compare based on first values; if they compare the same, compare based
     * on the second values of each tuple.
     * @param other The tuple to compare this one to.
     * @return a number less than, equal to, or greater than 0
     * depending on how the tuples compare.
     */
    @Override public int compareTo(Tuple<A, B> other) {
        Objects.requireNonNull(other);
        int cmp = first.compareTo(other.first);
        if (cmp == 0) {
            return second.compareTo(other.second);
        } else {
            return cmp;
        }
    }
}
