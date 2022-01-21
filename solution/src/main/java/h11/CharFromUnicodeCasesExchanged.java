package h11;

import java.util.function.Function;

public class CharFromUnicodeCasesExchanged implements Function<Integer, Character> {

    @Override
    public Character apply(Integer integer) {
        if (integer == null)
            throw new NullPointerException();
        if (integer < 0 || integer > Character.MAX_VALUE)
            throw new IllegalArgumentException(integer.toString());

        char character = Character.toChars(integer)[0];

        if (Character.isLowerCase(character))
            return Character.toUpperCase(character);
        else if (Character.isUpperCase(character))
            return Character.toLowerCase(character);
        else
            return character;
    }
}
