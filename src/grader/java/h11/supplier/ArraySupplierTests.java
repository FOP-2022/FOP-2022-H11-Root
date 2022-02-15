package h11.supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Supplier;

import static h11.utils.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link ArraySupplier}.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ArraySupplierTests {

    private static final String CLASS_NAME = "h11.supplier.ArraySupplier";

    private static Class<?> arraySupplierClass = null;
    private static Constructor<?> arraySupplierConstructor = null;
    private static Method get = null;

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        arraySupplierClass = assertClassExists(CLASS_NAME);
        assertClassHasExactModifiers(arraySupplierClass, Modifier.PUBLIC);
        assertClassTypeParameters(arraySupplierClass, new String[] {"T"}, new String[][] {{Object.class.getName()}});
        assertClassImplements(arraySupplierClass, Supplier.class.getName() + "<T>");

        arraySupplierConstructor = assertClassHasConstructor(arraySupplierClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 1 && parameterTypes[0].getTypeName().equals("T[]");
        });
        assertConstructor(arraySupplierConstructor, Modifier.PUBLIC);

        get = assertClassHasMethod(arraySupplierClass, method ->
            method.getName().equals("get") && method.getParameters().length == 0);
        assertMethod(get, Modifier.PUBLIC, type -> type.getTypeName().equals("T"), "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link ArraySupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    void testInstance() {
        assumeTrue(arraySupplierClass != null, "Class %s could not be found".formatted(CLASS_NAME));
        assumeTrue(arraySupplierConstructor != null,
            "Constructor for class %s could not be found".formatted(CLASS_NAME));
        assumeTrue(get != null, "Method %s#get() could not be found".formatted(CLASS_NAME));

        Object[][] parameterMatrix = {
            "Hello world!".split(""),
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {new Object(), new Object(), new Object(), new Object(), new Object()},
            {null, null, null},
            {"Abc", Integer.MIN_VALUE, Long.MAX_VALUE, 'X', null, new Object()},
            {}
        };

        for (Object[] parameters : parameterMatrix) {
            Object instance;
            try {
                instance = arraySupplierConstructor.newInstance((Object) parameters);
            } catch (InstantiationException e) {
                throw new AssertionFailedError("Could not create instance of " + CLASS_NAME, e);
            } catch (IllegalAccessException e) {
                throw new AssertionFailedError("Could not access constructor of class " + CLASS_NAME, e);
            } catch (InvocationTargetException e) {
                throw new AssertionFailedError("An exception occurred while instantiating " + CLASS_NAME,
                    e.getCause());
            }

            for (int i = 0; i < parameters.length + 5; i++) {
                try {
                    assertSame(i < parameters.length ? parameters[i] : null, get.invoke(instance));
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
