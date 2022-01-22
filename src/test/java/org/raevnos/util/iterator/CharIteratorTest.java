package org.raevnos.util.iterator;

import java.util.Iterator;
import java.util.Spliterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/** Test basic iterator and spliterator CharIterator functionality */
public class CharIteratorTest {
    private static void testNext(Iterator<Character> c, Character expected) {
        assertTrue("should have more remaining", c.hasNext());
        assertEquals("should be equal to " + expected, c.next(), expected);
    }

    @Test
    public void testIterator_Next() {
        Iterator<Character> c = new CharIterator("test");
        testNext(c, 't');
        testNext(c, 'e');
        testNext(c, 's');
        testNext(c, 't');
        assertFalse("should not have more remaining", c.hasNext());
    }

    @Test
    public void testIterator_forEach() {
        Iterator<Character> c = new CharIterator("txxxx");
        testNext(c, 't');
        c.forEachRemaining(ch -> assertEquals("should be x", ch, Character.valueOf('x')));
        assertFalse("should not have more remaining", c.hasNext());
    }

    private static void testNextSplit(Spliterator<Character> s, Character expected) {
        boolean res = s.tryAdvance(c -> assertEquals("should be equal to " + expected,
                                                     c, expected));
        assertTrue("should have advanced", res);
    }

    @Test
    public void testSpliterator_Iter() {
        String test = "test";
        Spliterator<Character> s = new CharIterator(test);
        assertEquals("wrong size", s.estimateSize(), test.length());
        testNextSplit(s, 't');
        testNextSplit(s, 'e');
        testNextSplit(s, 's');
        testNextSplit(s, 't');
        assertFalse("should not have more remaining",
                    s.tryAdvance(c -> assertNotNull("should not be called", null)));
    }


    @Test
    public void testSpliterator_Split() {
        String test = "testword";
        Spliterator<Character> s = new CharIterator(test);
        assertEquals("wrong size", s.estimateSize(), test.length());
        Spliterator<Character> s2 = s.trySplit();
        assertNotNull("trySplit should work", s2);
        assertEquals("wrong size after split (orig)", s.estimateSize(),
                     test.length() / 2);
        assertEquals("wrong size after split (new)", s2.estimateSize(),
                     test.length() / 2);
        testNextSplit(s, 'w');
        testNextSplit(s, 'o');
        testNextSplit(s, 'r');
        testNextSplit(s, 'd');
        assertFalse("should not have more remaining",
                    s.tryAdvance(c -> assertNotNull("should not be called", null)));
        testNextSplit(s2, 't');
        testNextSplit(s2, 'e');
        testNextSplit(s2, 's');
        testNextSplit(s2, 't');
        assertFalse("should not have more remaining",
                    s2.tryAdvance(c -> assertNotNull("should not be called", null)));

    }

    @Test
    public void testSpliterator_SplitSP() {
        String test = "tes\uD801\uDC37ord";
        Spliterator<Character> s = new CharIterator(test);
        assertEquals("wrong size", s.estimateSize(), test.length());
        Spliterator<Character> s2 = s.trySplit();
        assertNotNull("trySplit should work", s2);
        assertEquals("wrong size after split mid surrogate pair (orig)",
                     s.estimateSize(), 5);
        assertEquals("wrong size after split mid surrogate pair (new)",
                     s2.estimateSize(), 3);
    }
}
