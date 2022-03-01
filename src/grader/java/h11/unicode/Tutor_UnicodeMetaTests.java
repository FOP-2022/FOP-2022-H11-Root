package h11.unicode;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertMethod;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link UnicodeTests}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_UnicodeMetaTests extends AbstractTestClass implements PreInvocationCheck {

    private static Class<?> unicodeTestsClass = null;
    private static Constructor<?> unicodeTestsConstructor = null;
    private static Method testCharFromUnicode = null;
    private static Method testCharFromUnicodeCasesExchanged = null;

    public final static Map<String, List<Integer>> CONSTRUCTOR_INVOCATION_ARGS = Map.of(
        "h11/unicode/CharFromUnicode", new ArrayList<>(),
        "h11/unicode/CharFromUnicodeCasesExchanged", new ArrayList<>()
    );

    /**
     * Creates a new {@link Tutor_UnicodeMetaTests} object.
     */
    public Tutor_UnicodeMetaTests() {
        super("h11.unicode.UnicodeTests");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
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
    public void metaTest_testCharFromUnicode() {
        Map<Predicate<Integer>, String> invocationCheckList = Map.of(
            i -> isInCharacterRange(i) && Character.isLowerCase((char) (int) i),
            "CharFromUnicode.apply(Integer) was not tested with the code point of a lowercase character",
            i -> isInCharacterRange(i) && Character.isUpperCase((char) (int) i),
            "CharFromUnicode.apply(Integer) was not tested with the code point of a uppercase character",
            i -> isInCharacterRange(i) && !Character.isLowerCase((char) (int) i) && !Character.isUpperCase((char) (int) i),
            "CharFromUnicode.apply(Integer) was not tested with the code point of a character "
                + "that is neither a lowercase nor an uppercase letter",
            i -> i < 0,
            "CharFromUnicode.apply(Integer) was not tested with a negative integer",
            i -> i > Character.MAX_CODE_POINT,
            "CharFromUnicode.apply(Integer) was not tested with an integer bigger than the maximum code point"
        );

        List<Integer> charFromUnicodeApplyArgs = CONSTRUCTOR_INVOCATION_ARGS.get("h11/unicode/CharFromUnicode");
        charFromUnicodeApplyArgs.clear();
        invokeMethod(testCharFromUnicode, newInstance(unicodeTestsConstructor));

        if (charFromUnicodeApplyArgs.size() < 5) {
            throw new AssertionFailedError("CharFromUnicode.apply(Integer) was not called at least 5 times",
                "at least 5 calls", charFromUnicodeApplyArgs.size());
        }
        invocationCheckList.forEach((pred, msg) -> assertTrue(charFromUnicodeApplyArgs.stream().anyMatch(pred), msg));
    }

    /**
     * Tests for {@link UnicodeTests#testCharFromUnicodeCasesExchanged()}.
     */
    @Test
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | testCharFromUnicodeCasesExchanged()")
    public void metaTest_testCharFromUnicodeCasesExchanged() {
        Map<Predicate<Integer>, String> invocationCheckList = Map.of(
            i -> isInCharacterRange(i) && Character.isLowerCase((char) (int) i),
            "CharFromUnicodeCasesExchanged.apply(Integer) was not tested with the code point of a lowercase character",
            i -> isInCharacterRange(i) && Character.isUpperCase((char) (int) i),
            "CharFromUnicodeCasesExchanged.apply(Integer) was not tested with the code point of a uppercase character",
            i -> isInCharacterRange(i) && !Character.isLowerCase((char) (int) i) && !Character.isUpperCase((char) (int) i),
            "CharFromUnicodeCasesExchanged.apply(Integer) was not tested with the code point of a character "
                + "that is neither a lowercase nor an uppercase letter",
            i -> i < 0,
            "CharFromUnicodeCasesExchanged.apply(Integer) was not tested with a negative integer",
            i -> i > Character.MAX_CODE_POINT,
            "CharFromUnicodeCasesExchanged.apply(Integer) was not tested with an integer bigger than the maximum code point"
        );

        List<Integer> charFromUnicodeCasesExchangedApplyArgs = CONSTRUCTOR_INVOCATION_ARGS
            .get("h11/unicode/CharFromUnicodeCasesExchanged");
        charFromUnicodeCasesExchangedApplyArgs.clear();
        invokeMethod(testCharFromUnicodeCasesExchanged, newInstance(unicodeTestsConstructor));

        if (charFromUnicodeCasesExchangedApplyArgs.size() < 5) {
            throw new AssertionFailedError("CharFromUnicodeCasesExchanged.apply(Integer) was not called at least 5 times",
                "at least 5 calls", charFromUnicodeCasesExchangedApplyArgs.size());
        }
        invocationCheckList.forEach((pred, msg) -> assertTrue(charFromUnicodeCasesExchangedApplyArgs.stream().anyMatch(pred), msg));
    }

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

    private static boolean isInCharacterRange(int i) {
        return i >= Character.MIN_VALUE && i <= Character.MAX_VALUE;
    }
}
