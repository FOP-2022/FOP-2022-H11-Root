package h11.supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.*;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static h11.utils.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CyclicRangeSupplier}.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CyclicRangeSupplierTests {

    private static final String CLASS_NAME = "h11.supplier.CyclicRangeSupplier";

    private static Class<?> cyclicRangeSupplierClass = null;
    private static Constructor<?> cyclicRangeSupplierConstructor = null;
    private static Method get = null;

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        cyclicRangeSupplierClass = assertClassExists(CLASS_NAME);
        assertClassHasExactModifiers(cyclicRangeSupplierClass, Modifier.PUBLIC);
        assertClassNotGeneric(cyclicRangeSupplierClass);
        assertClassImplements(cyclicRangeSupplierClass,
            "%s<%s>".formatted(Supplier.class.getName(), Integer.class.getName()));

        cyclicRangeSupplierConstructor = assertClassHasConstructor(cyclicRangeSupplierClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 2 && parameterTypes[0].getTypeName().equals(int.class.getName()) &&
                parameterTypes[1].getTypeName().equals(int.class.getName());
        });
        assertConstructor(cyclicRangeSupplierConstructor, Modifier.PUBLIC, null, null);

        get = assertClassHasMethod(cyclicRangeSupplierClass, method ->
            method.getGenericReturnType().getTypeName().equals(Integer.class.getName()) &&
                method.getName().equals("get") &&
                method.getParameters().length == 0);
        assertMethod(get, Modifier.PUBLIC, type -> type.getTypeName().equals(Integer.class.getName()), "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link CyclicRangeSupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    void testInstance() {
        assumeTrue(cyclicRangeSupplierClass != null, "Class %s could not be found".formatted(CLASS_NAME));
        assumeTrue(cyclicRangeSupplierConstructor != null,
            "Constructor for class %s could not be found".formatted(CLASS_NAME));
        assumeTrue(get != null, "Method %s#get() could not be found".formatted(CLASS_NAME));

        Integer[][] parameterMatrix = {
            {  0,   9},
            {-10,   0},
            {  0,   0},
            {  0, -10},
            {  9,   0}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance;
            try {
                instance = cyclicRangeSupplierConstructor.newInstance((Object[]) parameters);
            } catch (InstantiationException e) {
                throw new AssertionFailedError("Could not create instance of " + CLASS_NAME, e);
            } catch (IllegalAccessException e) {
                throw new AssertionFailedError("Could not access constructor of class " + CLASS_NAME, e);
            } catch (InvocationTargetException e) {
                throw new AssertionFailedError("An exception occurred while instantiating " + CLASS_NAME,
                    e.getCause());
            }

            Iterator<Integer> iterator = IntStream
                .iterate(parameters[0], i -> i == parameters[1] ? parameters[0] : parameters[0] < parameters[1] ? i + 1 : i - 1)
                .iterator();

            for (int i = 0; i < 50; i++) {
                try {
                    assertEquals(iterator.next(), get.invoke(instance), "Values did not match in iteration " + i);
                } catch (IllegalAccessException e) {
                    throw new AssertionFailedError("Could not access constructor of class " + CLASS_NAME, e);
                } catch (InvocationTargetException e) {
                    throw new AssertionFailedError("An exception occurred while invoking %s#get()".formatted(CLASS_NAME),
                        e.getCause());
                }
            }
        }
    }
}
