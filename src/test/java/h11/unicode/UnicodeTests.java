package h11.unicode;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class UnicodeTests {

    /**
     * Tests {@link CharFromUnicode#apply(Integer)}.
     * Assert that the correct value is returned or the correct exception is thrown for:
     * <ul>
     *     <li>a random lowercase letter</li>
     *     <li>a random uppercase letter</li>
     *     <li>a random special character</li>
     *     <li>a random negative integer</li>
     *     <li>a random integer greater than {@link Character#MAX_CODE_POINT}</li>
     * </ul>
     */
    @Test
    public void testCharFromUnicode() {
        CharFromUnicode charFromUnicode = new CharFromUnicode();

        for (int i = 0; i < 5; i++) {
            int lowercaseLetter = ThreadLocalRandom.current().nextInt('a', 'z' + 1);
            int uppercaseLetter = ThreadLocalRandom.current().nextInt('A', 'Z' + 1);
            int specialCharacter = ThreadLocalRandom.current().nextInt(0x20, 0x30);
            int negativeInvalidNumber = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
            int positiveInvalidNumber = ThreadLocalRandom.current().nextInt(Character.MAX_CODE_POINT + 1, Integer.MAX_VALUE);

            assertEquals((char) lowercaseLetter, charFromUnicode.apply(lowercaseLetter));
            assertEquals((char) uppercaseLetter, charFromUnicode.apply(uppercaseLetter));
            assertEquals((char) specialCharacter, charFromUnicode.apply(specialCharacter));
            assertThrowsExactly(FormatException.class, () -> charFromUnicode.apply(negativeInvalidNumber));
            assertThrowsExactly(FormatException.class, () -> charFromUnicode.apply(positiveInvalidNumber));
        }
    }

    /**
     * Tests {@link CharFromUnicodeCasesExchanged#apply(Integer)}.
     * Assert that the correct value is returned or the correct exception is thrown for:
     * <ul>
     *     <li>a random lowercase letter</li>
     *     <li>a random uppercase letter</li>
     *     <li>a random special character</li>
     *     <li>a random negative integer</li>
     *     <li>a random integer greater than {@link Character#MAX_CODE_POINT}</li>
     * </ul>
     */
    @Test
    public void testCharFromUnicodeCasesExchanged() {
        CharFromUnicodeCasesExchanged charFromUnicodeCasesExchanged = new CharFromUnicodeCasesExchanged();

        for (int i = 0; i < 5; i++) {
            int lowercaseLetter = ThreadLocalRandom.current().nextInt('a', 'z' + 1);
            int uppercaseLetter = ThreadLocalRandom.current().nextInt('A', 'Z' + 1);
            int specialCharacter = ThreadLocalRandom.current().nextInt(0x20, 0x30);
            int negativeInvalidNumber = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
            int positiveInvalidNumber = ThreadLocalRandom.current().nextInt(Character.MAX_CODE_POINT + 1, Integer.MAX_VALUE);

            assertEquals(Character.toUpperCase((char) lowercaseLetter), charFromUnicodeCasesExchanged.apply(lowercaseLetter));
            assertEquals(Character.toLowerCase((char) uppercaseLetter), charFromUnicodeCasesExchanged.apply(uppercaseLetter));
            assertEquals((char) specialCharacter, charFromUnicodeCasesExchanged.apply(specialCharacter));
            assertThrowsExactly(FormatException.class, () -> charFromUnicodeCasesExchanged.apply(negativeInvalidNumber));
            assertThrowsExactly(FormatException.class, () -> charFromUnicodeCasesExchanged.apply(positiveInvalidNumber));
        }
    }
}
