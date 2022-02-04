package org.raevnos.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ComparatorsTest {
    @Test
    public void emptyFirstTest() {
        ArrayList<Optional<String>> test = new ArrayList<>();
        test.add(Optional.of("c"));
        test.add(Optional.empty());
        test.add(Optional.of("a"));
        test.add(Optional.of("b"));

        test.sort(Comparators.emptyFirst(Comparator.naturalOrder()));
        assertTrue("first element not empty", test.get(0).isEmpty());
        assertTrue("second element not a", test.get(1).orElse("ugh").equals("a"));
        assertTrue("third element not b", test.get(2).orElse("ugh").equals("b"));
        assertTrue("fourth element not c", test.get(3).orElse("ugh").equals("c"));
    }

    @Test
    public void emptyLastTest() {
        ArrayList<Optional<String>> test = new ArrayList<>();
        test.add(Optional.of("c"));
        test.add(Optional.empty());
        test.add(Optional.of("a"));
        test.add(Optional.of("b"));

        test.sort(Comparators.emptyLast(Comparator.reverseOrder()));
        assertTrue("first element not c", test.get(0).orElse("ugh").equals("c"));
        assertTrue("second element not b", test.get(1).orElse("ugh").equals("b"));
        assertTrue("third element not a", test.get(2).orElse("ugh").equals("a"));
        assertTrue("fourth element not empty", test.get(3).isEmpty());
    }
}
