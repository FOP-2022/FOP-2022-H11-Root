package h11.supplier;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ArraySupplier}, {@link CollectionSupplier} and {@link CyclicRangeSupplier}.
 */
public class SupplierTests {

    /**
     * Tests {@link ArraySupplier#get()} with 5 instances.
     * One instance has length 0, the second one has length 1 and the last 3 each have length 100.
     */
    @Test
    public void testArraySupplier() {
        ArraySupplier<Integer> arraySupplier1 = new ArraySupplier<>(buildIntegerArray(0, 0, 0));
        ArraySupplier<Integer> arraySupplier2 = new ArraySupplier<>(buildIntegerArray(1, 0, 0));
        ArraySupplier<Integer> arraySupplier3 = new ArraySupplier<>(buildIntegerArray(100, 0, 0));
        ArraySupplier<Integer> arraySupplier4 = new ArraySupplier<>(buildIntegerArray(100, 0, 10));
        ArraySupplier<Integer> arraySupplier5 = new ArraySupplier<>(buildIntegerArray(100, 0, -10));

        assertNull(arraySupplier1.get());

        assertEquals(0, arraySupplier2.get());
        assertNull(arraySupplier2.get());

        for (int i = 0; i <= 100; i++) {
            if (i != 100) {
                assertEquals(0, arraySupplier3.get());
                assertEquals(i * 10, arraySupplier4.get());
                assertEquals(i * -10, arraySupplier5.get());
            } else {
                assertNull(arraySupplier3.get());
                assertNull(arraySupplier4.get());
                assertNull(arraySupplier5.get());
            }
        }
    }

    /**
     * Tests {@link CollectionSupplier#get()} with 5 instances.
     * One instance has length 0, the second one has length 1 and the last 3 each have length 100.
     */
    @Test
    public void testCollectionSupplier() {
        CollectionSupplier<Integer> collectionSupplier1 = new CollectionSupplier<>(buildIntegerList(0, 0, 0));
        CollectionSupplier<Integer> collectionSupplier2 = new CollectionSupplier<>(buildIntegerList(1, 0, 0));
        CollectionSupplier<Integer> collectionSupplier3 = new CollectionSupplier<>(buildIntegerList(100, 0, 0));
        CollectionSupplier<Integer> collectionSupplier4 = new CollectionSupplier<>(buildIntegerList(100, 0, 11));
        CollectionSupplier<Integer> collectionSupplier5 = new CollectionSupplier<>(buildIntegerList(100, 0, 1000));

        assertNull(collectionSupplier1.get());

        assertEquals(0, collectionSupplier2.get());
        assertNull(collectionSupplier2.get());

        for (int i = 0; i <= 100; i++) {
            if (i != 100) {
                assertBetween(collectionSupplier3.get(), 0, 0);
                assertBetween(collectionSupplier4.get(), 0, 11);
                assertBetween(collectionSupplier5.get(), 0, 1000);
                // TODO: test for order
            } else {
                assertNull(collectionSupplier3.get());
                assertNull(collectionSupplier4.get());
                assertNull(collectionSupplier5.get());
            }
        }
    }

    /**
     * Tests {@link CyclicRangeSupplier#get()}.
     * The cyclic range is tested at least 3 times.
     */
    @Test
    public void testCyclicRangeSupplier() {
        CyclicRangeSupplier cyclicRangeSupplier = new CyclicRangeSupplier(0, 5);

        for (int i = 0; i < 20; i++) {
            assertEquals(i % (5 + 1), cyclicRangeSupplier.get());
        }
    }

    /**
     * Builds an integer array with length {@code length}.
     * The value for each element is calculated with the formula {@code start + i * offset}.
     *
     * @param length the length of the array
     * @param start  the start value
     * @param offset the offset that will be added with each iteration
     * @return an array of {@link Integer}
     */
    private Integer[] buildIntegerArray(int length, int start, int offset) {
        return Stream
            .generate(new Supplier<Integer>() {
                private int i = 0;

                @Override
                public Integer get() {
                    return start + i++ * offset;
                }
            })
            .limit(length)
            .toArray(Integer[]::new);
    }

    /**
     * Builds an integer list with size {@code length}.
     * Each element is a randomly generated integer in the closed range defined
     * by {@code min} and {@code max}.
     *
     * @param length the size of the list
     * @param min    the lower bound for the random integer interval
     * @param max    the upper bound for the random integer interval
     * @return a sorted list with random integers between {@code min} and {@code max} (inclusive)
     */
    private List<Integer> buildIntegerList(int length, int min, int max) {
        return Stream
            .generate(() -> ThreadLocalRandom.current().nextInt(min, max + 1))
            .limit(length)
            .sorted((a, b) -> a < b ? -1 : a > b ? 1 : 0)
            .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    /**
     * Asserts that a value is not less than {@code lowerBound} and
     * not greater than {@code upperBound}.
     *
     * @param value      the value
     * @param lowerBound the minimum value {@code value} may have
     * @param upperBound the maximum value {@code value} may have
     */
    private static void assertBetween(int value, int lowerBound, int upperBound) {
        assertTrue(value >= lowerBound);
        assertTrue(value <= upperBound);
    }
}
