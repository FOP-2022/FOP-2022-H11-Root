package h11.supplier;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassImplements;
import static h11.utils.Assertions.assertClassNotGeneric;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertMethod;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CyclicRangeSupplier}.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CyclicRangeSupplierTests extends AbstractTestClass {

    private static Class<?> cyclicRangeSupplierClass = null;
    private static Constructor<?> cyclicRangeSupplierConstructor = null;
    private static Method get = null;

    /**
     * Creates a new {@link CyclicRangeSupplierTests} object.
     */
    public CyclicRangeSupplierTests() {
        super("h11.supplier.CyclicRangeSupplier");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        cyclicRangeSupplierClass = assertClassExists(className);
        assertClassHasExactModifiers(cyclicRangeSupplierClass, Modifier.PUBLIC);
        assertClassNotGeneric(cyclicRangeSupplierClass);
        assertClassImplements(cyclicRangeSupplierClass,
            "%s<%s>".formatted(Supplier.class.getName(), Integer.class.getName()));

        cyclicRangeSupplierConstructor = assertClassHasConstructor(cyclicRangeSupplierClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 2 && parameterTypes[0].getTypeName().equals(int.class.getName())
                && parameterTypes[1].getTypeName().equals(int.class.getName());
        });
        assertConstructor(cyclicRangeSupplierConstructor, Modifier.PUBLIC, null, null);

        get = assertClassHasMethod(cyclicRangeSupplierClass, method ->
            method.getGenericReturnType().getTypeName().equals(Integer.class.getName())
                && method.getName().equals("get")
                && method.getParameters().length == 0);
        assertMethod(get, Modifier.PUBLIC, type -> type.getTypeName().equals(Integer.class.getName()), "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link CyclicRangeSupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    public void testInstance() {
        assumeTrue(cyclicRangeSupplierClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(cyclicRangeSupplierConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(get != null, "Method %s#get() could not be found".formatted(className));

        Integer[][] parameterMatrix = {
            {  0,   9},
            {-10,   0},
            {  0,   0},
            {  0, -10},
            {  9,   0}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(cyclicRangeSupplierConstructor, (Object[]) parameters);

            Iterator<Integer> iterator = IntStream
                .iterate(parameters[0], i -> i == parameters[1] ? parameters[0] : parameters[0] < parameters[1] ? i + 1 : i - 1)
                .iterator();

            for (int i = 0; i < 50; i++) {
                assertEquals(iterator.next(), invokeMethod(get, instance), "Values did not match in iteration " + i);
            }
        }
    }
}
