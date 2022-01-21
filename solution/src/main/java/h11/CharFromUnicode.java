package h11;

import java.util.function.Function;

public class CharFromUnicode implements Function<Integer, Character> {

    @Override
    public Character apply(Integer integer) {
        if (integer == null)
            throw new NullPointerException();
        if (integer < 0 || integer > Character.MAX_VALUE)
            throw new IllegalArgumentException(integer.toString());

        return Character.toChars(integer)[0];
    }
}
