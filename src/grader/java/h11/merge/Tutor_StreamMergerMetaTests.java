package h11.merge;

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
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link StreamMergerTest}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_StreamMergerMetaTests extends AbstractTestClass implements PreInvocationCheck {

    private static Class<?> streamMergerTestClass = null;
    private static Constructor<?> streamMergerTestConstructor = null;
    private static Method testMerge = null;

    /**
     * Map that is updated via bytecode transformations of {@link StreamMergerTest#testMerge()}
     * and holds information on whether required methods were invoked.
     */
    public final static Map<String, Boolean> INVOKED_REQUIRED_METHODS = Stream
        .of("of", "generate", "range")
        .collect(Collectors.toMap(s -> s, s -> false));

    /**
     * Creates a new {@link Tutor_StreamMergerMetaTests} object.
     */
    public Tutor_StreamMergerMetaTests() {
        super("h11.merge.StreamMergerTest");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        streamMergerTestClass = assertClassExists(className);

        streamMergerTestConstructor = assertClassHasConstructor(streamMergerTestClass, constructor ->
            constructor.getGenericParameterTypes().length == 0);

        testMerge = assertClassHasMethod(streamMergerTestClass, method ->
            method.getName().equals("testMerge")
                && method.getReturnType().equals(void.class)
                && method.getParameters().length == 0);
        assertAnnotations(testMerge, Test.class);
    }

    /**
     * Test for using {@link java.util.stream.Stream#of(Object[])} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @TestID(2)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("2 | java.util.stream.Stream#of(Object[]) in testMerge()")
    public void testIndex0Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        testMerge.setAccessible(true);
        invokeMethod(testMerge, newInstance(streamMergerTestConstructor));

        assertTrue(INVOKED_REQUIRED_METHODS.get("of"),
            "Stream.of(T...) was not used anywhere in StreamMergerTest.testMerge()");
    }

    /**
     * Test for using {@link java.util.stream.Stream#generate(Supplier)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @TestID(3)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("3 | java.util.stream.Stream#generate(java.util.function.Supplier) in testMerge()")
    public void testIndex1Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        testMerge.setAccessible(true);
        invokeMethod(testMerge, newInstance(streamMergerTestConstructor));

        assertTrue(INVOKED_REQUIRED_METHODS.get("generate"),
            "Stream.generate(Supplier) was not used anywhere in StreamMergerTest.testMerge()");
    }

    /**
     * Test for using {@link java.util.stream.IntStream#range(int, int)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @TestID(4)
    @ExtendWith(PreInvocationCheck.Interceptor.class)
    @DisplayName("4 | java.util.stream.IntStream#range(int, int) in testMerge()")
    public void testIndex2Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        testMerge.setAccessible(true);
        invokeMethod(testMerge, newInstance(streamMergerTestConstructor));

        assertTrue(INVOKED_REQUIRED_METHODS.get("range"),
            "IntStream.range(int, int) was not used anywhere in StreamMergerTest.testMerge()");
    }

    @Override
    public void check(int testID) {
        assumeTrue(streamMergerTestClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(streamMergerTestConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(testMerge != null, "Method %s#testMerge() could not be found".formatted(className));
    }
}
