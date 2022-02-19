package h11.unicode;

import h11.utils.AbstractTestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import static h11.utils.Assertions.assertClassExists;
import static h11.utils.Assertions.assertClassHasConstructor;
import static h11.utils.Assertions.assertClassHasExactModifiers;
import static h11.utils.Assertions.assertClassHasField;
import static h11.utils.Assertions.assertClassHasMethod;
import static h11.utils.Assertions.assertConstructor;
import static h11.utils.Assertions.assertField;
import static h11.utils.Assertions.assertFieldValue;
import static h11.utils.Assertions.assertMethodReturnValue;
import static h11.utils.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for class {@link CharWithIndex}.
 */
@TestForSubmission("h11")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CharWithIndexTests extends AbstractTestClass {

    private static Class<?> charWithIndexClass = null;
    private static Field theChar = null;
    private static Field index = null;
    private static Constructor<?> charWithIndexConstructor = null;
    private static Method getChar = null;
    private static Method getIndex = null;

    /**
     * Creates a new {@link CharWithIndexTests} object.
     */
    public CharWithIndexTests() {
        super("h11.unicode.CharWithIndex");
    }

    /**
     * Tests for correctness of class, constructor and method definitions.
     */
    @Test
    @DisplayName("1 | Class, constructor and method definitions")
    void testDefinitions() {
        charWithIndexClass = assertClassExists(className);
        assertClassHasExactModifiers(charWithIndexClass, Modifier.PUBLIC);

        theChar = assertClassHasField(charWithIndexClass, "theChar");
        assertField(theChar,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(char.class.getName()),
            null);

        index = assertClassHasField(charWithIndexClass, "index");
        assertField(index,
            Modifier.PRIVATE | Modifier.FINAL,
            type -> type.getTypeName().equals(int.class.getName()),
            null);

        charWithIndexConstructor = assertClassHasConstructor(charWithIndexClass, constructor -> {
            Type[] parameters = constructor.getGenericParameterTypes();

            return parameters.length == 2
                && parameters[0].getTypeName().equals(Character.class.getName())
                && parameters[1].getTypeName().equals(Integer.class.getName());
        });
        assertConstructor(charWithIndexConstructor, Modifier.PUBLIC, null, null);

        getChar = assertClassHasMethod(charWithIndexClass, method ->
            method.getName().equals("getChar")
                && method.getReturnType().equals(char.class)
                && method.getParameterTypes().length == 0);

        getIndex = assertClassHasMethod(charWithIndexClass, method ->
            method.getName().equals("getIndex")
                && method.getReturnType().equals(int.class)
                && method.getParameterTypes().length == 0);
    }

    /**
     * Tests for an instance of {@link CharWithIndex}.
     */
    @Test
    @DisplayName("2 | Class instance, fields and getter")
    void testApply() {
        assumeTrue(charWithIndexClass != null, "Class %s could not be found".formatted(className));
        assumeTrue(theChar != null, "Field %s#theChar could not be found".formatted(className));
        assumeTrue(index != null, "Field %s#index could not be found".formatted(className));
        assumeTrue(charWithIndexConstructor != null,
            "Constructor for class %s could not be found".formatted(className));
        assumeTrue(getChar != null,
            "Method %s#getChar() could not be found".formatted(className));
        assumeTrue(getIndex != null,
            "Method %s#getIndex() could not be found".formatted(className));

        boolean exceptionThrown1 = false;
        boolean exceptionThrown2 = false;

        try {
            newInstance(charWithIndexConstructor, 'A', null);
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(NullPointerException.class)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
            exceptionThrown1 = true;
        }
        try {
            newInstance(charWithIndexConstructor, null, (int) 'A');
        } catch (AssertionFailedError e) {
            if (!e.getCause().getClass().equals(NullPointerException.class)) {
                throw new AssertionFailedError("An unexpected exception was thrown", e);
            }
            exceptionThrown2 = true;
        }
        assertTrue(exceptionThrown1,
            "Expected a NullPointerException when invoking the constructor of %s with null as first parameter"
                .formatted(className));
        assertTrue(exceptionThrown2,
            "Expected a NullPointerException when invoking the constructor of %s with null as second parameter"
                .formatted(className));

        Object instance = newInstance(charWithIndexConstructor, 'A', (int) 'A');

        assertFieldValue(theChar, 'A', instance);
        assertFieldValue(index, (int) 'A', instance);

        assertMethodReturnValue(getChar, 'A', instance);
        assertMethodReturnValue(getIndex, (int) 'A', instance);
    }
}
