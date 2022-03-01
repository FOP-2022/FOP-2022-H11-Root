package h11.supplier;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CollectionSupplier}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CollectionSupplierTests extends AbstractTestClass {

    private static Class<?> collectionSupplierClass = null;
    private static Constructor<?> collectionSupplierConstructor = null;
    private static Method get = null;

    public static boolean ILLEGAL_INSTRUCTION_USED = false;

    /**
     * Creates a new {@link CollectionSupplierTests} object.
     */
    public CollectionSupplierTests() {
        super("h11.supplier.CollectionSupplier");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        collectionSupplierClass = assertClassExists(className);
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
    public void testInstance() {
        assumeTrue(collectionSupplierClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(collectionSupplierConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(get != null, "Method %s#get() could not be found".formatted(className));

        Object[][] parameterMatrix = {
            "Hello world!".split(""),
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {new Object(), new Object(), new Object(), new Object(), new Object()},
            {null, null, null},
            {"Abc", Integer.MIN_VALUE, Long.MAX_VALUE, 'X', null, new Object()},
            {}
        };

        for (Object[] parameters : parameterMatrix) {
            Object instance = newInstance(collectionSupplierConstructor, Arrays.asList(parameters));

            for (int i = 0; i < parameters.length + 5; i++) {
                assertSame(i < parameters.length ? parameters[i] : null, invokeMethod(get, instance));
            }
        }
    }

    /**
     * Check that {@link CollectionSupplier} does not use calls to illegal methods.
     * Needs to have bytecode transformations done in order to work.
     *
     * @see h11.utils.transform.CollectionSupplierVisitor
     */
    @Test
    @DisplayName("2-R | No illegal classes / methods requirement")
    public void testIllegalInvocations() {
        ILLEGAL_INSTRUCTION_USED = false;
        invokeMethod(get, newInstance(collectionSupplierConstructor, List.of()));
        assertTrue(ILLEGAL_INSTRUCTION_USED, "Invocation of illegal class or method detected");
    }
}
