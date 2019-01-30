package dhamith.me.numcalc;

import android.text.Html;
import android.util.Log;

public class ConversionDisplay {

    private static String decResult, binResult, octResult, hexResult;

    public static String buildForDecimal(String input, int radix) throws IllegalArgumentException {
        if (!StringUtils.isValidInput(input)) {
            throw new IllegalArgumentException("not_valid_input");
        }

        int upperBound = input.length() - 1;
        long finalRes = Long.parseLong(input, radix);
        StringBuilder outputBuilder = new StringBuilder();

        decResult = String.valueOf(finalRes);

        outputBuilder.append("<strong>").append(finalRes).append("</strong>").append(" = ");

        // traverse the string in reverse
        for (int i = upperBound, j = 0; i >= 0; i--, j++) {
            String s = String.valueOf(input.charAt(i));
            String exponent = StringUtils.getUnicodeSuperscript(j);

            if (radix == 16) {
                s = StringUtils.replaceHexWithDecimal(s);
            }

            outputBuilder
                    .append(s)
                    .append("×")
                    .append(radix)
                    .append(exponent);

            if (i != 0) {
                outputBuilder.append(" + ");
            }
        }

        return outputBuilder.toString();
    }

    public static String buildForOther(String input, int radix) {
        if (!StringUtils.isDecimal(input)) {
            throw new IllegalArgumentException("not_decimal_input");
        }

        StringBuilder outputBuilder = new StringBuilder();

        long mod = 0;
        long div = Long.parseLong(input);;
        String finalRes = Long.toString(div, radix).toUpperCase();

        switch (radix) {
            case 2:
                binResult = finalRes;
                break;
            case 8:
                octResult = finalRes;
                break;
            case 16:
                hexResult = finalRes;
                break;
        }

        outputBuilder
                .append("<strong>")
                .append(finalRes)
                .append("</strong><br><br>");

        while (div != 0) {
            long org = div;
            mod = div % radix;
            div /= radix;

            String s = org + " ÷ " + radix + " = ";
            s = String.format("%1$" + (input.length() + 8) + "s", String.valueOf(s))
                    .replace(" ", "&nbsp;");

            outputBuilder.append(s);

            s = String.format("%1$" + input.length()+ "s", String.valueOf(div))
                    .replace(" ", "&nbsp;")
                    + "(<strong><font color='red'>"
                    + StringUtils.replaceDecimalWithHex(String.valueOf(mod))
                    + "</font></strong>)<br>";

            outputBuilder.append(s);
        }

        return outputBuilder.toString();
    }

    public static String getDecResult() {
        return decResult;
    }

    public static String getBinResult() {
        return binResult;
    }

    public static String getOctResult() {
        return octResult;
    }

    public static String getHexResult() {
        return hexResult;
    }
}
