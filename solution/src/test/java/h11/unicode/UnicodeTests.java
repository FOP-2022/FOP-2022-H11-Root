package h11.unicode;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnicodeTests {

    @Test
    public void testBothClassesCharFromUnicode() {
        Random random = new Random();
        CharFromUnicode charFromUnicode = new CharFromUnicode();
        CharFromUnicodeCasesExchanged charFromUnicodeCasesExchanged = new CharFromUnicodeCasesExchanged();

        for (int i = 0; i < 5; i++) {
            int randomSmallChar = random.nextInt('a', 'z' + 1);
            int randomCapitalChar = random.nextInt('A', 'Z' + 1);
            int randomOutOfRangeInt = 0;
            int randomNegativeOutOfRangeInt = random.nextInt(Integer.MIN_VALUE, 0);
            int randomPositiveOutOfRangeInt = random.nextInt(Character.MAX_CODE_POINT + 1, Integer.MAX_VALUE);

            assertEquals((char) randomSmallChar, charFromUnicode.apply(randomSmallChar));
            assertEquals((char) randomCapitalChar, charFromUnicode.apply(randomCapitalChar));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomNegativeOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicode.apply(randomPositiveOutOfRangeInt));

            assertEquals(Character.toUpperCase((char) randomSmallChar), charFromUnicodeCasesExchanged.apply(randomSmallChar));
            assertEquals((char) randomCapitalChar, charFromUnicodeCasesExchanged.apply(randomCapitalChar));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomNegativeOutOfRangeInt));
            assertThrows(Exception.class, () -> charFromUnicodeCasesExchanged.apply(randomPositiveOutOfRangeInt));
        }
    }
}
