package h11.merge;

import h11.unicode.Tutor_CharWithIndexTests;
import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertArrayEquals;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasField;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertDoesNotThrow;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertFalse;
import static h11.utils.Assertions.assertField;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertNotNull;
import static h11.utils.Assertions.assertNull;
import static h11.utils.Assertions.assertSame;
import static h11.utils.Assertions.assertTrue;

/**
 * Tests for class {@link StreamMerger}.
 */
@SuppressWarnings({"JavadocReference", "unchecked"})
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_StreamMergerTests extends AbstractTestClass {

    public static final String FIELD_PREDICATE_IDENTIFIER = "predicate";
    public static final String FIELD_COMPARATOR_IDENTIFIER = "comparator";
    public static final String FIELD_FUNCTION_IDENTIFIER = "function";
    public static final String FIELD_COLLECTOR_IDENTIFIER = "collector";
    public static final String CONSTRUCTOR_SIGNATURE = "StreamMerger()";
    public static final String METHOD_MERGE_SIGNATURE = "merge(%s<%s>[])"
        .formatted(Stream.class.getName(), Integer.class.getName());

    /**
     * Creates a new {@link Tutor_StreamMergerTests} object.
     */
    public Tutor_StreamMergerTests() {
        super(
            "h11.merge.StreamMerger",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.merge." + CONSTRUCTOR_SIGNATURE)),
            Map.of(
                FIELD_PREDICATE_IDENTIFIER, field -> field.getName().equals(FIELD_PREDICATE_IDENTIFIER),
                FIELD_COMPARATOR_IDENTIFIER, field -> field.getName().equals(FIELD_COMPARATOR_IDENTIFIER),
                FIELD_FUNCTION_IDENTIFIER, field -> field.getName().equals(FIELD_FUNCTION_IDENTIFIER),
                FIELD_COLLECTOR_IDENTIFIER, field -> field.getName().equals(FIELD_COLLECTOR_IDENTIFIER)
            ),
            Map.of(METHOD_MERGE_SIGNATURE, predicateFromSignature(METHOD_MERGE_SIGNATURE))
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasModifiers(clazz, Modifier.PUBLIC);

        Field predicate = assertClassHasField(this, FIELD_PREDICATE_IDENTIFIER);
        assertField(predicate,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals("%s<%s>".formatted(Predicate.class.getName(), Integer.class.getName())),
            null);

        Field comparator = assertClassHasField(this, FIELD_COMPARATOR_IDENTIFIER);
        assertField(comparator,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals("%s<%s>".formatted(Comparator.class.getName(), Integer.class.getName())),
            null);

        Field function = assertClassHasField(this, FIELD_FUNCTION_IDENTIFIER);
        assertField(function,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(
                "%s<%s, h11.unicode.CharWithIndex>".formatted(Function.class.getName(), Integer.class.getName())
            ),
            null);

        Field collector = assertClassHasField(this, FIELD_COLLECTOR_IDENTIFIER);
        assertField(collector,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> Pattern.matches(
                "^%s<h11.unicode.CharWithIndex, .+?, h11.unicode.CharWithIndex\\[]>$".formatted(Collector.class.getName()),
                type.getTypeName()
            ),
            null);

        assertConstructor(assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE), Modifier.PUBLIC);

        assertMethod(assertClassHasMethod(this, METHOD_MERGE_SIGNATURE),
            Modifier.PUBLIC,
            type -> type.getTypeName().equals("h11.unicode.CharWithIndex[]"),
            null,
            (Predicate<Type>) null);
    }

    /**
     * Tests for {@link StreamMerger#predicate}.
     */
    @Test
    @DisplayName("2 | Field predicate")
    public void testPredicate() {
        Predicate<Integer> actualPredicate = getFieldValue(getField(FIELD_PREDICATE_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));
        Integer[] integers = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};

        assertFalse(actualPredicate.test(null), "Predicate does not return false for value null");
        for (Integer integer : integers) {
            assertTrue(actualPredicate.test(integer), "Predicate does not return true for valid value");
        }
    }

    /**
     * Tests for {@link StreamMerger#comparator}.
     */
    @Test
    @DisplayName("3 | Field comparator")
    public void testComparator() {
        Comparator<Integer> referenceComparator = Comparator.comparingInt(StreamMergerSolution::digitSum);
        Comparator<Integer> actualComparator = getFieldValue(getField(FIELD_COMPARATOR_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        Arrays
            .stream(new Integer[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {1000000, 20000, 9, 50, 19},
                {98765, 12345, 421337, 999}
            })
            .map(integers -> {
                Integer[][] lists = new Integer[][] {integers.clone(), integers.clone()};
                Arrays.sort(lists[0], referenceComparator);
                Arrays.sort(lists[1], actualComparator);
                return lists;
            })
            .forEach(lists -> assertArrayEquals(lists[0], lists[1], "Comparator did not sort the given array correctly"));
    }

    /**
     * Tests for {@link StreamMerger#function}.
     */
    @Test
    @DisplayName("4 | Field function")
    public void testFunction() {
        Function<Integer, ?> actualFunction = getFieldValue(getField(FIELD_FUNCTION_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        assertNotNull(actualFunction.apply((int) 'A'),
            "Function did not return correct value when invoked with " + (int) 'A');
        assertNotNull(actualFunction.apply((int) Character.MIN_VALUE),
            "Function did not return correct value when invoked with " + (int) Character.MIN_VALUE);
        assertNotNull(actualFunction.apply((int) Character.MAX_VALUE),
            "Function did not return correct value when invoked with " + (int) Character.MAX_VALUE);
        assertNull(actualFunction.apply(-1),
            "Function did not return correct value when invoked with " + -1);
        assertNull(actualFunction.apply(Integer.MIN_VALUE),
            "Function did not return correct value when invoked with " + Integer.MIN_VALUE);
        assertNull(actualFunction.apply(Character.MAX_VALUE + 1),
            "Function did not return correct value when invoked with " + Character.MAX_VALUE + 1);
        assertNull(actualFunction.apply(Integer.MAX_VALUE),
            "Function did not return correct value when invoked with " + Integer.MAX_VALUE);
    }

    /**
     * Tests for {@link Collector#supplier()} and {@link Collector#finisher()} of {@link StreamMerger#collector}.
     */
    @Test
    @DisplayName("5 | Collector supplier and finisher")
    public void testCollectorSupplierFinisher() {
        Collector<Object, Object, Object[]> actualCollector = getFieldValue(getField(FIELD_COLLECTOR_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));
        Supplier<Object> containerSupplier = assertDoesNotThrow(actualCollector::supplier,
            "An exception occurred while invoking collector.supplier()");
        Function<Object, Object[]> finisherFunction = assertDoesNotThrow(actualCollector::finisher,
            "An exception occurred while invoking collector.finisher()");

        Object container = assertDoesNotThrow(containerSupplier::get,
            "An exception occurred while invoking the functional method of the supplier returned by collector.supplier()");
        Object[] finisherResult = assertDoesNotThrow(() -> finisherFunction.apply(container),
            "An exception occurred while invoking the finisher function with an empty container");

        assertEquals(0, finisherResult.length,
            "Length of the array returned by the finisher function differs from expected length");
    }

    /**
     * Tests for {@link Collector#accumulator()} of {@link StreamMerger#collector}.
     */
    @Test
    @DisplayName("6 | Collector accumulator")
    public void testAccumulator() {
        Collector<Object, Object, Object[]> actualCollector = getFieldValue(getField(FIELD_COLLECTOR_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));
        Supplier<Object> containerSupplier = assertDoesNotThrow(actualCollector::supplier,
            "An exception occurred while invoking collector.supplier()");
        BiConsumer<Object, Object> collectorAccumulator = assertDoesNotThrow(actualCollector::accumulator,
            "An exception occurred while invoking collector.accumulator()");
        Function<Object, Object[]> finisherFunction = assertDoesNotThrow(actualCollector::finisher,
            "An exception occurred while invoking collector.finisher()");

        Object charWithIndexInstance = newCharWithIndexInstance();
        Object container = assertDoesNotThrow(containerSupplier::get,
            "An exception occurred while invoking the functional method of the supplier returned by collector.supplier()");
        collectorAccumulator.accept(container, charWithIndexInstance);
        Object[] finisherResult = assertDoesNotThrow(() -> finisherFunction.apply(container),
            "An exception occurred while invoking the finisher function with an empty container");

        assertEquals(1, finisherResult.length,
            "Length of the array returned by the finisher function differs from expected length");
        assertSame(charWithIndexInstance, finisherResult[0],
            "Element in array returned by the finisher function is not the same object that was passed to the accumulator");
    }

    /**
     * Tests for {@link Collector#combiner()} of {@link StreamMerger#collector}.
     */
    @Test
    @DisplayName("7 | Collector combiner")
    public void testCombiner() {
        Collector<Object, Object, Object[]> actualCollector = getFieldValue(getField(FIELD_COLLECTOR_IDENTIFIER),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));
        Supplier<Object> containerSupplier = assertDoesNotThrow(actualCollector::supplier,
            "An exception occurred while invoking collector.supplier()");
        BiConsumer<Object, Object> collectorAccumulator = assertDoesNotThrow(actualCollector::accumulator,
            "An exception occurred while invoking collector.accumulator()");
        BinaryOperator<Object> collectorCombiner = assertDoesNotThrow(actualCollector::combiner,
            "An exception occurred while invoking collector.combiner()");
        Function<Object, Object[]> finisherFunction = assertDoesNotThrow(actualCollector::finisher,
            "An exception occurred while invoking collector.finisher()");

        Object charWithIndexInstance1 = newCharWithIndexInstance();
        Object charWithIndexInstance2 = newCharWithIndexInstance();
        Object container1 = assertDoesNotThrow(containerSupplier::get,
            "An exception occurred while invoking the functional method of the supplier returned by collector.supplier()");
        Object container2 = assertDoesNotThrow(containerSupplier::get,
            "An exception occurred while invoking the functional method of the supplier returned by collector.supplier()");
        collectorAccumulator.accept(container1, charWithIndexInstance1);
        collectorAccumulator.accept(container2, charWithIndexInstance2);
        Object combinedContainer = assertDoesNotThrow(() -> collectorCombiner.apply(container1, container2),
            "An exception occurred while combining two container with the collector's combining function");
        Object[] finisherResult = assertDoesNotThrow(() -> finisherFunction.apply(combinedContainer),
            "An exception occurred while invoking the finisher function with an empty container");

        assertEquals(2, finisherResult.length,
            "Length of the array returned by the finisher function differs from expected length");
        assertSame(charWithIndexInstance1, finisherResult[0],
            "Element in array returned by the finisher function is not the same object that was passed to the accumulator");
        assertSame(charWithIndexInstance2, finisherResult[1],
            "Element in array returned by the finisher function is not the same object that was passed to the accumulator");
    }

    /**
     * Tests for {@link StreamMerger#merge(Stream[])}.
     */
    @Test
    @DisplayName("8 | merge(java.util.stream.Stream[])")
    public void testMerge() {
        Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE));
        Class<?> charWithIndexClass = getCharWithIndexClass();
        Integer[][] streamElements = new Integer[][] {
            {0, 1, 2, 3, 4, 5},
            {10, 100, 1000, 10000},
            {1, 2, 4, null, null, 32},
            {0, null, 1, (int) Character.MAX_VALUE}
        };

        Object[] referenceResultArray = StreamMergerSolution.merge(
            Arrays.stream(streamElements).map(Arrays::stream).toArray(Stream[]::new)
        );
        Object[] actualResultArray = invokeMethod(getMethod(METHOD_MERGE_SIGNATURE), instance,
            (Object) Arrays.stream(streamElements).map(Arrays::stream).toArray(Stream[]::new));

        assertEquals(referenceResultArray.length, actualResultArray.length,
            "Length of returned array differs from expected length");
        for (int i = 0; i < referenceResultArray.length; i++) {
            if (referenceResultArray[i] != null) {
                assertEquals(charWithIndexClass, actualResultArray[i] != null ? actualResultArray[i].getClass() : null,
                    "Class of object at index %d does not match expected one".formatted(i));
            } else {
                assertNull(actualResultArray[i], "Element at index %d should be null".formatted(i));
            }
        }
    }

    private static Object newCharWithIndexInstance() {
        Tutor_CharWithIndexTests charWithIndexTests = new Tutor_CharWithIndexTests();
        return charWithIndexTests.newInstance(
            charWithIndexTests.getConstructor(Tutor_CharWithIndexTests.CONSTRUCTOR_SIGNATURE), 'a', (int) 'a');
    }

    private static Class<?> getCharWithIndexClass() {
        return new Tutor_CharWithIndexTests().clazz;
    }

    /**
     * Compact solution for StreamMerger.
     * Please, CheckStyle, just shut up.
     *
     * @see StreamMerger
     */
    private static class StreamMergerSolution {

        private static final Predicate<Integer> predicate = Objects::nonNull;
        private static final Comparator<Integer> comparator = Comparator.comparing(StreamMergerSolution::digitSum);
        private static final Function<Integer, Object> function = integer -> {
            if (integer >= Character.MIN_VALUE && integer <= Character.MAX_VALUE) {
                return new Object();
            } else {
                return null;
            }
        };
        private static final Collector<Object, List<Object>, Object[]> collector = Collector.of(
            ArrayList::new,
            List::add,
            (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            },
            List::toArray
        );

        private static Object[] merge(Stream<Integer>[] integerStreams) {
            return concat(integerStreams)
                .filter(predicate)
                .sorted(comparator)
                .map(function)
                .collect(collector);
        }

        private static <T> Stream<T> concat(Stream<T>[] streams) {
            return Arrays.stream(streams).flatMap(stream -> stream);
        }

        private static Integer digitSum(Integer integer) {
            return integer
                .toString()
                .chars()
                .filter(i -> i >= '0' && i <= '9')
                .map(i -> i - '0')
                .sum();
        }
    }
}
