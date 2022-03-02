package h11.utils;

import kotlin.Pair;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An abstract test class with some utility methods to reduce redundancy.
 */
public abstract class AbstractTestClass {

    public final String className;
    public final Class<?> clazz;
    public final Map<String, Constructor<?>> constructorMap;
    public final Map<String, Field> fieldMap;
    public final Map<String, Method> methodMap;

    /**
     * Creates a new abstract test class for {@code className}.
     *
     * @param className the fully qualified name of the class that will be tested
     */
    protected AbstractTestClass(String className) {
        this(className, Map.of(), Map.of(), Map.of());
    }

    /**
     * Creates a new abstract test class for {@code className}.
     *
     * @param className             the fully qualified name of the class that will be tested
     * @param constructorPredicates a mapping of signatures to predicates that match a constructor
     *                              with that specific signature
     */
    protected AbstractTestClass(String className, Map<String, Predicate<Constructor<?>>> constructorPredicates) {
        this(className, constructorPredicates, Map.of(), Map.of());
    }

    /**
     * Creates a new abstract test class for {@code className}.
     *
     * @param className             the fully qualified name of the class that will be tested
     * @param constructorPredicates a mapping of signatures to predicates that match a constructor
     *                              with that specific signature
     * @param methodPredicates      a mapping of signatures to predicates that match a method
     *                              with that specific signature
     */
    protected AbstractTestClass(String className,
                                Map<String, Predicate<Constructor<?>>> constructorPredicates,
                                Map<String, Predicate<Method>> methodPredicates) {
        this(className, constructorPredicates, Map.of(), methodPredicates);
    }

    /**
     * Creates a new abstract test class for {@code className}.
     *
     * @param className             the fully qualified name of the class that will be tested
     * @param constructorPredicates a mapping of signatures to predicates that match a constructor
     *                              with that specific signature
     * @param fieldPredicates       a mapping of identifiers to predicates that match a field with
     *                              that specific identifier
     * @param methodPredicates      a mapping of signatures to predicates that match a method
     *                              with that specific signature
     */
    protected AbstractTestClass(String className,
                                Map<String, Predicate<Constructor<?>>> constructorPredicates,
                                Map<String, Predicate<Field>> fieldPredicates,
                                Map<String, Predicate<Method>> methodPredicates) {
        this.className = className;
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new TestAbortedException("Class %s could not be found".formatted(className), e);
        }
        this.constructorMap = mapPredicates(constructorPredicates, clazz.getConstructors(), clazz.getDeclaredConstructors());
        this.fieldMap = mapPredicates(fieldPredicates, clazz.getFields(), clazz.getDeclaredFields());
        this.methodMap = mapPredicates(methodPredicates, clazz.getMethods(), clazz.getDeclaredMethods());
    }

    /**
     * Returns the {@link Constructor} object for a given signature if it has been previously matched during instantiation.
     *
     * @param signature the signature of the constructor
     * @return the {@link Constructor} object corresponding to {@code signature}
     * @throws TestAbortedException if {@link #constructorMap} does not have a mapping for {@code signature}
     */
    public Constructor<?> getConstructor(String signature) {
        if (constructorMap.containsKey(signature)) {
            return constructorMap.get(signature);
        } else {
            throw new TestAbortedException("No constructor with signature %s was found".formatted(signature));
        }
    }

    /**
     * Returns the {@link Field} object for a given identifier if it has been previously matched during instantiation.
     *
     * @param identifier the identifier of the field
     * @return the {@link Field} object corresponding to {@code identifier}
     * @throws TestAbortedException if {@link #fieldMap} does not have a mapping for {@code identifier}
     */
    public Field getField(String identifier) {
        if (fieldMap.containsKey(identifier)) {
            return fieldMap.get(identifier);
        } else {
            throw new TestAbortedException("No field with identifier %s was found".formatted(identifier));
        }
    }

    /**
     * Returns the {@link Method} object for a given signature if it has been previously matched during instantiation.
     *
     * @param signature the signature of the method
     * @return the {@link Method} object corresponding to {@code signature}
     * @throws TestAbortedException if {@link #methodMap} does not have a mapping for {@code signature}
     */
    public Method getMethod(String signature) {
        if (methodMap.containsKey(signature)) {
            return methodMap.get(signature);
        } else {
            throw new TestAbortedException("No method with signature %s was found".formatted(signature));
        }
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
    public Object newInstance(Constructor<?> constructor, Object... params) {
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
    public <T> T invokeMethod(Method method, Object instance, Object... params) {
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
    public <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Could not access field %s".formatted(field.getName()), e);
        }
    }

    protected static <T extends Executable> Predicate<T> predicateFromSignature(String signature) {
        Matcher matcher = Pattern.compile("(?<name>[\\w.]+)\\((?<parameters>[\\w., <>\\[\\]]*)\\)").matcher(signature);
        matcher.matches(); // what the hell, Java? Why???
        String executableName = matcher.group("name");
        String[] executableParameters = matcher.group("parameters").split(",");
        return t -> t.getName().equals(executableName)
            && Arrays.equals(
                Arrays.stream(t.getGenericParameterTypes()).map(Type::getTypeName).toArray(String[]::new),
                Arrays.stream(executableParameters).map(String::trim).filter(s -> s.length() != 0).toArray(String[]::new)
            );
    }

    @SafeVarargs
    private static <T extends AccessibleObject> Map<String, T> mapPredicates(Map<String, Predicate<T>> predicateMap, T[]... ts) {
        return Arrays.stream(ts)
            .flatMap(Arrays::stream)
            // filter synthetic members such as bridge methods
            .filter(t -> !(t instanceof Member member) || !member.isSynthetic())
            .distinct()
            .map(t -> {
                for (Map.Entry<String, Predicate<T>> entry : predicateMap.entrySet()) {
                    if (entry.getValue().test(t)) {
                        return new Pair<>(entry.getKey(), t);
                    }
                }
                return null;
            })
            .filter(Objects::nonNull)
            .peek(pair -> pair.getSecond().setAccessible(true))
            .collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
    }
}
