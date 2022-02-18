package h11.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;

/**
 * Checks if a test methods requirements are fulfilled prior to invocation.
 */
public interface PreInvocationCheck {

    /**
     * Performs a check that the test method with ID {@code testID} has all requirements fulfilled.
     *
     * @param testID the test identifier
     */
    void check(int testID);

    /**
     * Intercepts the invocation of a {@link org.junit.jupiter.api.Test}-annotated method
     * and performs the check described by {@link #check(int)} on it.
     * The method must be annotated with {@link TestID}.
     */
    class Interceptor implements InvocationInterceptor {

        @Override
        public void interceptTestMethod(Invocation<Void> invocation,
                                        ReflectiveInvocationContext<Method> invocationContext,
                                        ExtensionContext extensionContext) throws Throwable {
            if (extensionContext.getRequiredTestInstance() instanceof PreInvocationCheck instance) {
                instance.check(extensionContext.getRequiredTestMethod().getAnnotation(TestID.class).value());
                invocation.proceed();
            } else {
                invocation.skip();
                throw new IllegalCallerException("Extension is not compatible with method that called it");
            }
        }
    }
}
