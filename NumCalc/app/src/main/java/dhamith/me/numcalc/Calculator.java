package dhamith.me.numcalc;

class Calculator {
    private final int radix;

    Calculator(int radix) {
        this.radix = radix;
    }

    String solve(String op, String a, String b) throws IllegalArgumentException {
        long n1 = Long.parseLong(a, radix);
        long n2 = Long.parseLong(b, radix);

        switch (op) {
            case "+":
                return Long.toString((n1 + n2), radix);

            case "-":
                return Long.toString((n1 - n2), radix);

            case "*":
                return Long.toString((n1 * n2), radix);

            case "/":
                if (n2 == 0) {
                    throw new IllegalArgumentException("divide_by_0");
                }
                return Long.toString((n1 / n2), radix);

            default:
                throw new IllegalArgumentException("unknown_op");
        }
    }
}
