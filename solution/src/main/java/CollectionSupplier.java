import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class CollectionSupplier<T> implements Supplier<T> {

    private final Iterator<T> iterator;

    public CollectionSupplier(Collection<T> collection) {
        iterator = collection.iterator();
    }

    @Override
    public T get() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
