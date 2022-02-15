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
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassImplements;
import static h11.utils.Assertions.assertClassTypeParameters;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertSame;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CollectionSupplier}.
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CollectionSupplierTests {

    private static final String CLASS_NAME = "h11.supplier.CollectionSupplier";

    private static Class<?> collectionSupplierClass = null;
    private static Constructor<?> collectionSupplierConstructor = null;
    private static Method get = null;

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        collectionSupplierClass = assertClassExists(CLASS_NAME);
        assertClassHasExactModifiers(collectionSupplierClass, Modifier.PUBLIC);
        assertClassTypeParameters(collectionSupplierClass, new String[] {"T"}, new String[][] {{Object.class.getName()}});
        assertClassImplements(collectionSupplierClass, Supplier.class.getName() + "<T>");

        collectionSupplierConstructor = assertClassHasConstructor(collectionSupplierClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 1 && parameterTypes[0].getTypeName().equals(Collection.class.getName() + "<T>");
        });
        assertConstructor(collectionSupplierConstructor, Modifier.PUBLIC, (Predicate<Type>) null);

        get = assertClassHasMethod(collectionSupplierClass, method ->
            method.getName().equals("get") && method.getParameters().length == 0);
        assertMethod(get, Modifier.PUBLIC, type -> type.getTypeName().equals("T"), "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link CollectionSupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    void testInstance() {
        assumeTrue(collectionSupplierClass != null, "Class %s could not be found".formatted(CLASS_NAME));
        assumeTrue(collectionSupplierConstructor != null,
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
                instance = collectionSupplierConstructor.newInstance(Arrays.asList(parameters));
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

    // TODO: add checks for requirement violation
}
