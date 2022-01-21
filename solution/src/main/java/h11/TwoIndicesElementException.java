package h11;

public class TwoIndicesElementException extends Exception {

    public TwoIndicesElementException(String s, int i, int j) {
        super(String.format("%s at (%d,%d)", s, i, j));
    }
}
