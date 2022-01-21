package h11.unicode;

import java.util.Objects;

public class CharWithIndex {

    private char theChar; // TODO: better name
    private int index;

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
}
