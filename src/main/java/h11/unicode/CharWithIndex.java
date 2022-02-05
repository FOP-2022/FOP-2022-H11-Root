package h11.unicode;

import java.util.Objects;

/**
 * A data class or pair for {@link char} and {@link int}.
 */
public class CharWithIndex {

    private final char theChar;
    private final int index;

    /**
     * Initializes a new {@link CharWithIndex} object.
     * Creates a pairing of a {@link Character} object and an {@link Integer} object.
     *
     * @param character the {@link Character} object
     * @param integer   the {@link Integer} object
     * @throws NullPointerException if either {@code character} or {@code integer} are {@code null}
     */
    public CharWithIndex(Character character, Integer integer) {
        Objects.requireNonNull(character);
        Objects.requireNonNull(integer);

        this.theChar = character;
        this.index = integer;
    }

    /**
     * Returns the value of private field "theChar".
     *
     * @return the value
     */
    public char getChar() {
        return theChar;
    }

    /**
     * Returns the value of private field "index".
     *
     * @return the value
     */
    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharWithIndex that = (CharWithIndex) o;
        return theChar == that.theChar && index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(theChar, index);
    }

    @Override
    public String toString() {
        return String.format("%d: %c", index, theChar);
    }
}
