package h11.merge;

import h11.unicode.CharWithIndex;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class StreamMergerTest {

    @Test
    public void testMerge() {
        StreamMerger streamMerger = new StreamMerger();
        Stream<Integer>[] streams = new Stream[] {
            Stream.of(10, -50, 100, 70),
            Stream.generate((Supplier<Integer>) () -> null).limit(5),
            IntStream.range(50, 55).boxed()
        };
        CharWithIndex[] actualCharsWithIndices = streamMerger.merge(streams);
        CharWithIndex[] expectedCharsWithIndices = Stream
            .of(10, 100, null, 50, 51, 70, 52, 53, 54)
            .map(i -> i == null ? null : new CharWithIndex((char) (int) i, i))
            .toArray(CharWithIndex[]::new);

        assertArrayEquals(expectedCharsWithIndices, actualCharsWithIndices);
    }
}
