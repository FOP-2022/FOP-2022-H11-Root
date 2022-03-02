package h11.supplier;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class {@link CollectionSupplier}.
 */
@SuppressWarnings("JavadocReference")
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_CollectionSupplierTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "CollectionSupplier(%s<T>)".formatted(Collection.class.getName());
    public static final String METHOD_GET_SIGNATURE = "get()";

    public static boolean ILLEGAL_INSTRUCTION_USED = false;

    /**
     * Creates a new {@link Tutor_CollectionSupplierTests} object.
     */
    public Tutor_CollectionSupplierTests() {
        super(
            "h11.supplier.CollectionSupplier",
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
     * and for the correct return value of {@link CollectionSupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    public void testInstance() {
        Object[][] parameterMatrix = {
            "Hello world!".split(""),
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {new Object(), new Object(), new Object(), new Object(), new Object()},
            {null, null, null},
            {"Abc", Integer.MIN_VALUE, Long.MAX_VALUE, 'X', null, new Object()},
            {}
        };

        for (Object[] parameters : parameterMatrix) {
            Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE), Arrays.asList(parameters));

            for (int i = 0; i < parameters.length + 5; i++) {
                assertSame(i < parameters.length ? parameters[i] : null,
                    invokeMethod(getMethod(METHOD_GET_SIGNATURE), instance));
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
        invokeMethod(getMethod(METHOD_GET_SIGNATURE), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE), List.of()));
        assertTrue(ILLEGAL_INSTRUCTION_USED, "Invocation of illegal class or method detected");
    }
}
