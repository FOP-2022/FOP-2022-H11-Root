package h11.unicode;

/**
 * {@link FormatException} signals that a code point is invalid or can not be
 * represented by a {@link Character} object.
 */
public class FormatException extends RuntimeException {

    /**
     * Initializes a new {@link FormatException} object.
     *
     * @param i the invalid code point
     */
    public FormatException(int i) {
        super(i + " " + generateMessage(i));
    }

    /**
     * Generates the specific exception message, depending on the value of {@code i}.
     *
     * @param i the invalid code point
     * @return a specific exception message describing the issue
     */
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
