package h11.unicode;

import java.util.Objects;

public class CharWithIndex {

    private final char theChar; // TODO: better name
    private final int index;

    public CharWithIndex(Character character, Integer integer) {
        Objects.requireNonNull(character);
        Objects.requireNonNull(integer);

        this.theChar = character;
        this.index = integer;
    }

    public char getChar() {
        return theChar;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
