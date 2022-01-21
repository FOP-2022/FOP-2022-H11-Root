package h11;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class StreamMerger {

    private final Predicate<Integer> predicate;
    private final Comparator<Integer> comparator;
    private final Function<Integer, CharWithIndex> function;
    private final Collector<CharWithIndex, List<CharWithIndex>, CharWithIndex[]> collector;

    /*
     * 1. remove need to iterate through stream to find out, if an element is null
     * 2. remove check on stream sorted (just sort it with the comparator?)
     * just concat streams, filter, sort, map? maybe reduce / collect somewhere?
     *
     * helper method to concat streams (varargs),
     */

    public StreamMerger() {
        this.predicate = Objects::nonNull;
        this.comparator = (i1, i2) -> digitSum(i1) - digitSum(i2);
        this.function = integer -> integer >= 0 && integer <= Character.MAX_VALUE ? // alternative: try-catch
            new CharWithIndex(Character.toChars(integer)[0], integer) :
            null;
        this.collector = new CharWithIndexCollector();
    }

    public CharWithIndex[] merge(Stream<Integer>[] integerStreams) {
        return concat(integerStreams)
            .filter(predicate)
            .sorted(comparator)
            .map(function)
            .collect(collector);
    }

    @SafeVarargs
    private Stream<Integer> concat(Stream<Integer>... streams) {
        Stream<Integer> concatStream = Stream.of();

        for (Stream<Integer> stream : streams) {
            concatStream = Stream.concat(concatStream, stream);
        }

        return concatStream;
    }

    // NOTE: negative integers also allowed
    private Integer digitSum(Integer integer) {
        return integer
            .toString()
            .chars()
            .filter(i -> i >= '0' && i <= '9')
            .map(i -> i - '0')
            .sum();
    }

    public static class CharWithIndexCollector implements Collector<CharWithIndex, List<CharWithIndex>, CharWithIndex[]> {
        @Override
        public Supplier<List<CharWithIndex>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<CharWithIndex>, CharWithIndex> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<CharWithIndex>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);

                return list1;
            };
        }

        @Override
        public Function<List<CharWithIndex>, CharWithIndex[]> finisher() {
            return list -> list.toArray(CharWithIndex[]::new);
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }
}
