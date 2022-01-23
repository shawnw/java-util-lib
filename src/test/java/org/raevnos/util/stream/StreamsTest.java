package org.raevnos.util.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class StreamsTest {
    @Test
    public void testStrideNull() {
        try {
            Stream<Stream> s = Streams.stride(null, 2);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e);
        }
    }

    @Test
    public void testStrideNeg() {
        try {
            Stream s = Streams.stride(Collections.emptyList().stream(), -2);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e);
        }
    }

    @Test
    public void testStride2() {
        List<String> test = List.of("a", "b", "c", "d", "e", "f");
        assertArrayEquals("bad stride 2", Streams.stride(test.stream(), 2)
                          .toArray(String[]::new),
                          new String[]{"a", "c", "e"});
    }

    @Test
    public void testStride3() {
        List<String> test = List.of("a", "b", "c", "d", "e", "f", "g");
        assertArrayEquals("bad stride 3", Streams.stride(test.stream(), 3)
                          .toArray(String[]::new),
                          new String[]{"a", "d", "g"});
    }

    @Test
    public void testStrideInt() {
        int[] test = new int[]{1,2,3,4,5,6,7};
        assertArrayEquals("bad stride 2", Streams.strideInt(Arrays.stream(test), 2)
                          .toArray(),
                          new int[]{1,3,5,7});
    }

    @Test
    public void testStrideLong() {
        long[] test = new long[]{1,2,3,4,5,6,7};
        assertArrayEquals("bad stride 2", Streams.strideLong(Arrays.stream(test), 2)
                          .toArray(),
                          new long[]{1,3,5,7});
    }
}
