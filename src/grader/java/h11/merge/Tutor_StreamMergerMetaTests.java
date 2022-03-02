package h11.merge;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static h11.utils.Assertions.assertAnnotations;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertMethod;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for class {@link StreamMergerTest}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class Tutor_StreamMergerMetaTests extends AbstractTestClass {

    public static final String CONSTRUCTOR_SIGNATURE = "StreamMergerTest()";
    public static final String METHOD_TEST_MERGE_SIGNATURE = "testMerge()";

    /**
     * Map that is updated via bytecode transformations of {@link StreamMergerTest#testMerge()}
     * and holds information on whether required methods were invoked.
     */
    public static final Map<String, Boolean> INVOKED_REQUIRED_METHODS = Stream
        .of("of", "generate", "range")
        .collect(Collectors.toMap(s -> s, s -> false));

    /**
     * Creates a new {@link Tutor_StreamMergerMetaTests} object.
     */
    public Tutor_StreamMergerMetaTests() {
        super(
            "h11.merge.StreamMergerTest",
            Map.of(CONSTRUCTOR_SIGNATURE, predicateFromSignature("h11.merge." + CONSTRUCTOR_SIGNATURE)),
            Map.of(METHOD_TEST_MERGE_SIGNATURE, predicateFromSignature(METHOD_TEST_MERGE_SIGNATURE))
        );
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    public void testDefinitions() {
        assertClassHasConstructor(this, CONSTRUCTOR_SIGNATURE);

        Method testMerge = assertClassHasMethod(this, METHOD_TEST_MERGE_SIGNATURE);
        assertMethod(testMerge, null, type -> type.getTypeName().equals(void.class.getName()), null);
        assertAnnotations(testMerge, Test.class);
    }

    /**
     * Test for using {@link java.util.stream.Stream#of(Object[])} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @DisplayName("2 | Stream#of(Object[]) in testMerge()")
    public void testIndex0Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        invokeMethod(getMethod(METHOD_TEST_MERGE_SIGNATURE), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        assertTrue(INVOKED_REQUIRED_METHODS.get("of"),
            "Stream.of(T...) was not used anywhere in StreamMergerTest.testMerge()");
    }

    /**
     * Test for using {@link java.util.stream.Stream#generate(Supplier)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @DisplayName("3 | Stream#generate(Supplier) in testMerge()")
    public void testIndex1Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        invokeMethod(getMethod(METHOD_TEST_MERGE_SIGNATURE), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        assertTrue(INVOKED_REQUIRED_METHODS.get("generate"),
            "Stream.generate(Supplier) was not used anywhere in StreamMergerTest.testMerge()");
    }

    /**
     * Test for using {@link java.util.stream.IntStream#range(int, int)} in {@link StreamMergerTest#testMerge()}.
     */
    @Test
    @DisplayName("4 | IntStream#range(int, int) in testMerge()")
    public void testIndex2Correct() {
        INVOKED_REQUIRED_METHODS.replaceAll((key, value) -> false);
        invokeMethod(getMethod(METHOD_TEST_MERGE_SIGNATURE), newInstance(getConstructor(CONSTRUCTOR_SIGNATURE)));

        assertTrue(INVOKED_REQUIRED_METHODS.get("range"),
            "IntStream.range(int, int) was not used anywhere in StreamMergerTest.testMerge()");
    }
}
