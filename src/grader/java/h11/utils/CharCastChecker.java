package h11.utils;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Check for narrowing casts to {@code char} and throw an exception if one is encountered.
 */
public final class CharCastChecker {

    private CharCastChecker() {}

    private static final Map<String, CtModel> MODEL_CACHE = new HashMap<>();

    /**
     * Check if the class with fully qualified name {@code className} has any narrowing casts to {@code char}.
     *
     * @param className the fully qualified name of the class to check
     */
    public static void checkModel(String className) {
        getModelForClass(className)
            .getAllTypes()
            .stream()
            .filter(ctType -> ctType.getQualifiedName().equals(className))
            .flatMap(ctType -> {
                for (CtMethod<?> method : ctType.getMethodsByName("apply")) {
                    List<CtParameter<?>> parameterList = method.getParameters();
                    if (parameterList.size() == 1
                        && parameterList.get(0).getType().getQualifiedName().equals(Integer.class.getName())) {
                        return getCastsToChar(method);
                    }
                }
                return Stream.empty();
            })
            .forEach(CharCastChecker::assertNoUnsafeCharCast);
    }

    private static CtModel getModelForClass(String className) {
        return MODEL_CACHE.computeIfAbsent(className, sourceClassName -> {
            Launcher launcher = new Launcher();
            launcher.addInputResource(
                switch (sourceClassName) {
                    case "h11.unicode.CharFromUnicode" ->
                        "src/main/java/h11/unicode/CharFromUnicode.java";
                    case "h11.unicode.CharFromUnicodeCasesExchanged" ->
                        "src/main/java/h11/unicode/CharFromUnicodeCasesExchanged.java";
                    default ->
                        "";
                }
            );
            return launcher.buildModel();
        });
    }

    private static Stream<CtExpression<?>> getCastsToChar(CtMethod<?> method) {
        return method
            .getBody()
            .getElements(element -> {
                if (element instanceof CtExpression<?> expression) {
                    return expression
                        .getTypeCasts()
                        .stream()
                        .anyMatch(ctTypeReference -> ctTypeReference.getQualifiedName().equals("char"));
                } else {
                    return false;
                }
            })
            .stream()
            .map(element -> (CtExpression<?>) element);
    }

    private static void assertNoUnsafeCharCast(CtExpression<?> expression) {
        expression
            .getTypeCasts()
            .stream()
            .filter(ctTypeReference -> ctTypeReference.getQualifiedName().equals("char"))
            .forEach(ctTypeReference -> {
                boolean charCastFound = false; // make sure cast to char comes _before_ a number
                Iterator<CtElement> iterator = ctTypeReference.getParent().descendantIterator();
                while (iterator.hasNext()) {
                    CtElement element = iterator.next();
                    if (element instanceof CtTypeReference<?> typeReference) {
                        switch (typeReference.getQualifiedName()) {
                            case "char":
                                charCastFound = true;
                                break;

                            case "int":
                            case "long":
                            case "float":
                            case "double":
                                if (charCastFound) {
                                    fail("Unsafe cast to char in line " + typeReference.getPosition().getLine());
                                }
                                break; // make CheckStyle shut up

                            default:
                        }
                    }
                }
            });
    }
}
