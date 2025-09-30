package io.jokev.guestbook_api.util;

public class SafeTextValidator {

    public static boolean isSafe(String input) {
        for (char c : input.toCharArray()) {
            if (!isAllowedChar(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllowedChar(char c) {
        if (Character.isISOControl(c) || Character.isSurrogate(c)) {
            return false;
        }

        return Character.isLetterOrDigit(c)
                || Character.isSpaceChar(c)
                || isAllowedPunctuation(c);
    }

    private static boolean isAllowedPunctuation(char c) {
        return ",.!?'-_()[]\"".indexOf(c) >= 0;
    }

}
