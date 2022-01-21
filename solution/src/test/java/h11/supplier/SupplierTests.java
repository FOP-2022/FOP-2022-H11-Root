package h11.supplier;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTests {

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

    @Test
    public void testCyclicRangeSupplier() {
        CyclicRangeSupplier cyclicRangeSupplier = new CyclicRangeSupplier(0, 5);

        for (int i = 0; i < 20; i++)
            assertEquals(i % (5 + 1), cyclicRangeSupplier.get());
    }

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

    private LinkedList<Integer> buildIntegerList(int length, int min, int max) {
        return Stream
            .generate(() -> ThreadLocalRandom.current().nextInt(min, max + 1))
            .limit(length)
            .sorted((a, b) -> a < b ? -1 : a > b ? 1 : 0)
            .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    private static void assertBetween(int value, int lowerBound, int upperBound) {
        assertTrue(value >= lowerBound);
        assertTrue(value <= upperBound);
    }
}
