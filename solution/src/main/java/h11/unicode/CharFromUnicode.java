package h11.unicode;

import java.util.function.Function;

public class CharFromUnicode implements Function<Integer, Character> {

    @Override
    public Character apply(Integer integer) {
        if (integer == null)
            throw new NullPointerException();
        if (integer < Character.MIN_VALUE || integer > Character.MAX_VALUE)
            throw new FormatException(integer);

        return Character.toChars(integer)[0];
    }
}
