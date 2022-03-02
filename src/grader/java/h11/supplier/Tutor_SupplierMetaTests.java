package h11.supplier;

import h11.utils.AbstractTestClass;
import kotlin.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

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
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertTrue;

/**
 * Tests for class {@link SupplierTests}.
 */
@SuppressWarnings("JavadocReference")
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_SupplierMetaTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "SupplierTests()";
    public static final String METHOD_TEST_ARRAY_SUPPLIER = "testArraySupplier()";
    public static final String METHOD_TEST_COLLECTION_SUPPLIER = "testCollectionSupplier()";
    public static final String METHOD_TEST_CYCLIC_RANGE_SUPPLIER = "testCyclicRangeSupplier()";
    public static final String METHOD_BUILD_INTEGER_ARRAY = "buildIntegerArray(int, int, int)";
    public static final String METHOD_BUILD_INTEGER_LIST = "buildIntegerList(int, int, int)";

    private static final List<Integer[]> BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    private static final List<Integer[]> BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    private static final List<Integer[]> CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS = new ArrayList<>();
    /**
     * Updated via bytecode transformation whenever {@link CyclicRangeSupplier#get()}
     * is invoked in {@link SupplierTests#testCyclicRangeSupplier()}.
     */
    @SuppressWarnings("FieldMayBeFinal") public static int CYCLIC_RANGE_SUPPLIER_GET_CALLS = 0;

    /**
     * Creates a new {@link Tutor_SupplierMetaTests} object.
     */
    public Tutor_SupplierMetaTests() {
        super(
            "h11.supplier.SupplierTests",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.supplier." + CONSTRUCTOR_SIGNATURE)),
            Map.of(
                METHOD_TEST_ARRAY_SUPPLIER, predicateFromSignature(METHOD_TEST_ARRAY_SUPPLIER),
                METHOD_TEST_COLLECTION_SUPPLIER, predicateFromSignature(METHOD_TEST_COLLECTION_SUPPLIER),
                METHOD_TEST_CYCLIC_RANGE_SUPPLIER, predicateFromSignature(METHOD_TEST_CYCLIC_RANGE_SUPPLIER),
                METHOD_BUILD_INTEGER_ARRAY, predicateFromSignature(METHOD_BUILD_INTEGER_ARRAY),
                METHOD_BUILD_INTEGER_LIST, predicateFromSignature(METHOD_BUILD_INTEGER_LIST)
            )
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasModifiers(clazz, Modifier.PUBLIC);

        assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE);

        Method testArraySupplier = assertClassHasMethod(this, METHOD_TEST_ARRAY_SUPPLIER);
        assertAnnotations(testArraySupplier, Test.class);
        assertMethod(testArraySupplier, Modifier.PUBLIC, type -> type.getTypeName().equals(void.class.getName()), null);

        Method testCollectionSupplier = assertClassHasMethod(this, METHOD_TEST_COLLECTION_SUPPLIER);
        assertAnnotations(testCollectionSupplier, Test.class);
        assertMethod(testCollectionSupplier, Modifier.PUBLIC, type -> type.getTypeName().equals(void.class.getName()), null);

        Method testCyclicRangeSupplier = assertClassHasMethod(this, METHOD_TEST_CYCLIC_RANGE_SUPPLIER);
        assertAnnotations(testCyclicRangeSupplier, Test.class);
        assertMethod(testCyclicRangeSupplier, Modifier.PUBLIC, type -> type.getTypeName().equals(void.class.getName()), null);

        assertMethod(
            assertClassHasMethod(this, METHOD_BUILD_INTEGER_ARRAY),
            Modifier.PRIVATE,
            type -> type.getTypeName().equals(Integer.class.getName() + "[]"),
            null,
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName()),
            type -> type.getTypeName().equals(int.class.getName())
        );

        assertMethod(
            assertClassHasMethod(this, METHOD_BUILD_INTEGER_LIST),
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
    @DisplayName("2 | buildIntegerArray(int, int, int)")
    public void testBuildIntegerArray() {
        Method buildIntegerArray = getMethod(METHOD_BUILD_INTEGER_ARRAY);
        Integer[][] parameterMatrix = {
            {  0,                 0,  0},
            { 10,                 0,  0},
            { 10,                 0, -1},
            { 20,                -1,  1},
            { 10, Integer.MAX_VALUE,  1},
            {100,                20, 10}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE));
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
    @DisplayName("3 | buildIntegerList(int, int, int)")
    public void testBuildIntegerList() {
        Method buildIntegerList = getMethod(METHOD_BUILD_INTEGER_LIST);
        Integer[][] parameterMatrix = {
            { 0,                 0,                     1},
            {10,                 0,                     1},
            {10,                -1,                     1},
            {20,                -1,                    10},
            {10, Integer.MIN_VALUE, Integer.MAX_VALUE - 1}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE));
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
    @DisplayName("4 | testArraySupplier()")
    public void metaTest_testArraySupplier() {
        BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(getMethod(METHOD_TEST_ARRAY_SUPPLIER), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        if (BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.size() < 5) {
            throw new AssertionFailedError("buildIntegerArray(int, int, int) has not been called at least five times",
                "at least 5",
                BUILD_INTEGER_ARRAY_INTERCEPTED_ARGUMENTS.size());
        }
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
    @DisplayName("5 | testCollectionSupplier()")
    public void metaTest_testCollectionSupplier() {
        BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(getMethod(METHOD_TEST_COLLECTION_SUPPLIER), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        if (BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.size() < 5) {
            throw new AssertionFailedError("buildIntegerList(int, int, int) has not been called at least five times",
                "at least 5",
                BUILD_INTEGER_LIST_INTERCEPTED_ARGUMENTS.size());
        }
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
    @DisplayName("6 | testCyclicRangeSupplier()")
    public void metaTest_testCyclicRangeSupplier() {
        CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS.clear();
        invokeMethod(getMethod(METHOD_TEST_CYCLIC_RANGE_SUPPLIER), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        boolean b = CYCLIC_RANGE_SUPPLIER_CONSTRUCTOR_INTERCEPTED_ARGUMENTS
            .stream()
            .anyMatch(arguments -> CYCLIC_RANGE_SUPPLIER_GET_CALLS >= (Math.abs(arguments[1] - arguments[0]) + 1) * 3);
        if (!b) {
            throw new AssertionFailedError("CyclicRangeSupplier.get() was not invoked at least (|last - first| + 1) * 3 times");
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