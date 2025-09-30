package io.jokev.guestbook_api.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SafeTextValidatorTest {

    @Test
    public void testIsSafe_withAllowedLettersAndDigits() {
        assertTrue(SafeTextValidator.isSafe("Hello123"));
    }

    @Test
    public void testIsSafe_withAllowedSpaceAndPunctuation() {
        assertTrue(SafeTextValidator.isSafe("Hi there! How's it going? (ok)"));
    }

    @Test
    public void testIsSafe_withEmptyString() {
        assertTrue(SafeTextValidator.isSafe(""));
    }

    @Test
    public void testIsSafe_withDisallowedControlChar() {
        assertFalse(SafeTextValidator.isSafe("Hello\u0000World"));
    }

    @Test
    public void testIsSafe_withDisallowedSurrogateChar() {
        assertFalse(SafeTextValidator.isSafe("Test\uD800Test"));
    }

    @Test
    public void testIsSafe_withDisallowedPunctuation() {
        assertFalse(SafeTextValidator.isSafe("Hello@World#"));
    }

    @Test
    public void testIsSafe_withMixedAllowedAndDisallowed() {
        assertFalse(SafeTextValidator.isSafe("Good! Bad$"));
    }

    @Test
    public void testIsSafe_withAllowedPunctuationOnly() {
        assertTrue(SafeTextValidator.isSafe(",.!?'-_()[]\""));
    }

    @Test
    public void testIsSafe_withUnicodeLetter() {
        assertTrue(SafeTextValidator.isSafe("Café"));
    }

    @Test
    public void testIsSafe_withNonSpaceWhitespace() {
        assertFalse(SafeTextValidator.isSafe("Hello\tWorld"));
    }
}
