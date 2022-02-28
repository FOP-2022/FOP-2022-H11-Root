package h11.supplier;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import kotlin.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link SupplierTests}.
 */
@SuppressWarnings("JavadocReference")
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SupplierMetaTests extends AbstractTestClass implements PreInvocationCheck {

    private static Class<?> supplierTestsClass = null;
    private static Constructor<?> supplierTestsConstructor = null;
    private static Method testArraySupplier = null;
    private static Method testCollectionSupplier = null;
    private static Method testCyclicRangeSupplier = null;
    private static Method buildIntegerArray = null;
    private static Method buildIntegerList = null;

    private final static List<Integer[]> BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    private final static List<Integer[]> BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    private final static List<Integer[]> CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    /**
     * Updated via bytecode transformation whenever {@link CyclicRangeSupplier#get()}
     * is invoked in {@link SupplierTests#testCyclicRangeSupplier()}.
     */
    @SuppressWarnings("FieldMayBeFinal") public static int CYCLIC_RANGE_SUPPLIER_GET_CALLS = 0;

    /**
     * Creates a new {@link SupplierMetaTests} object.
     */
    public SupplierMetaTests() {
        super("h11.supplier.SupplierTests");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        supplierTestsClass = assertClassExists(className);
        assertClassHasExactModifiers(supplierTestsClass, Modifier.PUBLIC);

        supplierTestsConstructor = assertClassHasConstructor(supplierTestsClass, constructor ->
            constructor.getGenericParameterTypes().length == 0);

        testArraySupplier = assertClassHasMethod(supplierTestsClass, "testArraySupplier");
        assertAnnotations(testArraySupplier, Test.class);
        assertMethod(
            testArraySupplier,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );

        testCollectionSupplier = assertClassHasMethod(supplierTestsClass, "testCollectionSupplier");
        assertAnnotations(testCollectionSupplier, Test.class);
        assertMethod(
            testCollectionSupplier,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );

        testCyclicRangeSupplier = assertClassHasMethod(supplierTestsClass, "testCyclicRangeSupplier");
        assertAnnotations(testCyclicRangeSupplier, Test.class);
        assertMethod(
            testCyclicRangeSupplier,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );

        buildIntegerArray = assertClassHasMethod(supplierTestsClass, "buildIntegerArray");
        assertMethod(
            buildIntegerArray,
            Modifier.PRIVATE,
            type -> type.getTypeName().equals(Integer.class.getName() + "[]"),
            null,
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName())
        );

        buildIntegerList = assertClassHasMethod(supplierTestsClass, "buildIntegerList");
        assertMethod(
            buildIntegerList,
            Modifier.PRIVATE,
            type -> type.getTypeName().equals("%s<%s>".formatted(List.class.getName(), Integer.class.getName())),
            null,
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName())
        );
    }

    /**
     * Tests for {@link SupplierTests#buildIntegerArray(int, int, int)}.
     */
    @Test
    @TestID(2)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("2 | buildIntegerArray(int, int, int)")
    public void testBuildIntegerArray() {
        buildIntegerArray.setAccessible(true);

        Integer[][] parameterMatrix = {
            {  0,                 0,  0},
            { 10,                 0,  0},
            { 10,                 0, -1},
            { 20,                -1,  1},
            { 10, Integer.MAX_VALUE,  1},
            {100,                20, 10}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(supplierTestsConstructor);
            Integer[] integerArray = invokeMethod(buildIntegerArray, instance, (Object[]) parameters);

            assertEquals(parameters[0], integerArray.length,
                "Size of array returned by %s differs from expected".formatted(buildIntegerArray.getName()));
            for (int i = 0; i < parameters[0]; i++) {
                assertEquals(parameters[1] + i * parameters[2], integerArray[i],
                    "Value in the array returned by %s differs from expected at index %d"
                        .formatted(buildIntegerArray.getName(), i));
            }
        }
    }

    /**
     * Tests for {@link SupplierTests#buildIntegerList(int, int, int)}.
     */
    @Test
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | buildIntegerList(int, int, int)")
    public void testBuildIntegerList() {
        buildIntegerList.setAccessible(true);

        Integer[][] parameterMatrix = {
            { 0,                 0,                     1},
            {10,                 0,                     1},
            {10,                -1,                     1},
            {20,                -1,                    10},
            {10, Integer.MIN_VALUE, Integer.MAX_VALUE - 1}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(supplierTestsConstructor);
            List<Integer> integerList = invokeMethod(buildIntegerList, instance, (Object[]) parameters);

            assertTrue(integerList instanceof LinkedList<Integer>,
                "List returned by %s is not an instance of java.util.LinkedList".formatted(buildIntegerList.getName()));
            assertEquals(parameters[0], integerList.size(),
                "Size of list returned by %s differs from expected".formatted(buildIntegerList.getName()));
            for (int i = 0; i < parameters[0]; i++) {
                assertTrue(integerList.get(i) >= parameters[1],
                    "Element at index %d is out of bounds (less than %d)".formatted(i, parameters[1]));
                assertTrue(integerList.get(i) <= parameters[2],
                    "Element at index %d is out of bounds (greater than %d)".formatted(i, parameters[2]));
            }
            List<Integer> referenceList = (List<Integer>) ((LinkedList<Integer>) integerList).clone();
            integerList.sort(Comparator.naturalOrder());
            for (int i = 0; i < integerList.size(); i++) {
                assertEquals(integerList.get(i), referenceList.get(i),
                    "Returned list is not ordered - values differ at index %d after sorting again".formatted(i));
            }
        }
    }

    /**
     * Tests for {@link SupplierTests#testArraySupplier()}.
     * Needs to have bytecode transformations done in order to work.
     *
     * @see h11.utils.transform.SupplierTestVisitors.ArraySupplierMethodVisitor
     */
    @Test
    @TestID(4)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("4 | testArraySupplier()")
    public void metaTest_testArraySupplier() {
        Map<Pair<Predicate<Integer[]>, String>, Boolean> buildIntegerArrayInvocationPredicates = Stream
            .<Pair<Predicate<Integer[]>, String>>of(
                new Pair<>(arguments -> arguments[0] == 0,
                    "buildIntegerArray(int, int, int) was not called with length == 0 at least once"),
                new Pair<>(arguments -> arguments[0] == 1,
                    "buildIntegerArray(int, int, int) was not called with length == 1 at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && arguments[2] < -1,
                    "buildIntegerArray(int, int, int) was not called with length >= 100 and offset < -1 at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && arguments[2] == 0,
                    "buildIntegerArray(int, int, int) was not called with length >= 100 and offset == 0 at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && arguments[2] > 1,
                    "buildIntegerArray(int, int, int) was not called with length >= 100 and offset > 1 at least once")
            )
            .collect(Collectors.toMap(pair -> pair, pair -> false));

        BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(testArraySupplier, newInstance(supplierTestsConstructor));

        if (BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.size() < 5) {
            throw new AssertionFailedError("buildIntegerArray(int, int, int) has not been called at least five times",
                "at least 5",
                BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.size());
        }
        BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.forEach(arguments ->
            buildIntegerArrayInvocationPredicates.replaceAll((pair, boolVal) -> boolVal || pair.getFirst().test(arguments)));
        buildIntegerArrayInvocationPredicates.forEach((pair, bool) -> assertTrue(bool, pair.getSecond()));
    }

    /**
     * Tests for {@link SupplierTests#testCollectionSupplier()}.
     * Needs to have bytecode transformations done in order to work.
     *
     * @see h11.utils.transform.SupplierTestVisitors.CollectionSupplierMethodVisitor
     */
    @Test
    @TestID(5)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("5 | testCollectionSupplier()")
    public void metaTest_testCollectionSupplier() {
        Map<Pair<Predicate<Integer[]>, String>, Boolean> buildIntegerListInvocationPredicates = Stream
            .<Pair<Predicate<Integer[]>, String>>of(
                new Pair<>(arguments -> arguments[0] == 0,
                    "buildIntegerList(int, int, int) was not called with length == 0 at least once"),
                new Pair<>(arguments -> arguments[0] == 1,
                    "buildIntegerList(int, int, int) was not called with length == 1 at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && arguments[1] == arguments[2],
                    "buildIntegerList(int, int, int) was not called with length >= 100 and min == max at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && arguments[1] + 11 == arguments[2],
                    "buildIntegerList(int, int, int) was not called with length >= 100 and min + 11 == max at least once"),
                new Pair<>(arguments -> arguments[0] >= 100 && Math.abs(arguments[2] - arguments[1]) >= 10 * arguments[0],
                    "buildIntegerList(int, int, int) was not called with length >= 100 and "
                        + "max - min >= 10 * length at least once")
            )
            .collect(Collectors.toMap(pair -> pair, pair -> false));

        BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(testCollectionSupplier, newInstance(supplierTestsConstructor));

        if (BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.size() < 5) {
            throw new AssertionFailedError("buildIntegerList(int, int, int) has not been called at least five times",
                "at least 5",
                BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.size());
        }
        BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.forEach(arguments ->
            buildIntegerListInvocationPredicates.replaceAll((pair, boolVal) -> boolVal || pair.getFirst().test(arguments)));
        buildIntegerListInvocationPredicates.forEach((pair, bool) -> assertTrue(bool, pair.getSecond()));
    }

    /**
     * Tests for {@link SupplierTests#testCyclicRangeSupplier()}.
     * Needs to have bytecode transformations done in order to work.
     *
     * @see h11.utils.transform.SupplierTestVisitors.CyclicRangeSupplierMethodVisitor
     */
    @Test
    @TestID(6)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("6 | testCyclicRangeSupplier()")
    public void metaTest_testCyclicRangeSupplier() {
        CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(testCyclicRangeSupplier, newInstance(supplierTestsConstructor));

        boolean b = CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS
            .stream()
            .anyMatch(arguments -> CYCLIC_RANGE_SUPPLIER_GET_CALLS >= (Math.abs(arguments[1] - arguments[0]) + 1) * 3);
        if (!b) {
            throw new AssertionFailedError("CyclicRangeSupplier.get() was not invoked at least (|last - first| + 1) * 3 times");
        }
    }

    @Override
    public void check(int testID) {
        assumeTrue(supplierTestsClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(supplierTestsConstructor != null,
            "Constructor for class %s could not be found".formatted(className));

        switch (testID) {
            case 2 ->
                assumeTrue(buildIntegerArray != null,
                    "Method %s#buildIntegerArray(int, int, int) could not be found".formatted(className));

            case 3 ->
                assumeTrue(buildIntegerList != null,
                    "Method %s#buildIntegerList(int, int, int) could not be found".formatted(className));

            case 4 ->
                assumeTrue(testArraySupplier != null,
                    "Method %s#testArraySupplier() could not be found".formatted(className));

            case 5 ->
                assumeTrue(testCollectionSupplier != null,
                    "Method %s#testCollectionSupplier() could not be found".formatted(className));

            case 6 ->
                assumeTrue(testCyclicRangeSupplier != null,
                    "Method %s#testCyclicRangeSupplier() could not be found".formatted(className));

            // Checkstyle doesn't like switches without default branch so here's a no-op, I guess
            default -> assumeTrue(
                ((1 / 3) << 5 & 67 | -4 ^ 0b10 >> 4 / 3 * 2 % (-'☕' << "(͡° ͜ʖ ͡°)".chars().sum() >>> (0xb7c2))) == -4
            );
        }
    }

    /**
     * Interceptor method that is called before {@link SupplierTests#buildIntegerArray(int, int, int)}
     * and logs passed arguments to {@link #BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS}.
     */
    @SuppressWarnings("unused")
    public static void interceptBuildIntegerArray(int i1, int i2, int i3) {
        BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.add(new Integer[] {i1, i2, i3});
    }

    /**
     * Interceptor method that is called before {@link SupplierTests#buildIntegerList(int, int, int)}
     * and logs passed arguments to {@link #BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS}.
     */
    @SuppressWarnings("unused")
    public static void interceptBuildIntegerList(int i1, int i2, int i3) {
        BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.add(new Integer[] {i1, i2, i3});
    }

    /**
     * Interceptor method that is called before {@link CyclicRangeSupplier#CyclicRangeSupplier(int, int)}
     * and logs passed arguments to {@link #CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS}.
     */
    @SuppressWarnings("unused")
    public static void interceptCyclicRangeSupplierConstructor(int i1, int i2) {
        CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS.add(new Integer[] {i1, i2});
    }
}
