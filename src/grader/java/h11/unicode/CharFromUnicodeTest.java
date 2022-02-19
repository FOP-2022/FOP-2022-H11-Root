package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Predicate;

import static h11.utils.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CharFromUnicode}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CharFromUnicodeTest extends AbstractTestClass {

    private static Class<?> charFromUnicodeClass = null;
    private static Constructor<?> charFromUnicodeConstructor = null;
    private static Method apply = null;

    /**
     * Creates a new {@link CharFromUnicodeTest} object.
     */
    public CharFromUnicodeTest() {
        super("h11.unicode.CharFromUnicode");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        charFromUnicodeClass = assertClassExists(className);
        assertClassHasExactModifiers(charFromUnicodeClass, Modifier.PUBLIC);
        assertClassImplements(charFromUnicodeClass,
            "%s<%s, %s>".formatted(Function.class.getName(), Integer.class.getName(), Character.class.getName()));

        charFromUnicodeConstructor = assertClassHasConstructor(charFromUnicodeClass, constructor ->
            constructor.getParameters().length == 0);

        apply = assertClassHasMethod(charFromUnicodeClass, method ->
            method.getName().equals("apply")
                && method.getReturnType().equals(Character.class)
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0].equals(Integer.class));
        assertMethod(apply,
            Modifier.PUBLIC,
            null,
            null,
            (Predicate<Type>) null);
    }

    /**
     * Tests for {@link CharFromUnicode#apply(Integer)}.
     */
    @Test
    @DisplayName("2 | apply(java.lang.Integer)")
    void testApply() {
        assumeTrue(charFromUnicodeClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(charFromUnicodeConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(apply != null,
            "Method %s#apply(java.lang.Integer) could not be found".formatted(className));

        Class<?> formatExceptionClass = FormatExceptionTest.getFormatExceptionClass();
        Object instance = newInstance(charFromUnicodeConstructor);

        try {
            invokeMethod(apply, instance, (Object) null);
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(NullPointerException.class)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
        }
        try {
            invokeMethod(apply, instance, Character.MIN_VALUE - 1);
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(formatExceptionClass)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
        }
        try {
            invokeMethod(apply, instance, Character.MAX_VALUE + 1);
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(formatExceptionClass)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
        }

        assertEquals(Character.MIN_VALUE, (Character) invokeMethod(apply, instance, (int) Character.MIN_VALUE));
        assertEquals((char) (Character.MAX_VALUE / 2), (Character) invokeMethod(apply, instance, (int) Character.MAX_VALUE / 2));
        assertEquals(Character.MAX_VALUE, (Character) invokeMethod(apply, instance, (int) Character.MAX_VALUE));
    }

    // TODO: add check for unsafe operations
}
