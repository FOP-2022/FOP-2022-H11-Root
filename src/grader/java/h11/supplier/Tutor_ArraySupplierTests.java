package h11.supplier;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

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

import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertClassImplements;
import static h11.utils.Assertions.assertClassTypeParameters;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertSame;

/**
 * Tests for class {@link ArraySupplier}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_ArraySupplierTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "ArraySupplier(T[])";
    public static final String METHOD_GET_SIGNATURE = "get()";

    /**
     * Creates a new {@link Tutor_ArraySupplierTests} object.
     */
    public Tutor_ArraySupplierTests() {
        super(
            "h11.supplier.ArraySupplier",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.supplier." + CONSTRUCTOR_SIGNATURE)),
            Map.of(METHOD_GET_SIGNATURE, predicateFromSignature(METHOD_GET_SIGNATURE))
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasModifiers(clazz, Modifier.PUBLIC);
        assertClassTypeParameters(clazz, new String[] {"T"}, new String[][] {{Object.class.getName()}});
        assertClassImplements(clazz, Supplier.class.getName() + "<T>");

        assertConstructor(assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE), Modifier.PUBLIC, (Predicate<Type>) null);

        assertMethod(assertClassHasMethod(this, METHOD_GET_SIGNATURE),
            Modifier.PUBLIC,
            type -> type.getTypeName().equals("T"),
            "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link ArraySupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    public void testInstance() {
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
            Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE), (Object) parameters);

            for (int i = 0; i < parameters.length + 5; i++) {
                assertSame(i < parameters.length ? parameters[i] : null,
                    invokeMethod(getMethod(METHOD_GET_SIGNATURE), instance));
            }
        }
    }
}
