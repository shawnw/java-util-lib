package org.raevnos.util.stream;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.DoubleStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/** Basic tests of streams returned by Readers methods */
public class ReadersTest {
    @Test
    public void testIntReader() {
        String test = "1 2 3 4";
        int[] testArr = new int[] {1, 2, 3, 4};
        try (IntStream is = Readers.intStream(new StringReader(test))) {
            assertArrayEquals("Different integers", testArr, is.toArray());
        }
    }

    @Test
    public void testLongReader() {
        String test = "1 2 3 4";
        long[] testArr = new long[] {1, 2, 3, 4};
        try (LongStream is = Readers.longStream(new StringReader(test))) {
            assertArrayEquals("Different longs", testArr, is.toArray());
        }
    }

    @Test
    public void testDoubleReader() {
        String test = "1.1 2.2 3.3 4.4";
        double[] testArr = new double[] {1.1, 2.2, 3.3, 4.4};
        try (DoubleStream is = Readers.doubleStream(new StringReader(test))) {
            assertArrayEquals(testArr, is.toArray(), 0.01);
        }
    }

    @Test
    public void testCodePoints() {
        String test = "tes\uD801\uDC37ord";
        try (IntStream cpStream = Readers.codePoints(new StringReader(test))) {
            int[] cps = cpStream.toArray();
            assertArrayEquals("different codepoints",
                              test.codePoints().toArray(), cps);
            assertTrue("Should be 7 codepoints", cps.length == 7);
        }
    }
}
