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

import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertClassImplements;
import static h11.utils.Assertions.assertEquals;
import static h11.utils.Assertions.assertMethod;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CharFromUnicodeCasesExchanged}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CharFromUnicodeCasesExchangedTest extends AbstractTestClass {

    private static Class<?> charFromUnicodeCasesExchangedClass = null;
    private static Constructor<?> charFromUnicodeCasesExchangedConstructor = null;
    private static Method apply = null;

    /**
     * Creates a new {@link CharFromUnicodeCasesExchangedTest} object.
     */
    public CharFromUnicodeCasesExchangedTest() {
        super("h11.unicode.CharFromUnicodeCasesExchanged");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        charFromUnicodeCasesExchangedClass = assertClassExists(className);
        assertClassHasExactModifiers(charFromUnicodeCasesExchangedClass, Modifier.PUBLIC);
        assertClassImplements(charFromUnicodeCasesExchangedClass,
            "%s<%s, %s>".formatted(Function.class.getName(), Integer.class.getName(), Character.class.getName()));

        charFromUnicodeCasesExchangedConstructor = assertClassHasConstructor(charFromUnicodeCasesExchangedClass,
            constructor -> constructor.getParameters().length == 0);

        apply = assertClassHasMethod(charFromUnicodeCasesExchangedClass, method ->
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
     * Tests for {@link CharFromUnicodeCasesExchanged#apply(Integer)}.
     */
    @Test
    @DisplayName("2 | apply(java.lang.Integer)")
    void testApply() {
        assumeTrue(charFromUnicodeCasesExchangedClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(charFromUnicodeCasesExchangedConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(apply != null,
            "Method %s#apply(java.lang.Integer) could not be found".formatted(className));

        Class<?> formatExceptionClass = FormatExceptionTest.getFormatExceptionClass();
        Object instance = newInstance(charFromUnicodeCasesExchangedConstructor);

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

        assertEquals('A', (Character) invokeMethod(apply, instance, (int) 'a'));
        assertEquals('M', (Character) invokeMethod(apply, instance, (int) 'm'));
        assertEquals('Z', (Character) invokeMethod(apply, instance, (int) 'z'));
    }

    // TODO: add check for unsafe operations
}
