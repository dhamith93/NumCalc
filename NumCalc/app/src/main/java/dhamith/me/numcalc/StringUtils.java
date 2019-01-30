package dhamith.me.numcalc;

import java.util.HashMap;

public class StringUtils {
    private static HashMap<Character, String> unicodeSuperscriptMap = new HashMap<>();
    static {
        unicodeSuperscriptMap.put('0', "⁰");
        unicodeSuperscriptMap.put('1', "¹");
        unicodeSuperscriptMap.put('2', "²");
        unicodeSuperscriptMap.put('3', "³");
        unicodeSuperscriptMap.put('4', "⁴");
        unicodeSuperscriptMap.put('5', "⁵");
        unicodeSuperscriptMap.put('6', "⁶");
        unicodeSuperscriptMap.put('7', "⁷");
        unicodeSuperscriptMap.put('8', "⁸");
        unicodeSuperscriptMap.put('9', "⁹");
    }

    public static String convertOpsToUnicode(String input) {
        if (input.isEmpty()) {
            return null;
        }

        return input.replace("*", "×").replace("/", "÷");
    }

    public static String convertOpsToNormal(String input) {
        if (input.isEmpty()) {
            return null;
        }

        return input.replace("×", "*").replace("÷", "/");
    }

    public static String replaceHexWithDecimal(String input) {
        if (input.isEmpty()) {
            return null;
        }

        input = input.toUpperCase();

        return input.replace("A", "10").replace("B", "11")
                .replace("C", "12").replace("D", "13")
                .replace("E", "14").replace("F", "15");
    }

    public static String replaceDecimalWithHex(String input) {
        if (input.isEmpty()) {
            return null;
        }

        return input.replace("10", "A").replace("11", "B")
                .replace("12", "C").replace("13", "D")
                .replace("14", "E").replace("15", "F");
    }

    public static String getUnicodeSuperscript(int exponent) {
        String superscript = String.valueOf(exponent);
        StringBuilder outputBuilder = new StringBuilder();

        for (int i = 0; i < superscript.length(); i++) {
            outputBuilder.append(unicodeSuperscriptMap.get(superscript.charAt(i)));
        }

        return outputBuilder.toString();
    }

    public static boolean isDecimal(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("^(?:[0-9])+$");
    }

    public static boolean isBinary(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("^(?:[0-1])+$");
    }

    public static boolean isOctal(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("^(?:[0-7])+$");
    }

    public static boolean isHexadecimal(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("^(?:[a-fA-F0-9])+$");
    }

    public static boolean isValidInput(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("^(?:[a-fA-F0-9])+$");
    }

    public static boolean isValidExpression(String input) {
        if (input.isEmpty()) {
            return false;
        }

        return input.matches("(?:[a-fA-F0-9]+(?:[-+*/][a-fA-F0-9]*)+[a-fA-F0-9]+)+$");
    }
}
