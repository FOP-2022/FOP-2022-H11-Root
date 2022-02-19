package h11.unicode;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertMethod;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link UnicodeTests}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UnicodeMetaTests extends AbstractTestClass implements PreInvocationCheck {

    private static Class<?> unicodeTestsClass = null;
    private static Constructor<?> unicodeTestsConstructor = null;
    private static Method testCharFromUnicode = null;
    private static Method testCharFromUnicodeCasesExchanged = null;

    /**
     * Creates a new {@link UnicodeMetaTests} object.
     */
    public UnicodeMetaTests() {
        super("h11.unicode.UnicodeTests");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        unicodeTestsClass = assertClassExists(className);

        unicodeTestsConstructor = assertClassHasConstructor(unicodeTestsClass, constructor ->
            constructor.getGenericParameterTypes().length == 0);

        testCharFromUnicode = assertClassHasMethod(unicodeTestsClass, "testCharFromUnicode");
        assertAnnotations(testCharFromUnicode, Test.class);
        assertMethod(
            testCharFromUnicode,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );

        testCharFromUnicodeCasesExchanged = assertClassHasMethod(unicodeTestsClass, "testCharFromUnicodeCasesExchanged");
        assertAnnotations(testCharFromUnicodeCasesExchanged, Test.class);
        assertMethod(
            testCharFromUnicodeCasesExchanged,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );
    }

    /**
     * Tests for {@link UnicodeTests#testCharFromUnicode()}.
     */
    @Test
    @TestID(2)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("2 | testCharFromUnicode()")
    void testBuildIntegerArray() {
        Object instance = newInstance(unicodeTestsConstructor);


    }

    /**
     * Tests for {@link UnicodeTests#testCharFromUnicodeCasesExchanged()}.
     */
    @Test
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | testCharFromUnicodeCasesExchanged()")
    void testBuildIntegerList() {
        Object instance = newInstance(unicodeTestsConstructor);


    }

    // TODO: implement tests for testCharFromUnicode and testCharFromUnicodeCasesExchanged

    @Override
    public void check(int testID) {
        assumeTrue(unicodeTestsClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(unicodeTestsConstructor != null,
            "Constructor for class %s could not be found".formatted(className));

        switch (testID) {
            case 2 ->
                assumeTrue(testCharFromUnicode != null,
                    "Method %s#testCharFromUnicode() could not be found".formatted(className));

            case 3 ->
                assumeTrue(testCharFromUnicodeCasesExchanged != null,
                    "Method %s#testCharFromUnicodeCasesExchanged() could not be found".formatted(className));

            // Checkstyle doesn't like switches without default branch so here's a no-op, I guess
            default -> assumeTrue(
                ((1 / 3) << 5 & 67 | -4 ^ 0b10 >> 4 / 3 * 2 % (-'☕' << "(͡° ͜ʖ ͡°)".chars().sum() >>> (0xb7c2))) == -4
            );
        }
    }
}
