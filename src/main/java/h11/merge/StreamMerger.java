package h11.merge;

import h11.unicode.CharWithIndex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Class for merging multiple streams of integers into an array of {@link CharWithIndex} objects.
 */
public class StreamMerger {

    private final Predicate<Integer> predicate;
    private final Comparator<Integer> comparator;
    private final Function<Integer, CharWithIndex> function;
    private final Collector<CharWithIndex, List<CharWithIndex>, CharWithIndex[]> collector;

    /**
     * Initializes a new {@link StreamMerger} object.
     * Sets all required attributes to their designated value.
     */
    public StreamMerger() {
        predicate = Objects::nonNull;
        comparator = (i1, i2) -> digitSum(i1) - digitSum(i2);
        function = integer ->
            integer >= 0 && integer <= Character.MAX_VALUE
                ? new CharWithIndex(Character.toChars(integer)[0], integer)
                : null;
        collector = new CharWithIndexCollector();
    }

    /**
     * Merges multiple streams of integers into an array of {@link CharWithIndex}.
     * The merge process is done in multiple steps:
     * <ol>
     *     <li>The array of streams is merged into one continuous stream</li>
     *     <li>All {@code null}-elements are filtered</li>
     *     <li>The elements are sorted in ascending order, according to their sum of digits</li>
     *     <li>
     *         Each element is mapped to a {@link CharWithIndex} object if its value is representable
     *         by {@link Character} or {@code null} if it isn't
     *     </li>
     *     <li>The resulting stream of {@link CharWithIndex} is collected into an array of the same type</li>
     * </ol>
     *
     * @param integerStreams the array of streams to precess
     * @return the resulting array of {@link CharWithIndex}
     */
    public CharWithIndex[] merge(Stream<Integer>[] integerStreams) {
        return concat(integerStreams)
            .filter(predicate)
            .sorted(comparator)
            .map(function)
            .collect(collector);
    }

    /**
     * Merge an arbitrary number of streams of integers into one continuous stream.
     *
     * @param streams a variable amount of streams to be merged
     * @return a continuous stream containing all elements in order
     */
    @SafeVarargs
    private Stream<Integer> concat(Stream<Integer>... streams) {
        Stream<Integer> concatStream = Stream.of();

        for (Stream<Integer> stream : streams) {
            concatStream = Stream.concat(concatStream, stream);
        }

        return concatStream;
    }

    /**
     * Calculate the sum of digits for a given integer.
     * If the integer is negative, its absolute value will be used.
     *
     * @param integer the integer to calculate the sum for
     * @return the resulting sum of digits for the given integer
     */
    private Integer digitSum(Integer integer) {
        return integer
            .toString()
            .chars()
            .filter(i -> i >= '0' && i <= '9')
            .map(i -> i - '0')
            .sum();
    }

    /**
     * A collector for accumulating {@link CharWithIndex} objects and saving them in an array of the same type.
     */
    public static class CharWithIndexCollector implements Collector<CharWithIndex, List<CharWithIndex>, CharWithIndex[]> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Supplier<List<CharWithIndex>> supplier() {
            return ArrayList::new;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BiConsumer<List<CharWithIndex>, CharWithIndex> accumulator() {
            return List::add;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BinaryOperator<List<CharWithIndex>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);

                return list1;
            };
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Function<List<CharWithIndex>, CharWithIndex[]> finisher() {
            return list -> list.toArray(CharWithIndex[]::new);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }
}
