package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.TestAbortedException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassExtends;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for {@link FormatException}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class FormatExceptionTests extends AbstractTestClass {

    private static Class<?> formatExceptionClass = null;
    private static Constructor<?> formatExceptionConstructor = null;

    /**
     * Creates a new {@link FormatExceptionTests} object.
     */
    public FormatExceptionTests() {
        super("h11.unicode.FormatException");
    }

    /**
     * Tests for correctness of class and constructor.
     */
    @Test
    @DisplayName("1 | Class and constructor definitions")
    void testDefinitions() {
        formatExceptionClass = assertClassExists(className);
        assertClassHasExactModifiers(formatExceptionClass, Modifier.PUBLIC);
        assertClassExtends(formatExceptionClass, type -> type.getTypeName().equals(RuntimeException.class.getName()));

        formatExceptionConstructor = assertClassHasConstructor(formatExceptionClass, constructor -> {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            return parameterTypes.length == 1 && parameterTypes[0].getTypeName().equals(int.class.getName());
        });
        assertConstructor(formatExceptionConstructor, Modifier.PUBLIC, (Predicate<Type>) null);
    }

    /**
     * Tests for successfully setting the exception message, depending on the constructors first parameter.
     */
    @Test
    @DisplayName("2 | Class instance")
    void testInstance() {
        assumeTrue(formatExceptionClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(formatExceptionConstructor != null,
            "Constructor for class %s could not be found".formatted(className));

        Map<Integer, String> parameterMapping = Stream
            .of(Character.MAX_CODE_POINT + 1,
                Integer.MAX_VALUE,
                -1,
                Integer.MIN_VALUE,
                Character.MAX_VALUE + 1,
                Character.MAX_CODE_POINT - 1)
            .collect(Collectors.toMap(i -> i, FormatExceptionTests::getErrorMessage));

        for (Map.Entry<Integer, String> entry : parameterMapping.entrySet()) {
            assertEquals(entry.getValue(),
                ((Exception) newInstance(formatExceptionConstructor, entry.getKey())).getMessage(),
                "Actual exception message differs from expected one");
        }
    }

    private static String getErrorMessage(int i) {
        if (i > Character.MAX_CODE_POINT) {
            return "%d exceeds 0x10FFFF and is not a valid Unicode code point".formatted(i);
        } else if (i > Character.MAX_VALUE) {
            return "%d exceeds 0xFFFF and cannot be represented by Character".formatted(i);
        } else {
            return "%d is a negative number and therefore not a valid code point".formatted(i);
        }
    }

    /**
     * Returns the {@link Class} object for {@link FormatException}.
     *
     * @return the {@link Class} object
     */
    public static Class<?> getFormatExceptionClass() {
        try {
            return Class.forName("h11.unicode.FormatException");
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class h11.unicode.FormatException could not be found", e);
        }
    }
}
