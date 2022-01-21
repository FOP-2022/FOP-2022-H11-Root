package h11;

import h11.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignment11 {

//    private static Comparator<Integer> comparator = Comparator.naturalOrder();
    private static Comparator<Integer> comparator = (a, b) -> a < b ? -1 : a > b ? 1 : 0; // alt. a - b (possible overflow)

    @Test
    public void testSuppliers() {
        // h11.ArraySupplier test
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

        // h11.CollectionSupplier test
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

        // h11.CyclicRangeSupplier test
        CyclicRangeSupplier cyclicRangeSupplier = new CyclicRangeSupplier(0, 5);

        for (int i = 0; i < 20; i++)
            assertEquals(i % (5 + 1), cyclicRangeSupplier.get());
    }

    @Test
    public void testBothClassesCharFromUnicode() {
        Random random = new Random();
        CharFromUnicode charFromUnicode = new CharFromUnicode();
        CharFromUnicodeCasesExchanged charFromUnicodeCasesExchanged = new CharFromUnicodeCasesExchanged();

        for (int i = 0; i < 5; i++) {
            int randomSmallChar = random.nextInt('a', 'z' + 1);
            int randomCapitalChar = random.nextInt('A', 'Z' + 1);
            int randomOutOfRangeInt = 0;
            int randomNegativeOutOfRangeInt = random.nextInt(Integer.MIN_VALUE, 0);
            int randomPositiveOutOfRangeInt = random.nextInt(Character.MAX_CODE_POINT + 1, Integer.MAX_VALUE);

            assertEquals((char) randomSmallChar, charFromUnicode.apply(randomSmallChar));
            assertEquals((char) randomCapitalChar, charFromUnicode.apply(randomCapitalChar));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomNegativeOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomPositiveOutOfRangeInt));

            assertEquals(Character.toUpperCase((char) randomSmallChar), charFromUnicodeCasesExchanged.apply(randomSmallChar));
            assertEquals((char) randomCapitalChar, charFromUnicodeCasesExchanged.apply(randomCapitalChar));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomNegativeOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomPositiveOutOfRangeInt));
        }
    }

    private static Integer[] buildIntegerArray(int length, int start, int offset) {
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

    private static LinkedList<Integer> buildIntegerList(int length, int min, int max) {
        Random random = new Random();
        LinkedList<Integer> linkedList = Stream
            .generate(() -> random.nextInt(min, max + 1))
            .limit(length)
            .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

        Collections.sort(linkedList, comparator);

        return linkedList;

//        return Stream
//            .generate(() -> ThreadLocalRandom.current().nextInt(min, max + 1))
//            .limit(length)
//            .sorted(comparator)
//            .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    private static void assertBetween(int value, int lowerBound, int upperBound) {
        assertTrue(value >= lowerBound);
        assertTrue(value <= upperBound);
    }
}
