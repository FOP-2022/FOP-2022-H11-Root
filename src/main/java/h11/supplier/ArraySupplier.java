package h11.supplier;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * A supplier for elements in a generic arrays.
 *
 * @param <T> the generic type of the supplied value
 */
public class ArraySupplier<T> implements Supplier<T> {

    private final Iterator<T> iterator;

    /**
     * Initializes a new {@link ArraySupplier} object.
     * Creates an iterator for the given array and assigns it to a private field.
     *
     * @param ts the array of values to return successively
     */
    public ArraySupplier(T[] ts) {
        iterator = Arrays.stream(ts).iterator();
    }

    /**
     * Returns the values of the array given to the constructor successively.
     *
     * @return the next value of the given array or {@code null} if the end has been reached
     */
    @Override
    public T get() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
