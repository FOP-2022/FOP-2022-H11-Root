package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertClassExtends;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasModifiers;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertEquals;

/**
 * Tests for {@link FormatException}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_FormatExceptionTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "FormatException(int)";

    /**
     * Creates a new {@link Tutor_FormatExceptionTests} object.
     */
    public Tutor_FormatExceptionTests() {
        super(
            "h11.unicode.FormatException",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.unicode." + CONSTRUCTOR_SIGNATURE))
        );
    }

    /**
     * Tests for correctness of class and constructor.
     */
    @Test
    @DisplayName("1 | Class and constructor definitions")
    public void testDefinitions() {
        assertClassHasModifiers(clazz, Modifier.PUBLIC);
        assertClassExtends(clazz, type -> type.getTypeName().equals(RuntimeException.class.getName()));

        assertConstructor(assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE), Modifier.PUBLIC, (Predicate<Type>) null);
    }

    /**
     * Tests for successfully setting the exception message, depending on the constructors first parameter.
     */
    @Test
    @DisplayName("2 | Class instance")
    public void testInstance() {
        Map<Integer, String> parameterMapping = Stream
            .of(Character.MAX_CODE_POINT + 1,
                Integer.MAX_VALUE,
                -1,
                Integer.MIN_VALUE,
                Character.MAX_VALUE + 1,
                Character.MAX_CODE_POINT - 1)
            .collect(Collectors.toMap(i -> i, Tutor_FormatExceptionTests::getErrorMessage));

        for (Map.Entry<Integer, String> entry : parameterMapping.entrySet()) {
            assertEquals(entry.getValue(),
                ((Exception) newInstance(getConstructor(CONSTRUCTOR_SIGNATURE), entry.getKey())).getMessage(),
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
}
