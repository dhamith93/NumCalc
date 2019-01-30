package dhamith.me.numcalc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    @Test
    public void test1() {
        Expression expression = new Expression("2+4*5*6*40/10", 10);
        assertEquals("482", expression.result());
    }

    @Test
    public void test2() {
        Expression expression = new Expression("10+100*101*110*101000/1010", 2);
        assertEquals("111100010", expression.result());
    }

    @Test
    public void test3() {
        Expression expression = new Expression("2+4*5*6*50/12", 8);
        assertEquals("742", expression.result());
    }

    @Test
    public void test4() {
        Expression expression = new Expression("2+4*5*6*28/A", 16);
        assertEquals("1E2", expression.result().toUpperCase());
    }

    @Test
    public void test5() {
        Expression expression = new Expression("2+A", 16);
        assertEquals("C", expression.result().toUpperCase());
    }

    @Test
    public void test6() {
        Expression expression = new Expression("2+4", 10);
        assertEquals("6", expression.result().toUpperCase());
    }

    @Test
    public void test7() {
        Expression expression = new Expression("2+4", 8);
        assertEquals("6", expression.result().toUpperCase());
    }

    @Test
    public void test8() {
        Expression expression = new Expression("1010-1010", 2);
        assertEquals("0", expression.result().toUpperCase());
    }

    @Test
    public void test9() {
        Expression expression = new Expression("5-5", 8);
        assertEquals("0", expression.result().toUpperCase());
    }

    @Test
    public void test10() {
        Expression expression = new Expression("A+B+C*D+E*F+1/2+3+4*5+6-7+8+9", 16);
        assertEquals("1AA", expression.result().toUpperCase());
    }

    @Test
    public void test11() {
        Expression expression = new Expression("10/1+1000000-10*101010", 2);
        assertEquals("-10010", expression.result().toUpperCase());
    }
}
