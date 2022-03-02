package h11.supplier;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertClassImplements;
import static h11.utils.Assertions.assertClassNotGeneric;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertMethod;

/**
 * Tests for class {@link CyclicRangeSupplier}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_CyclicRangeSupplierTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "CyclicRangeSupplier(int, int)";
    public static final String METHOD_GET_SIGNATURE = "get()";

    /**
     * Creates a new {@link Tutor_CyclicRangeSupplierTests} object.
     */
    public Tutor_CyclicRangeSupplierTests() {
        super(
            "h11.supplier.CyclicRangeSupplier",
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
        assertClassNotGeneric(clazz);
        assertClassImplements(clazz, "%s<%s>".formatted(Supplier.class.getName(), Integer.class.getName()));

        assertConstructor(assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE), Modifier.PUBLIC, null, null);

        assertMethod(assertClassHasMethod(this, METHOD_GET_SIGNATURE),
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(Integer.class.getName()),
            "get");
    }

    /**
     * Tests for successfully instantiating the class with an array of objects
     * and for the correct return value of {@link CyclicRangeSupplier#get()}.
     */
    @Test
    @DisplayName("2 | Class instance and method tests")
    public void testInstance() {
        Integer[][] parameterMatrix = {
            {  0,   9},
            {-10,   0},
            {  0,   0},
            {  0, -10},
            {  9,   0}
        };

        for (Integer[] parameters : parameterMatrix) {
            Object instance = newInstance(getConstructor(CONSTRUCTOR_SIGNATURE), (Object[]) parameters);

            Iterator<Integer> iterator = IntStream
                .iterate(parameters[0], i -> i == parameters[1] ? parameters[0] : parameters[0] < parameters[1] ? i + 1 : i - 1)
                .iterator();

            for (int i = 0; i < 50; i++) {
                assertEquals(iterator.next(),
                    invokeMethod(getMethod(METHOD_GET_SIGNATURE), instance),
                    "Values did not match in iteration " + i);
            }
        }
    }
}
