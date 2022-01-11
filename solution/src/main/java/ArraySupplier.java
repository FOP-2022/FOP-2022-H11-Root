import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;

public class ArraySupplier<T> implements Supplier<T> {

    private final Iterator<T> iterator;

    public ArraySupplier(T[] ts) {
        iterator = Arrays.stream(ts).iterator();
    }

    @Override
    public T get() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
