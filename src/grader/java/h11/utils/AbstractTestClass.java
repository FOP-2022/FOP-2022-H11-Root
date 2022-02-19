package h11.utils;

import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An abstract test class with some utility methods to reduce redundancy.
 */
public abstract class AbstractTestClass {

    protected final String className;

    /**
     * Creates a new abstract test class for {@code className}.
     *
     * @param className the fully qualified name of the class that will be tested
     */
    protected AbstractTestClass(String className) {
        this.className = className;
    }

    /**
     * Creates a new instance using {@code constructor} and {@code params} as
     * parameters to pass to the constructor.
     *
     * @param constructor the constructor to use
     * @param params      parameters to pass to the constructor when instantiating
     * @return a new instance of the class {@code constructor} belongs to
     * @throws AssertionFailedError if any exception is thrown during the invocation of {@code constructor}
     */
    protected Object newInstance(Constructor<?> constructor, Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException e) {
            throw new AssertionFailedError("Could not create instance of " + className, e);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Could not access constructor of class " + className, e);
        } catch (InvocationTargetException e) {
            throw new AssertionFailedError("An exception occurred while instantiating " + className,
                e.getCause());
        }
    }

    /**
     * Invokes {@code method} with {@code params} as parameters and returns the
     * value returned by the invoked method.
     *
     * @param method   the method to invoke
     * @param instance the context, i.e. instance of a class with this field
     * @param params   the parameters to pass to {@code method}
     * @param <T>      the return type
     * @return the value returned by invoking {@code method}
     * @throws AssertionFailedError if any exception is thrown during the invocation of {@code method}
     */
    protected <T> T invokeMethod(Method method, Object instance, Object... params) {
        try {
            return (T) method.invoke(instance, params);
        } catch (InvocationTargetException e) {
            throw new AssertionFailedError(
                "An exception occurred while invoking method %s".formatted(method.getName()),
                e.getCause());
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Could not access method %s".formatted(method.getName()), e);
        }
    }

    /**
     * Returns the value of {@code field} in the context of {@code instance}.
     *
     * @param field    the field
     * @param instance the context, i.e. instance of a class with this field
     * @param <T>      the return type
     * @return the value of {@code field}
     * @throws AssertionFailedError if the field is not accessible
     */
    protected <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Could not access field %s".formatted(field.getName()), e);
        }
    }
}
