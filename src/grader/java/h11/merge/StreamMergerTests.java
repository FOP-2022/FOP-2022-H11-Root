package h11.merge;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasField;
import static h11.utils.Assertions.assertClassHasMethod;
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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link StreamMerger}.
 */
@SuppressWarnings({"JavadocReference", "unchecked"})
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class StreamMergerTests extends AbstractTestClass implements PreInvocationCheck {

    private static Class<?> streamMergerClass = null;
    private static Field predicate = null;
    private static Field comparator = null;
    private static Field function = null;
    private static Field collector = null;
    private static Constructor<?> streamMergerConstructor = null;
    private static Method merge = null;

    /**
     * Creates a new {@link StreamMergerTests} object.
     */
    public StreamMergerTests() {
        super("h11.merge.StreamMerger");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        streamMergerClass = assertClassExists(className);
        assertClassHasExactModifiers(streamMergerClass, Modifier.PUBLIC);

        predicate = assertClassHasField(streamMergerClass, "predicate");
        assertField(predicate,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals("%s<%s>".formatted(Predicate.class.getName(), Integer.class.getName())),
            null);

        comparator = assertClassHasField(streamMergerClass, "comparator");
        assertField(comparator,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals("%s<%s>".formatted(Comparator.class.getName(), Integer.class.getName())),
            null);

        function = assertClassHasField(streamMergerClass, "function");
        assertField(function,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(
                "%s<%s, h11.unicode.CharWithIndex>".formatted(Function.class.getName(), Integer.class.getName())
            ),
            null);

        collector = assertClassHasField(streamMergerClass, "collector");
        assertField(collector,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> Pattern.matches(
                "^%s<h11.unicode.CharWithIndex, .+?, h11.unicode.CharWithIndex\\[]>$".formatted(Collector.class.getName()),
                type.getTypeName()
            ),
            null);

        streamMergerConstructor = assertClassHasConstructor(streamMergerClass, constructor ->
            constructor.getParameters().length == 0);
        assertConstructor(streamMergerConstructor, Modifier.PUBLIC);

        merge = assertClassHasMethod(streamMergerClass, method -> {
            Type[] parameterTypes = method.getGenericParameterTypes();

            return method.getName().equals("merge")
                && method.getGenericReturnType().getTypeName().equals("h11.unicode.CharWithIndex[]")
                && parameterTypes.length == 1
                && parameterTypes[0].getTypeName().equals("%s<%s>[]".formatted(Stream.class.getName(), Integer.class.getName()));
        });
        assertMethod(merge, Modifier.PUBLIC, null, null, (Predicate<Type>) null);
    }

    /**
     * Tests for {@link StreamMerger#predicate}.
     */
    @Test
    @TestID(2)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("2 | Field predicate")
    void testPredicate() {
        predicate.setAccessible(true);

        Predicate<Integer> actualPredicate = getFieldValue(predicate, newInstance(streamMergerConstructor));
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
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | Field comparator")
    void testComparator() {
        comparator.setAccessible(true);

        Comparator<Integer> referenceComparator = Comparator.comparingInt(StreamMergerSolution::digitSum);
        Comparator<Integer> actualComparator = getFieldValue(comparator, newInstance(streamMergerConstructor));

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
    @TestID(4)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("4 | Field function")
    void testFunction() {
        function.setAccessible(true);

        Function<Integer, ?> actualFunction = getFieldValue(function, newInstance(streamMergerConstructor));

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
    @TestID(5)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("5 | Collector supplier and finisher")
    void testCollectorSupplierFinisher() {
        collector.setAccessible(true);

        Collector<Object, Object, Object[]> actualCollector = getFieldValue(collector, newInstance(streamMergerConstructor));
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
    @TestID(6)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("6 | Collector accumulator")
    void testAccumulator() {
        collector.setAccessible(true);

        Collector<Object, Object, Object[]> actualCollector = getFieldValue(collector, newInstance(streamMergerConstructor));
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
    @TestID(7)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("7 | Collector combiner")
    void testCombiner() {
        collector.setAccessible(true);

        Collector<Object, Object, Object[]> actualCollector = getFieldValue(collector, newInstance(streamMergerConstructor));
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
    @TestID(8)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("8 | merge(java.util.stream.Stream[])")
    void testMerge() {
        Object instance = newInstance(streamMergerConstructor);
        Class<?> charWithIndexClass = getCharWithIndexClass();
        Integer[][] streamElements = new Integer[][] {
            {0, 1, 2, 3, 4, 5},
            {10, 100, 1000, 10000},
            {1, 2, 4, null, null, 32},
            {-1, 0, null, 1, (int) Character.MAX_VALUE, Character.MAX_VALUE + 1}
        };

        Object[] referenceResultArray = StreamMergerSolution.merge(
            Arrays.stream(streamElements).map(Arrays::stream).toArray(Stream[]::new)
        );
        Object[] actualResultArray = invokeMethod(merge, instance,
            (Object) Arrays.stream(streamElements).map(Arrays::stream).toArray(Stream[]::new));

        assertEquals(referenceResultArray.length, actualResultArray.length,
            "Length of returned array differs from expected length");
        for (int i = 0; i < referenceResultArray.length; i++) {
            if (referenceResultArray[i] != null) {
                assertEquals(charWithIndexClass, actualResultArray[i].getClass(),
                    "Class of object at index %d does not match expected one".formatted(i));
            } else {
                assertNull(actualResultArray[i], "Element at index %d should be null".formatted(i));
            }
        }
    }

    @Override
    public void check(int testID) {
        assumeTrue(streamMergerClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(streamMergerConstructor != null,
            "Constructor for class %s could not be found".formatted(className));

        switch (testID) {
            case 2 ->
                assumeTrue(predicate != null,
                    "Field %s#predicate could not be found".formatted(className));

            case 3 ->
                assumeTrue(comparator != null,
                    "Field %s#comparator could not be found".formatted(className));

            case 4 ->
                assumeTrue(function != null,
                    "Field %s#function could not be found".formatted(className));

            case 5, 6, 7 ->
                assumeTrue(collector != null,
                    "Field %s#collector could not be found".formatted(className));

            case 8 ->
                assumeTrue(merge != null,
                    "Method %s#merge(java.util.stream.Stream[]) could not be found".formatted(className));

            // Checkstyle doesn't like switches without default branch so here's a no-op, I guess
            default -> assumeTrue(
                ((1 / 3) << 5 & 67 | -4 ^ 0b10 >> 4 / 3 * 2 % (-'☕' << "(͡° ͜ʖ ͡°)".chars().sum() >>> (0xb7c2))) == -4
            );
        }
    }

    private static Object newCharWithIndexInstance() {
        final String className = "h11.unicode.CharWithIndex";

        try {
            Class<?> charWithIndexClass = Class.forName(className);
            Constructor<?> charWithIndexConstructor = assertClassHasConstructor(charWithIndexClass, constructor -> {
                Type[] parameters = constructor.getGenericParameterTypes();

                return parameters.length == 2
                    && parameters[0].getTypeName().equals(Character.class.getName())
                    && parameters[1].getTypeName().equals(Integer.class.getName());
            });

            return charWithIndexConstructor.newInstance('a', (int) 'a');
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class %s does not exist".formatted(className), e);
        } catch (InstantiationException e) {
            throw new AssertionFailedError("Could not create instance of " + className, e);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Could not access constructor of class " + className, e);
        } catch (InvocationTargetException e) {
            throw new AssertionFailedError("An exception occurred while instantiating " + className,
                e.getCause());
        }
    }

    private static Class<?> getCharWithIndexClass() {
        try {
            return Class.forName("h11.unicode.CharWithIndex");
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class h11.unicode.CharWithIndex could not be found", e);
        }
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
