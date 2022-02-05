package h11.supplier;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * A supplier for elements in a collection.
 *
 * @param <T> the generic type of the supplied value
 */
public class CollectionSupplier<T> implements Supplier<T> {

    private final Iterator<T> iterator;

    /**
     * Initializes a new {@link CollectionSupplier} object.
     * Creates an iterator for the given collection and assigns it to a private field.
     *
     * @param collection the collection of values to return successively
     */
    public CollectionSupplier(Collection<T> collection) {
        iterator = collection.iterator();
    }

    /**
     * Returns the values of the collection given to the constructor successively.
     *
     * @return the next value of the given collection or {@code null} if the end has been reached
     */
    @Override
    public T get() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
