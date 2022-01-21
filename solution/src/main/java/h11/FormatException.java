package h11;

public class FormatException extends Exception {

    public FormatException(int i) {
        super(i + " " + generateMessage(i));
    }

    private static String generateMessage(int i) {
        if (i > Character.MAX_CODE_POINT) {
            return "exceeds 0x10FFFF and is not a valid Unicode code point";
        } else if (i > Character.MAX_VALUE) {
            return "exceeds 0xFFFF and cannot be represented by Character";
        } else {
            return "is a negative number and therefore not a valid code point";
        }
    }
}
