package org.raevnos.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.io.Serializable;

/**
 * A list that enforces a sorted order on elements added to it. Much like a
 * {@code SortedSet}, but allows duplicates and backed by an {@code ArrayList}
 */
public class SortedList<T> extends AbstractList<T>
    implements RandomAccess, Serializable {
    private final Comparator<? super T> comparator;
    private final ArrayList<T> source;

    /** Create a new empty list with natural ordering */
    SortedList() {
        super();
        this.comparator = null;
        this.source = new ArrayList<T>();
    }

    /** Create a new empty list with an initial capacity and natural
     * ordering.
     * @param cap the initial capacity.
     */
    SortedList(int cap) {
        super();
        this.comparator = null;
        this.source = new ArrayList<T>(cap);
    }

    /** Create an empty list with the given ordering
     * @param cmp Comparator to use to determine ordering. If null,
     * uses the natural order.
     */
    SortedList(Comparator<? super T> cmp) {
        super();
        this.comparator = cmp;
        this.source = new ArrayList<T>();
    }

    /** Create a new empty list with an initial capacity and ordering.
     * @param cap the initial capacity
     * @param cmp Comparator to use to determine ordering. If null,
     * uses the natural order.
     */
    SortedList(int cap, Comparator<? super T> cmp) {
        super();
        this.comparator = cmp;
        this.source = new ArrayList<T>(cap);
    }

    /** Create a new list populated by the given collection and natural ordering.
     * @param c The collection to populate the list from.
     * @throws NullPointerException if the collection is null or has a null element
     */
    SortedList(Collection<? extends T> c) {
        super();
        if (Objects.requireNonNull(c).stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        this.comparator = null;
        this.source = new ArrayList<T>(c);
        this.source.sort(null);
    }

    /** Create a new list populated by the given collection and given ordering.
     * @param c The collection to populate the list from.
     * @param cmp Comparator to use to determine ordering. If null,
     * uses the natural ordering.
     * @throws NullPointerException if the collection is null or has a null element
     */
    SortedList(Collection<? extends T> c, Comparator<? super T> cmp) {
        super();
        if (Objects.requireNonNull(c).stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        this.comparator = cmp;
        this.source = new ArrayList<T>(c);
        this.source.sort(this.comparator);
    }

    /** Copy an existing SortedList
     * @param sl the sorted list to copy
     * @throws NullPointerException if given a null argument
     */
    SortedList(SortedList<T> sl) {
        super();
        Objects.requireNonNull(sl);
        this.comparator = sl.comparator;
        this.source = new ArrayList<T>(sl.source);
    }

    /** Insert an element at the given spot.
     * @param index index at which the element is to be inserted
     * @param element element to be inserted
     * @throws NullPointerException if element is null
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalArgumentException if the insertion would result
     * in an unsorted list.
     * @throws ClassCastException if the class of the element prevents
     * it from being added.
     */
    @Override
    public void add(int index, T element) {
        Objects.requireNonNull(element);
        Objects.checkIndex(index, source.size() + 1);
        if (comparator != null) {
            if (index > 0 && comparator.compare(source.get(index - 1), element) > 0) {
                throw new IllegalArgumentException();
            }
            if (index < source.size()
                && comparator.compare(element, source.get(index)) > 0) {
                throw new IllegalArgumentException();
            }
        } else {
            @SuppressWarnings("unchecked")
                Comparable<? super T> elem = (Comparable<? super T>)element;
            if (index > 0 && elem.compareTo(source.get(index - 1)) < 0) {
                throw new IllegalArgumentException();
            }
            if (index < source.size()
                && elem.compareTo(source.get(index)) > 0) {
                throw new IllegalArgumentException();
            }
        }
        source.add(index, element);
        modCount++;
    }

    /** Insert an element at the appropriate spot.
     * @param element the element to insert
     * @return true if the element was added
     * @throws NullPointerException if element is null
     * @throws ClassCastException if the class of the element prevents
     * it from being added.
     */
    @Override
    public boolean add(T element) {
        Objects.requireNonNull(element);
        if (source.isEmpty()) {
            if (source.add(element)) {
                modCount++;
                return true;
            } else {
                return false;
            }
        } else {
            int index = Collections.binarySearch(source, element, comparator);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
            source.add(index, element);
            modCount++;
            return true;
        }
    }

    /** Insert all the elements of a collection.
     * @param c the collection to add
     * @returns true if any elements were added
     * @throws NullPointerException if argument is null or element of
     * the collection is null.
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (Objects.requireNonNull(c).stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        boolean added = source.addAll(c);
        if (added) {
            source.sort(comparator);
            modCount++;
        }
        return added;
    }

    @Override
    public void clear() {
        source.clear();
    }

    /** See if a given object exists in the list. Uses binary search.
     * @param o the object to look for
     * @return true if it's found in the list.
     * @throws NullPointerException if the argument is null.
     * @throws ClassCastException if the element is incompatible with this collection.
     */
    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        @SuppressWarnings("unchecked")
            T other = (T)o;
        return Collections.binarySearch(source, other, comparator) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof SortedList) {
            SortedList other = (SortedList)o;
            return source.equals(other.source)
                && Objects.equals(comparator, other.comparator);
        } else {
            return false;
        }
    }

    /** Get an element from the list
     * @param index the index of the element to get
     * @returns the element
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public T get(int index) {
        return source.get(index);
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    /** Returns the index of the first occurance of the given element
     * in the list, or -1 if it doesn't exist. Uses a binary
     * search.
     * @param o the object to look for
     * @return the index of the object, or -1 if it doesn't exist.
     * @throws NullPointerException if the argument is null
     * @throws ClassCastException if the argument isn't compatible with the class.
     */
    @Override
    public int indexOf(Object o) {
        Objects.requireNonNull(o);
        @SuppressWarnings("unchecked")
            T other = (T)o;
        int idx = Collections.binarySearch(source, other, comparator);
        if (idx >= 0) {
            while ((idx - 1) > 0 && source.get(idx - 1).equals(other)) {
                idx--;
            }
            return idx;
        } else {
            return -1;
        }
    }

    /** Returns the index of the last occurance of the given element
     * in the list, or -1 if it doesn't exist. Uses a binary
     * search.
     * @param o the object to look for
     * @return the index of the object, or -1 if it doesn't exist.
     * @throws NullPointerException if the argument is null
     * @throws ClassCastException if the argument isn't compatible with the class.
     */
    @Override
    public int lastIndexOf(Object o) {
        Objects.requireNonNull(o);
        @SuppressWarnings("unchecked")
            T other = (T)o;
        int idx = Collections.binarySearch(source, other, comparator);
        if (idx >= 0) {
            while ((idx + 1) < source.size() && source.get(idx + 1).equals(other)) {
                idx++;
            }
            return idx;
        } else {
            return -1;
        }
    }

    @Override
    public T remove(int index) {
        Objects.checkIndex(index, source.size());
        modCount++;
        return source.remove(index);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        boolean removed = source.removeIf(filter);
        if (removed) {
            modCount++;
        }
        return removed;
    }

    /** Overwrite an existing element, which must preserve sorted order of the list.
     * @param index the index to replace
     * @param element the new element
     * @return the element previously at the position
     * @throws NullPointerException if element is null
     * @throws IndexOutOfBoundsException if index is out of range
     * @throws IllegalArgumentException if the element will break the
     * sorted order of the list
     * @throws ClassCastException if the argument isn't compatible with the class.
     */
    @Override
    public T set(int index, T element) {
        Objects.requireNonNull(element);
        Objects.checkIndex(index, source.size());
        if (comparator != null) {
            if (index > 0 && comparator.compare(source.get(index - 1), element) > 0) {
                throw new IllegalArgumentException();
            }
            if (index < source.size() - 1
                && comparator.compare(element, source.get(index + 1)) > 0) {
                throw new IllegalArgumentException();
            }
        } else {
            @SuppressWarnings("unchecked")
                Comparable<? super T> elem = (Comparable<? super T>)element;
            if (index > 0 && elem.compareTo(source.get(index - 1)) < 0) {
                throw new IllegalArgumentException();
            }
            if (index < source.size() - 1
                && elem.compareTo(source.get(index + 1)) > 0) {
                throw new IllegalArgumentException();
            }
        }
        return source.set(index, element);
    }

    /** The number of elements in the list
     * @returns the number of elements in the list
     */
    @Override
    public int size() {
        return source.size();
    }

    /** Sort the list. Since the list is already sorted, this
     * operation is meaningless and it just raises an error if it
     * would try to sort the list in a different order than it already
     * is.
     * @param c A comparator or null for natural ordering.
     * @throws UnsupportedOperationException if sorting would result
     * in a different order than the list is already in.
     */
    @Override
    public void sort(Comparator<? super T> c) {
        if (comparator == null && c == null) {
            return;
        } else if (comparator != null && c != null && comparator.equals(c)) {
            return;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /** Add the NONNULL and SORTED characteristics to the underlying
     * arraylist spliterator */
    private class SortedSpliteratorAdaptor implements Spliterator<T> {
        private final Spliterator<T> sp;
        public SortedSpliteratorAdaptor(Spliterator<T> sp) {
            Objects.requireNonNull(sp);
            this.sp = sp;
        }

        @Override
        public int characteristics() {
            return sp.characteristics() | NONNULL | SORTED;
        }

        @Override
        public long estimateSize() {
            return sp.estimateSize();
        }

        @Override
        public boolean tryAdvance(java.util.function.Consumer<? super T> action) {
            return sp.tryAdvance(action);
        }

        @Override
        public void forEachRemaining(java.util.function.Consumer<? super T> action) {
            sp.forEachRemaining(action);
        }

        @Override
        public Spliterator<T> trySplit() {
            Spliterator<T> newSp = sp.trySplit();
            if (newSp != null) {
                return new SortedSpliteratorAdaptor(newSp);
            } else {
                return null;
            }
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return new SortedSpliteratorAdaptor(source.spliterator());
    }

    @Override
    public Object[] toArray() {
        return source.toArray();
    }

    @Override
    public <T2> T2[] toArray(T2[] a) {
        return source.toArray(a);
    }

    @Override
    public String toString() {
        return source.toString();
    }
}
