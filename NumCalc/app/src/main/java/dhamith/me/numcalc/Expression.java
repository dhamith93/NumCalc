package dhamith.me.numcalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

class Expression {
    private final ArrayList<Node> expression;
    private final HashMap<String, Integer> order;
    private final String input;
    private final int radix;

    private class Node {
        String value;
        boolean isOp;

        Node(String value, boolean isOp) {
            this.value = value;
            this.isOp = isOp;
        }
    }

    Expression(String input, int radix) {
        this.input = input;
        this.radix = radix;

        expression = new ArrayList<>();
        order = new HashMap<>();

        order.put("+", 1);
        order.put("-", 1);
        order.put("*", 2);
        order.put("/", 2);
    }

    private boolean isOp(char c) {
        return (c == '+' || c == '-' || c == '*' || c == '/');
    }

    private void buildPostfixExpression(String input) {
        int strLen = input.length();
        StringBuilder operand = new StringBuilder();
        Stack<Node> opStack = new Stack<>();

        for (int i = 0; i < strLen; i++) {
            char c = input.charAt(i);

            if (isOp(c)) {
                String op = Character.toString(c);
                expression.add(new Node(operand.toString(), false));

                while (!opStack.empty() && (order.get(opStack.peek().value) >= order.get(op))) {
                    expression.add(opStack.pop());
                }

                opStack.push(new Node(op, true));
                operand.setLength(0);
            } else {
                operand.append(c);

                if (i == (strLen - 1)) expression.add(new Node(operand.toString(), false));
            }
        }

        while (!opStack.empty()) {
            expression.add(opStack.pop());
        }
    }

    private void buildInfixExpression(String input) {
        int strLen = input.length();
        StringBuilder operand = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            char c = input.charAt(i);

            if (isOp(c)) {
                expression.add(new Node(operand.toString(), false));
                expression.add(new Node(Character.toString(c), true));
                operand.setLength(0);
            } else {
                operand.append(c);

                if (i == (strLen - 1)) expression.add(new Node(operand.toString(), false));
            }
        }
    }

    String result() {
        if (!StringUtils.isValidExpression(input)) throw new RuntimeException("Invalid Expression");

        buildPostfixExpression(input);

        Stack<String> tempStack = new Stack<>();
        Calculator calculator = new Calculator(radix);

        for (Node n: expression) {
            if (!n.isOp) {
                tempStack.push(n.value);
                continue;
            }

            String n2 = tempStack.pop();
            String n1 = tempStack.pop();

            tempStack.push(calculator.solve(n.value, n1, n2));
        }

        return tempStack.pop();
    }

    String convert(int outputRadix) {
        buildInfixExpression(input);

        StringBuilder output = new StringBuilder();

        for (Node n: expression) {
            if (!n.isOp) {
                long v = Long.parseLong(n.value, radix);
                n.value = Long.toString(v, outputRadix);
            }

            output.append(n.value);
        }

        return output.toString();
    }
}
