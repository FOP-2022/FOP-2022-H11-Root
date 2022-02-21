package h11.unicode;

import h11.utils.AbstractTestClass;
import h11.utils.PreInvocationCheck;
import h11.utils.TestID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.opentest4j.TestAbortedException;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Map<Predicate<Integer>, Boolean> invocationCheckList = Stream.<Predicate<Integer>>of(
                i -> isInCharacterRange(i) && Character.isLowerCase((char) (int) i),
                i -> isInCharacterRange(i) && Character.isUpperCase((char) (int) i),
                i -> isInCharacterRange(i) && !Character.isLowerCase((char) (int) i) && !Character.isUpperCase((char) (int) i),
                i -> i < 0,
                i -> i > Character.MAX_CODE_POINT
            )
            .collect(Collectors.toMap(pred -> pred, pred -> false));

        Class<?> charFromUnicodeClass = getCharFromUnicodeClass();
        Method apply = getApplyMethod(charFromUnicodeClass);
        MockedConstruction<?> construction = Mockito.mockConstructionWithAnswer(charFromUnicodeClass, invocation -> {
            if (invocation.getMethod().equals(apply)) {
                for (Predicate<Integer> predicate : invocationCheckList.keySet()) {
                    if (predicate.test(invocation.getArgument(0))) {
                        invocationCheckList.put(predicate, true);
                    }
                }
            }

            return invocation.callRealMethod();
        });
        invokeMethod(testCharFromUnicode, newInstance(unicodeTestsConstructor));

        assertTrue(invocationCheckList.values().stream().allMatch(Boolean::booleanValue),
            "%s#apply(java.lang.Integer) was not invoked with all required parameters"
                .formatted(charFromUnicodeClass.getName()));

        construction.closeOnDemand();
    }

    /**
     * Tests for {@link UnicodeTests#testCharFromUnicodeCasesExchanged()}.
     */
    @Test
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | testCharFromUnicodeCasesExchanged()")
    public void metaTest_testCharFromUnicodeCasesExchanged() {
        Map<Predicate<Integer>, Boolean> invocationCheckList = Stream.<Predicate<Integer>>of(
                i -> isInCharacterRange(i) && Character.isLowerCase((char) (int) i),
                i -> isInCharacterRange(i) && Character.isUpperCase((char) (int) i),
                i -> isInCharacterRange(i) && !Character.isLowerCase((char) (int) i) && !Character.isUpperCase((char) (int) i),
                i -> i < 0,
                i -> i > Character.MAX_CODE_POINT
            )
            .collect(Collectors.toMap(pred -> pred, pred -> false));

        Class<?> charFromUnicodeCasesExchangedClass = getCharFromUnicodeCasesExchangedClass();
        Method apply = getApplyMethod(charFromUnicodeCasesExchangedClass);
        MockedConstruction<?> construction = Mockito.mockConstructionWithAnswer(
            charFromUnicodeCasesExchangedClass,
            invocation -> {
                if (invocation.getMethod().equals(apply)) {
                    for (Predicate<Integer> predicate : invocationCheckList.keySet()) {
                        if (predicate.test(invocation.getArgument(0))) {
                            invocationCheckList.put(predicate, true);
                        }
                    }
                }

                return invocation.callRealMethod();
            }
        );
        invokeMethod(testCharFromUnicodeCasesExchanged, newInstance(unicodeTestsConstructor));

        assertTrue(invocationCheckList.values().stream().allMatch(Boolean::booleanValue),
            "%s#apply(java.lang.Integer) was not invoked with all required parameters"
                .formatted(charFromUnicodeCasesExchangedClass.getName()));

        construction.closeOnDemand();
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

    private static Class<?> getCharFromUnicodeClass() {
        try {
            return Class.forName("h11.unicode.CharFromUnicode");
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class h11.unicode.CharFromUnicode could not be found", e);
        }
    }

    private static Class<?> getCharFromUnicodeCasesExchangedClass() {
        try {
            return Class.forName("h11.unicode.CharFromUnicodeCasesExchanged");
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class h11.unicode.CharFromUnicodeCasesExchanged could not be found", e);
        }
    }

    private static Method getApplyMethod(Class<?> clazz) {
        return Stream
            .concat(Arrays.stream(clazz.getMethods()), Arrays.stream(clazz.getDeclaredMethods()))
            .filter(method -> method.getName().equals("apply")
                && method.getReturnType().equals(Character.class)
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0].equals(Integer.class))
            .findFirst()
            .orElseThrow();
    }

    private static boolean isInCharacterRange(int i) {
        return i >= Character.MIN_VALUE && i <= Character.MAX_VALUE;
    }
}
