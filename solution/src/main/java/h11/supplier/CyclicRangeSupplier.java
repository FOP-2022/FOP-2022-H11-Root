package h11.supplier;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CyclicRangeSupplier implements Supplier<Integer> {

    private final Iterator<Integer> iterator;

    public CyclicRangeSupplier(int first, int last) {
        iterator = Stream
            .iterate(
                first,
                i -> i == last ?
                    first :
                    first < last ?
                        i + 1 :
                        i - 1
            )
            .iterator();
    }

    @Override
    public Integer get() {
        return iterator.next();
    }
}
