package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;

import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasField;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertField;
import static h11.utils.Assertions.assertFieldValue;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertMethodReturnValue;
import static h11.utils.Assertions.assertTrue;

/**
 * Tests for class {@link CharWithIndex}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_CharWithIndexTests extends AbstractTestClass {

    public static final String FIELD_THE_CHAR_IDENTIFIER = "theChar";
    public static final String FIELD_INDEX_IDENTIFIER = "index";
    public static final String CONSTRUCTOR_SIGNATURE = "CharWithIndex(%s, %s)"
        .formatted(Character.class.getName(), Integer.class.getName());
    public static final String METHOD_GET_CHAR_SIGNATURE = "getChar()";
    public static final String METHOD_GET_INDEX_SIGNATURE = "getIndex()";

    /**
     * Creates a new {@link Tutor_CharWithIndexTests} object.
     */
    public Tutor_CharWithIndexTests() {
        super(
            "h11.unicode.CharWithIndex",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.unicode." + CONSTRUCTOR_SIGNATURE)),
            Map.of(
                FIELD_THE_CHAR_IDENTIFIER, field -> field.getName().equals(FIELD_THE_CHAR_IDENTIFIER),
                FIELD_INDEX_IDENTIFIER, field -> field.getName().equals(FIELD_INDEX_IDENTIFIER)
            ),
            Map.of(
                METHOD_GET_CHAR_SIGNATURE, predicateFromSignature(METHOD_GET_CHAR_SIGNATURE),
                METHOD_GET_INDEX_SIGNATURE, predicateFromSignature(METHOD_GET_INDEX_SIGNATURE)
            )
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasModifiers(clazz, Modifier.PUBLIC);

        assertField(assertClassHasField(this, FIELD_THE_CHAR_IDENTIFIER),
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(char.class.getName()),
            null);
        assertField(assertClassHasField(this, FIELD_INDEX_IDENTIFIER),
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(int.class.getName()),
            null);

        assertConstructor(assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE), Modifier.PUBLIC, null, null);

        assertMethod(assertClassHasMethod(this, METHOD_GET_CHAR_SIGNATURE),
            null,
            type -> type.getTypeName().equals(char.class.getName()),
            null);
        assertMethod(assertClassHasMethod(this, METHOD_GET_INDEX_SIGNATURE),
            null,
            type -> type.getTypeName().equals(int.class.getName()),
            null);
    }

    /**
     * Tests for an instance of {@link CharWithIndex}.
     */
    @Test
    @DisplayName("2 | Class instance, fields and getter")
    public void testInstance() {
        Constructor<?> charWithIndexConstructor = getConstructor(CONSTRUCTOR_SIGNATURE);
        boolean exceptionThrown1 = false;
        boolean exceptionThrown2 = false;

        try {
            newInstance(charWithIndexConstructor, 'A', null);
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(NullPointerException.class)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
            exceptionThrown1 = true;
        }
        try {
            newInstance(charWithIndexConstructor, null, (int) 'A');
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(NullPointerException.class)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
            exceptionThrown2 = true;
        }
        assertTrue(exceptionThrown1,
            "Expected a NullPointerException when invoking the constructor of %s with null as first parameter"
                .formatted(className));
        assertTrue(exceptionThrown2,
            "Expected a NullPointerException when invoking the constructor of %s with null as second parameter"
                .formatted(className));

        Object instance = newInstance(charWithIndexConstructor, 'A', (int) 'A');

        Field theChar = getField(FIELD_THE_CHAR_IDENTIFIER);
        Field index = getField(FIELD_INDEX_IDENTIFIER);
        assertFieldValue(theChar, 'A', instance);
        assertFieldValue(index, (int) 'A', instance);

        Method getChar = getMethod(METHOD_GET_CHAR_SIGNATURE);
        Method getIndex = getMethod(METHOD_GET_INDEX_SIGNATURE);
        assertMethodReturnValue(getChar, 'A', instance);
        assertMethodReturnValue(getIndex, (int) 'A', instance);
    }
}
