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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
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
 * Tests for class {@link ArraySupplier}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_ArraySupplierTests extends AbstractTestClass {

    private static Class<?> arraySupplierClass = null;
    private static Constructor<?> arraySupplierConstructor = null;
    private static Method get = null;

    /**
     * Creates a new {@link Tutor_ArraySupplierTests} object.
     */
    public Tutor_ArraySupplierTests() {
        super("h11.supplier.ArraySupplier");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        arraySupplierClass = assertClassExists(className);
        assertClassHasExactModifiers(arraySupplierClass, Modifier.PUBLIC);
        assertClassTypeParameters(arraySupplierClass, new String[] {"T"}, new String[][] {{Object.class.getName()}});
        assertClassImplements(arraySupplierClass, Supplier.class.getName() + "<T>");

        arraySupplierConstructor = assertClassHasConstructor(arraySupplierClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 1 && parameterTypes[0].getTypeName().equals("T[]");
        });
        assertConstructor(arraySupplierConstructor, Modifier.PUBLIC, (Predicate<Type>) null);

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
    public void testInstance() {
        assumeTrue(arraySupplierClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(arraySupplierConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(get != null, "Method %s#get() could not be found".formatted(className));

        Map<List<ArrayList<LinkedList<Set<Vector<Integer>>>>>[],
            Map<Map<List<ArrayList<LinkedList<Set<Vector<Integer>>>>>[],
                List<ArrayList<LinkedList<Set<Vector<Integer>>>>>[]>,
                List<ArrayList<LinkedList<Set<Vector<Integer>>>>>>> abomination = Map.of();
        Object[][] parameterMatrix = {
            "Hello world!".split(""),
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {new Object(), new Object(), new Object(), new Object(), new Object()},
            {null, null, null},
            {"Abc", Integer.MIN_VALUE, Long.MAX_VALUE, 'X', null, new Object()},
            {abomination},
            {}
        };

        for (Object[] parameters : parameterMatrix) {
            Object instance = newInstance(arraySupplierConstructor, (Object) parameters);

            for (int i = 0; i < parameters.length + 5; i++) {
                assertSame(i < parameters.length ? parameters[i] : null, invokeMethod(get, instance));
            }
        }
    }
}
