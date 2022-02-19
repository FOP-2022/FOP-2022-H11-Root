package h11.supplier;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
    void testDefinitions() {
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
    void testBuildIntegerArray() {
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
    void testBuildIntegerList() {
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

    // TODO: implement tests for testArraySupplier, testCollectionSupplier and testCyclicRangeSupplier

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

            // Checkstyle doesn't like switches without default branch so here's a no-op, I guess
            default -> assumeTrue(
                ((1 / 3) << 5 & 67 | -4 ^ 0b10 >> 4 / 3 * 2 % (-'☕' << "(͡° ͜ʖ ͡°)".chars().sum() >>> (0xb7c2))) == -4
            );
        }
    }
}
