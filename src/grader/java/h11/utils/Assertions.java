package h11.utils;

import org.jetbrains.annotations.Nullable;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Extensions for JUnit's {@link org.junit.jupiter.api.Assertions} class.
 * This class contains assertions for classes, fields and methods using Java reflections.
 */
public class Assertions extends org.junit.jupiter.api.Assertions {

    /**
     * A mapping of modifiers in {@link Modifier} to their actual name.
     */
    private static final Map<Integer, String> MODIFIER_STRING = new HashMap<>();

    static {
        MODIFIER_STRING.put(Modifier.PUBLIC, "PUBLIC");
        MODIFIER_STRING.put(Modifier.PRIVATE, "PRIVATE");
        MODIFIER_STRING.put(Modifier.PROTECTED, "PROTECTED");
        MODIFIER_STRING.put(Modifier.STATIC, "STATIC");
        MODIFIER_STRING.put(Modifier.FINAL, "FINAL");
        MODIFIER_STRING.put(Modifier.SYNCHRONIZED, "SYNCHRONIZED");
        MODIFIER_STRING.put(Modifier.VOLATILE, "VOLATILE");
        MODIFIER_STRING.put(Modifier.TRANSIENT, "TRANSIENT");
        MODIFIER_STRING.put(Modifier.NATIVE, "NATIVE");
        MODIFIER_STRING.put(Modifier.INTERFACE, "INTERFACE");
        MODIFIER_STRING.put(Modifier.ABSTRACT, "ABSTRACT");
        MODIFIER_STRING.put(Modifier.STRICT, "STRICT");
    }

    /*================================================*
     * Classes                                        *
     *================================================*/

    /**
     * Asserts that the class {@code className} exists and returns the corresponding
     * {@link Class} object.
     *
     * @param className the fully qualified class name
     * @return the {@link Class} object if no exception is thrown
     */
    public static Class<?> assertClassExists(String className) {
        return assertDoesNotThrow(
            () -> Class.forName(className),
            "Class %s could not be found".formatted(className)
        );
    }

    /**
     * Asserts that {@code clazz} has the modifiers represented by {@code modifiers}.
     *
     * @param clazz     the class to get the actual modifiers from
     * @param modifiers the modifiers the class must have
     */
    public static void assertClassHasModifiers(Class<?> clazz, int modifiers) {
        assertModifiers(modifiers, clazz.getModifiers() & modifiers,
            "Class does not have expected modifiers");
    }

    /**
     * Asserts that {@code clazz} only has the modifiers represented by {@code modifiers}.
     *
     * @param clazz     the class to get the actual modifiers from
     * @param modifiers the exact modifiers the class must have
     */
    public static void assertClassHasExactModifiers(Class<?> clazz, int modifiers) {
        assertModifiers(modifiers, clazz.getModifiers(), "Class does not have expected modifiers");
    }

    /**
     * Asserts that {@code clazz} is not generic, meaning it has no type parameters.
     *
     * @param clazz the class to assert non-genericity for
     */
    public static void assertClassNotGeneric(Class<?> clazz) {
        assertEquals(0, clazz.getTypeParameters().length,
            "Class is supposed to be non-generic but is generic");
    }

    /**
     * Asserts that {@code clazz} is generic and has the exact the type parameters listed in
     * {@code identifiers}. Furthermore, this method asserts that each type parameter has
     * the correct bounds, meaning both the same number of bounds in {@code bounds[i]} as
     * well as the bounds themselves (or rather, their name) in any order. Bounds must be
     * specified as the fully qualified name of the type, e.g. {@code "java.lang.String"}.
     *
     * @param clazz       the class to assert genericity for
     * @param identifiers the names of the expected type parameters
     * @param bounds      a matrix of bounds; mapping of bounds to the corresponding
     *                    type parameter in {@code identifiers}
     */
    public static void assertClassTypeParameters(Class<?> clazz, String[] identifiers, String[][] bounds) {
        TypeVariable<?>[] typeParameters = clazz.getTypeParameters();

        assertEquals(identifiers.length, typeParameters.length,
            "Class is either not generic or does not have the expected number of type parameters");

        for (int i = 0; i < identifiers.length; i++) {
            TypeVariable<?> typeParameter = typeParameters[i];
            assertEquals(identifiers[i], typeParameter.getName(), "Type parameter names do not match");

            Set<String> actualBounds = Arrays
                .stream(typeParameter.getBounds())
                .map(Type::getTypeName)
                .collect(Collectors.toUnmodifiableSet());
            assertEquals(bounds[i].length, actualBounds.size(), ("Type parameter %s does not have the expected "
                + "number of bounds").formatted(typeParameter.getName()));

            for (String bound : bounds[i]) {
                if (!actualBounds.contains(bound)) {
                    fail("Type parameter %s is not bound by %s".formatted(typeParameter.getName(), bound));
                }
            }
        }
    }

    /**
     * Asserts that {{@code predicate} matches the direct superclass of {@code clazz}.
     *
     * @param clazz     the class to assert the superclass for
     * @param predicate the predicate to match the superclass with
     */
    public static void assertClassExtends(Class<?> clazz, Predicate<Type> predicate) {
        if (!predicate.test(clazz.getGenericSuperclass())) {
            fail("Superclass of class \"%s\" does not match given predicate".formatted(clazz.getSimpleName()));
        }
    }

    /**
     * Asserts that {@code clazz} implements interfaces that are matched by {@code interfaces}.
     *
     * @param clazz      the class to assert the implemented interfaces for
     * @param interfaces the predicates to match implemented interfaces
     */
    public static void assertClassImplements(Class<?> clazz, String... interfaces) {
        Set<String> actualInterfaceTypes = Arrays
            .stream(clazz.getGenericInterfaces())
            .map(Type::getTypeName)
            .collect(Collectors.toUnmodifiableSet());

        Arrays
            .stream(interfaces)
            .forEach(typeName -> {
                actualInterfaceTypes
                    .stream()
                    .filter(typeName::equals)
                    .findAny()
                    .orElseThrow(() -> new AssertionFailedError(
                        "Class %s does not implement interface %s".formatted(clazz.getName(), typeName)
                    ));
            });
    }

    /*================================================*
     * Fields                                         *
     *================================================*/

    /**
     * Asserts that {@code clazz} has a field with name {@code fieldName} and returns it.
     *
     * @param clazz     the class the field belongs to
     * @param fieldName the name of the field
     * @return the {@link Field} object if no exception is thrown
     */
    public static Field assertClassHasField(Class<?> clazz, String fieldName) {
        return assertDoesNotThrow(
            () -> assertClassHasField(clazz, field -> field.getName().equals(fieldName)),
            "No field matching name \"%s\" was found".formatted(fieldName)
        );
    }

    /**
     * Asserts that {@code clazz} has exactly one field matching {@code predicate} and returns it.
     *
     * @param clazz     the class the field belongs to
     * @param predicate a predicate for matching required criteria
     * @return the {@link Field} object if no exception is thrown
     */
    public static Field assertClassHasField(Class<?> clazz, Predicate<Field> predicate) {
        Set<Field> filteredFields = Stream
            .concat(Arrays.stream(clazz.getFields()), Arrays.stream(clazz.getDeclaredFields()))
            .filter(predicate)
            .collect(Collectors.toUnmodifiableSet());

        if (filteredFields.size() == 0) {
            throw new AssertionFailedError("No fields matching the given predicate were found");
        } else if (filteredFields.size() != 1) {
            throw new AssertionFailedError("Multiple fields matching the given predicate were found"
                + " - cannot resolve ambiguity");
        } else {
            return filteredFields.iterator().next();
        }
    }

    /**
     * Asserts that a field has correct modifiers, type and name. Checks involving parameters
     * with the {@link Nullable} annotation will be skipped if they are {@code null}.
     *
     * @param field         the field to assert the things above for
     * @param modifiers     the modifiers the field should have
     * @param typePredicate a predicate for asserting the type of the given field
     * @param fieldName     the name the given field should have
     */
    public static void assertField(Field field, @Nullable Integer modifiers, @Nullable Predicate<Type> typePredicate,
                                   @Nullable String fieldName) {
        if (modifiers != null) {
            assertModifiers(modifiers, field.getModifiers(),
                "Modifiers of field \"%s\" don't match the expected ones".formatted(field.getName()));
        }
        if (typePredicate != null && !typePredicate.test(field.getGenericType())) {
            fail("Field \"%s\" does not have correct type".formatted(field.getName()));
        }
        if (fieldName != null) {
            assertEquals(fieldName, field.getName(),
                "Name of Field \"%s\" does not match expected name".formatted(field.getName()));
        }
    }

    /**
     * Asserts that {@code field} matches {@code expected} for the specified field in {@code instance}.
     *
     * @param field    the field to assert the value for
     * @param instance the instance to get the field value from (may be {@code null} if the field is static)
     * @param expected the predicate to test the field value
     */
    public static void assertFieldValue(Field field, @Nullable Object expected, @Nullable Object instance) {
        try {
            field.setAccessible(true);
            assertEquals(expected, field.get(instance));
        } catch (IllegalAccessException e) {
            fail("Could not access field \"%s\"".formatted(field.getName()));
        } catch (InaccessibleObjectException | SecurityException e) {
            fail("Could not make field \"%s\" accessible".formatted(field.getName()));
        }
    }

    /*================================================*
     * Constructor                                    *
     *================================================*/

    /**
     * Asserts that {@code clazz} has a constructor matching {@code predicate} and returns it.
     *
     * @param clazz     the class the constructor belongs to
     * @param predicate the predicate matching constructor
     * @return the {@link Constructor} object if no exception is thrown
     */
    public static Constructor<?> assertClassHasConstructor(Class<?> clazz, Predicate<Constructor<?>> predicate) {
        Set<Constructor<?>> filteredConstructors = Stream
            .concat(Arrays.stream(clazz.getConstructors()), Arrays.stream(clazz.getDeclaredConstructors()))
            .filter(predicate)
            .collect(Collectors.toUnmodifiableSet());

        if (filteredConstructors.size() == 0) {
            throw new AssertionFailedError("No constructors matching the given predicate were found");
        } else if (filteredConstructors.size() != 1) {
            throw new AssertionFailedError("Multiple constructors matching the given predicate were found"
                + " - cannot resolve ambiguity");
        } else {
            return filteredConstructors.iterator().next();
        }
    }

    /**
     * Asserts that {@code constructor} has correct modifiers and parameters types.
     *
     * @param constructor         the constructor to assert the things above for
     * @param modifiers           the modifiers the constructor should have
     * @param parameterPredicates predicates for the constructor's parameters in the same order they
     *                            are declared
     */
    @SafeVarargs
    public static void assertConstructor(Constructor<?> constructor, @Nullable Integer modifiers,
                                         Predicate<Type>... parameterPredicates) {
        Type[] actualParameterTypes = constructor.getGenericParameterTypes();

        if (modifiers != null) {
            assertModifiers(modifiers, constructor.getModifiers(),
                "Modifiers of constructor don't match the expected ones");
        }
        assertEquals(
            parameterPredicates.length,
            actualParameterTypes.length,
            "Constructor with parameter list [%s] does not have expected number of parameters"
                .formatted(parametersToString(actualParameterTypes))
        );
        for (int i = 0; i < parameterPredicates.length; i++) {
            if (parameterPredicates[i] != null && !parameterPredicates[i].test(actualParameterTypes[i])) {
                fail(("Parameter #%d for constructor with parameter list [%s]"
                    + " does not match predicate").formatted(i, parametersToString(actualParameterTypes)));
            }
        }
    }

    /*================================================*
     * Methods                                        *
     *================================================*/

    /**
     * Asserts that {@code clazz} has a method with name {@code methodName} and returns it.
     *
     * @param clazz      the class the method belongs to
     * @param methodName the name of the method
     * @return the {@link Method} object if no exception is thrown
     */
    public static Method assertClassHasMethod(Class<?> clazz, String methodName) {
        return assertDoesNotThrow(
            () -> assertClassHasMethod(clazz, field -> field.getName().equals(methodName)),
            "No method matching name \"%s\" was found".formatted(methodName)
        );
    }

    /**
     * Asserts that {@code clazz} has exactly one method matching {@code predicate} and returns it.
     *
     * @param clazz           the class the method belongs to
     * @param predicate a predicate for matching required criteria
     * @return the {@link Method} object if no exception is thrown
     */
    public static Method assertClassHasMethod(Class<?> clazz, Predicate<Method> predicate) {
        Set<Method> filteredMethods = Stream
            .concat(Arrays.stream(clazz.getMethods()), Arrays.stream(clazz.getDeclaredMethods()))
            .filter(predicate)
            .collect(Collectors.toUnmodifiableSet());

        if (filteredMethods.size() == 0) {
            throw new AssertionFailedError("No methods matching the given predicate were found");
        } else if (filteredMethods.size() != 1) {
            throw new AssertionFailedError("Multiple methods matching the given predicate were found"
                + " - cannot resolve ambiguity");
        } else {
            return filteredMethods.iterator().next();
        }
    }

    /**
     * Asserts that a method has correct modifiers, return type, parameter types and name. Checks
     * involving parameters with the {@link Nullable} annotation will be skipped if they are {@code null}.
     * {@code parameterPredicates} itself cannot be {@code null} but its components can.
     *
     * @param method              the method to assert the things above for
     * @param modifiers           the modifiers the method should have
     * @param returnTypePredicate a predicate for asserting the type of the given method
     * @param parameterPredicates predicates for the methods' parameters in the same order they
     *                            are declared
     * @param methodName          the name the given method should have
     */
    @SafeVarargs
    public static void assertMethod(Method method,
                                    @Nullable Integer modifiers,
                                    @Nullable Predicate<Type> returnTypePredicate,
                                    @Nullable String methodName,
                                    Predicate<Type>... parameterPredicates) {
        Type[] actualParameterTypes = method.getGenericParameterTypes();

        if (modifiers != null) {
            assertModifiers(modifiers, method.getModifiers(),
                "Modifiers of method \"%s\" don't match the expected ones".formatted(methodToString(method)));

        }
        if (returnTypePredicate != null && !returnTypePredicate.test(method.getGenericReturnType())) {
            fail("Method \"%s\" does not have the correct return type".formatted(methodToString(method)));
        }
        assertEquals(parameterPredicates.length, actualParameterTypes.length,
            "Method \"%s\" does not have expected number of parameters".formatted(methodToString(method)));
        for (int i = 0; i < parameterPredicates.length; i++) {
            if (parameterPredicates[i] != null && parameterPredicates[i].test(actualParameterTypes[i])) {
                fail("Parameter #%d of Method \"%s\" does not match given predicate"
                    .formatted(i, methodToString(method)));
            }
        }
        if (methodName != null) {
            assertEquals(methodName, method.getName(),
                "Name of Method \"%s\" does not match expected name".formatted(methodToString(method)));
        }
    }

    /**
     * Asserts that {@code method} returns an object that equals {@code expected} when invoked.
     * {@code instance} may only be {@code null} if {@code method} is static.
     *
     * @param method     the method that will be invoked
     * @param expected   the expected object
     * @param instance   the execution context
     * @param parameters the parameters that will be passed to the method
     */
    public static void assertMethodReturnValue(Method method, @Nullable Object expected, @Nullable Object instance,
                                               Object... parameters) {
        try {
            method.setAccessible(true);
            assertEquals(expected, method.invoke(instance, parameters));
        } catch (InaccessibleObjectException | SecurityException e) {
            fail("Could not make method \"%s\" accessible".formatted(methodToString(method)), e);
        } catch (IllegalAccessException e) {
            fail("Could not access field \"%s\"".formatted(methodToString(method)), e);
        } catch (InvocationTargetException e) {
            fail("An exception occurred while invoking method \"%s\"".formatted(methodToString(method)), e);
        }
    }

    /*================================================*
     * Utility methods                                *
     *================================================*/

    /**
     * Asserts that the given modifiers match and throws an {@link AssertionFailedError} in case they don't.
     *
     * @param expected         the expected modifiers
     * @param actual           the actual modifiers
     * @param exceptionMessage the exception message
     */
    private static void assertModifiers(int expected, int actual, String exceptionMessage) {
        if (actual != expected) {
            throw new AssertionFailedError(exceptionMessage, modifiersToString(expected), modifiersToString(actual));
        }
    }

    /**
     * Extracts all modifiers in {@code modifiers} in human-readable form.
     *
     * @param modifiers the modifiers
     * @return the human-readable modifiers
     */
    private static String modifiersToString(int modifiers) {
        StringJoiner joiner = new StringJoiner(", ");

        for (int i = 0; i < MODIFIER_STRING.size(); i++) {
            if ((1 << i & modifiers) != 0) {
                joiner.add(MODIFIER_STRING.get(1 << i));
            }
        }

        return joiner.toString();
    }

    /**
     * Returns the method's signature as a string.
     *
     * @param method the method to get the signature for
     * @return the method signature
     */
    private static String methodToString(Method method) {
        return "%s(%s)".formatted(method.getName(), parametersToString(method.getGenericParameterTypes()));
    }

    /**
     * Returns the type names of the given types joined together with commas.
     *
     * @param parameterTypes an array of types
     * @return the joined type names
     */
    private static String parametersToString(Type... parameterTypes) {
        return Arrays.stream(parameterTypes).map(Type::getTypeName).collect(Collectors.joining(", "));
    }
}
