package h11.unicode;

import java.util.function.Function;

public class CharFromUnicodeCasesExchanged implements Function<Integer, Character> {

    /**
     * Maps the given {@link Integer} to a corresponding {@link Character} object with
     * uppercase and lowercase letters swapped.
     * @param integer the integer to map
     * @return a {@link Character} with code point {@code integer} or code point of its
     * lowercase / uppercase equivalent
     * @throws NullPointerException if {@code integer} is {@code null}
     * @throws FormatException if the given integer cannot be represented by {@link Character}
     */
    @Override
    public Character apply(Integer integer) {
        if (integer == null) {
            throw new NullPointerException();
        }
        if (integer < Character.MIN_VALUE || integer > Character.MAX_VALUE) {
            throw new FormatException(integer);
        }

        char character = Character.toChars(integer)[0];

        if (Character.isLowerCase(character)) {
            return Character.toUpperCase(character);
        } else if (Character.isUpperCase(character)) {
            return Character.toLowerCase(character);
        } else {
            return character;
        }
    }
}
