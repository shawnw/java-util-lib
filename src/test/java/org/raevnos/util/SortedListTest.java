package org.raevnos.util;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class SortedListTest {
    @Test
    public void testAdd() {
        SortedList<String> sl = new SortedList<String>();
        assertTrue("add failed", sl.add("b"));
        assertTrue("add failed", sl.add("a"));
        assertTrue("add failed", sl.add("d"));
        assertTrue("add failed", sl.add("c"));
        assertArrayEquals("mismatched list", sl.toArray(String[]::new),
                          new String[]{"a", "b", "c", "d"});
        try {
            sl.add(4, "e");
            assertArrayEquals("mismatched list", sl.toArray(String[]::new),
                              new String[]{"a", "b", "c", "d", "e"});
        } catch (IndexOutOfBoundsException e) {
            fail("add to end failed");
        }

        try {
            sl.add(3, "f");
            fail("add should have raised an exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testIndexOf() {
        SortedList<String> sl = new SortedList<String>(List.of("d", "b", "c", "b", "a"));

        assertTrue("Wrong index returned 1", sl.indexOf("b") == 1);
        assertTrue("Wrong index returned 2", sl.lastIndexOf("b") == 2);
        assertTrue("Wrong index returned 3", sl.indexOf("d") == 4);
        assertTrue("Wrong index returned 4", sl.lastIndexOf("d") == 4);
        assertTrue("Wrong index returned 5", sl.indexOf("a") == 0);
        assertTrue("Wrong index returned 6", sl.lastIndexOf("a") == 0);
        assertTrue("Wrong index returned 7", sl.indexOf("q") == -1);
        assertTrue("Wrong index returned 8", sl.lastIndexOf("q") == -1);
        assertTrue("contains failed 1", sl.contains("a"));
        assertFalse("contains failed 2", sl.contains("z"));
    }

    @Test
    public void testComparator() {
        SortedList<String> sl = new SortedList<String>(Comparator.reverseOrder());
        assertTrue("add failed", sl.add("a"));
        assertTrue("add failed", sl.add("b"));
        try {
            sl.add(0, "d");
            sl.add(1, "c");
        } catch (IllegalArgumentException e) {
            fail("add index failed");
        }
        assertArrayEquals("wrong order", sl.toArray(String[]::new),
                          new String[] { "d", "c", "b", "a" });
    }
}
