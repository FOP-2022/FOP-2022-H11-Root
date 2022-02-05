package h11.supplier;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A supplier for infinite elements in a closed cyclic range.
 */
public class CyclicRangeSupplier implements Supplier<Integer> {

    private final Iterator<Integer> iterator;

    /**
     * Initializes a new {@link CyclicRangeSupplier} object.
     * Creates an infinite iterator and assigns it to a private field.
     * The iterator starts at {@code start} and goes up (or down if {@code last < first})
     * to {@code last} inclusive. If {@link Iterator#next()} is called when the last value
     * returned was {@code last}, the iterator will wrap around and start again at {@code start}.
     * If both values are equal, that will be the only value the iterator will return.
     *
     * @param first the value to start at
     * @param last  the value after which to loop around
     */
    public CyclicRangeSupplier(int first, int last) {
        iterator = Stream
            .iterate(
                first,
                i -> i == last
                    ?
                        first :
                        first < last
                            ?
                                i + 1 :
                                i - 1
            )
            .iterator();
    }

    /**
     * Returns the next value in the specified range.
     *
     * @return the next value
     */
    @Override
    public Integer get() {
        return iterator.next();
    }
}
