package h11.unicode;

import java.util.function.Function;

/**
 * A Function for mapping an {@link Integer} to a {@link Character}.
 */
public class CharFromUnicode implements Function<Integer, Character> {

    /**
     * Maps the given {@link Integer} to a corresponding {@link Character} object.
     *
     * @param integer the integer to map
     * @return a {@link Character} with code point {@code integer}
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

        return Character.toChars(integer)[0];
    }
}
