package h11.merge;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;

/**
 * Tests for class {@link StreamMergerTest}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class StreamMergerMetaTests extends AbstractTestClass {

    private static Class<?> streamMergerTestClass = null;
    private static Constructor<?> streamMergerTestConstructor = null;
    private static Method testMerge = null;

    /**
     * Creates a new {@link StreamMergerMetaTests} object.
     */
    public StreamMergerMetaTests() {
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
    @DisplayName("2 | java.util.stream.Stream#of(Object[]) in testMerge()")
    public void testIndex0Correct() {

    }

    /**
     * Test for using {@link java.util.stream.Stream#generate(Supplier)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @DisplayName("3 | java.util.stream.Stream#generate(java.util.function.Supplier) in testMerge()")
    public void testIndex1Correct() {

    }

    /**
     * Test for using {@link java.util.stream.IntStream#range(int, int)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @DisplayName("4 | java.util.stream.IntStream#range(int, int) in testMerge()")
    public void testIndex2Correct() {

    }
}
