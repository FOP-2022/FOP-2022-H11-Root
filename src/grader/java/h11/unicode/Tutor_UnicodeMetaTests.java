package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertMethod;
import static h11.utils.Assertions.assertTrue;

/**
 * Tests for class {@link UnicodeTests}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_UnicodeMetaTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "UnicodeTests()";
    public static final String METHOD_TEST_CHAR_FROM_UNICODE_SIGNATURE = "testCharFromUnicode()";
    public static final String METHOD_TEST_CHAR_FROM_UNICODE_CASES_EXCHANGED_SIGNATURE = "testCharFromUnicodeCasesExchanged()";

    public static final Map<String, List<Integer>> CONSTRUCTOR_INVOCATION_ARGS = Map.of(
        "h11/unicode/CharFromUnicode", new ArrayList<>(),
        "h11/unicode/CharFromUnicodeCasesExchanged", new ArrayList<>()
    );

    /**
     * Creates a new {@link Tutor_UnicodeMetaTests} object.
     */
    public Tutor_UnicodeMetaTests() {
        super(
            "h11.unicode.UnicodeTests",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.unicode." + CONSTRUCTOR_SIGNATURE)),
            Map.of(
                METHOD_TEST_CHAR_FROM_UNICODE_SIGNATURE,
                predicateFromSignature(METHOD_TEST_CHAR_FROM_UNICODE_SIGNATURE),
                METHOD_TEST_CHAR_FROM_UNICODE_CASES_EXCHANGED_SIGNATURE,
                predicateFromSignature(METHOD_TEST_CHAR_FROM_UNICODE_CASES_EXCHANGED_SIGNATURE)
            )
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE);

        Method testCharFromUnicode = assertClassHasMethod(this, METHOD_TEST_CHAR_FROM_UNICODE_SIGNATURE);
        assertAnnotations(testCharFromUnicode, Test.class);
        assertMethod(
            testCharFromUnicode,
            Modifier.PUBLIC,
            type -> type.getTypeName().equals(void.class.getName()),
            null
        );

        Method testCharFromUnicodeCasesExchanged = assertClassHasMethod(this,
            METHOD_TEST_CHAR_FROM_UNICODE_CASES_EXCHANGED_SIGNATURE);
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
    @DisplayName("2 | testCharFromUnicode()")
    public void metaTest_testCharFromUnicode() {
        List<Integer> charFromUnicodeApplyArgs = CONSTRUCTOR_INVOCATION_ARGS.get("h11/unicode/CharFromUnicode");
        charFromUnicodeApplyArgs.clear();
        invokeMethod(getMethod(METHOD_TEST_CHAR_FROM_UNICODE_SIGNATURE), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        if (charFromUnicodeApplyArgs.size() < 5) {
            throw new AssertionFailedError("CharFromUnicode.apply(Integer) was not called at least 5 times",
                "at least 5 calls", charFromUnicodeApplyArgs.size());
        }
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
        invocationCheckList.forEach((pred, msg) -> assertTrue(charFromUnicodeApplyArgs.stream().anyMatch(pred), msg));
    }

    /**
     * Tests for {@link UnicodeTests#testCharFromUnicodeCasesExchanged()}.
     */
    @Test
    @DisplayName("3 | testCharFromUnicodeCasesExchanged()")
    public void metaTest_testCharFromUnicodeCasesExchanged() {
        List<Integer> charFromUnicodeCasesExchangedApplyArgs = CONSTRUCTOR_INVOCATION_ARGS
            .get("h11/unicode/CharFromUnicodeCasesExchanged");
        charFromUnicodeCasesExchangedApplyArgs.clear();
        invokeMethod(getMethod(METHOD_TEST_CHAR_FROM_UNICODE_CASES_EXCHANGED_SIGNATURE),
            newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        if (charFromUnicodeCasesExchangedApplyArgs.size() < 5) {
            throw new AssertionFailedError("CharFromUnicodeCasesExchanged.apply(Integer) was not called at least 5 times",
                "at least 5 calls", charFromUnicodeCasesExchangedApplyArgs.size());
        }
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
        invocationCheckList.forEach((pred, msg) ->
            assertTrue(charFromUnicodeCasesExchangedApplyArgs.stream().anyMatch(pred), msg));
    }

    private static boolean isInCharacterRange(int i) {
        return i >= Character.MIN_VALUE && i <= Character.MAX_VALUE;
    }
}
