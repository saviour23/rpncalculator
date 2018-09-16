package com.rpn.calculator.service;


import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import com.rpn.calculator.com.rpn.calculator.service.Calculator;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CalculatorTest {

    private Calculator calculator = null;

    // we need new instance of calculator before each test case
    @Before
    public void setUp() {
        calculator = Calculator.getInstance();
    }

    @After
    public void tearDown() {
        calculator = null;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testOperators() {

        try {
            calculator.eval("5 2");
            assertEquals(new BigDecimal(5), calculator.getValueFromIndex(0));
            assertEquals(new BigDecimal(2), calculator.getValueFromIndex(1));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSubstraction() throws CalculatorException {

        calculator.eval("5 2 -");
        assertEquals(1, calculator.getSize());
        assertEquals(new BigDecimal(3).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
        calculator.eval("3 -");
        assertEquals(1, calculator.getSize());
        assertEquals(BigDecimal.ZERO.setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
    }

    @Test
    public void testSubstractionWithNegative() {


        try {
            calculator.eval("1 2 3 4 5 *");
            assertEquals(4, calculator.getSize());
            calculator.eval("clear 3 4 -");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(-1).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testMultiplication() {

        try {
            calculator.eval("1 2 3 4 5");
            calculator.eval("* * * *");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(120).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testDivision() {

        try {
            calculator.eval("7 12 2 /");
            assertEquals(new BigDecimal(7), calculator.getValueFromIndex(0));
            assertEquals(new BigDecimal(6).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(1));
            calculator.eval("*");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(42).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
            calculator.eval("4 /");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(10.5).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSqrt() {

        try {
            calculator.eval("2 sqrt");
            calculator.eval("clear 9 sqrt");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(3).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
        } catch (Exception e) {

        }
    }

    @Test
    public void testInsuficientParameters() {
        try {
            calculator.eval("1 2 3 * 5 + * * 6 5");
        } catch (CalculatorException e) {
            assertEquals("operator * (position: 15): insufficient parameters", e.getMessage());
        }
        assertEquals(1, calculator.getSize());
        assertEquals(new BigDecimal(11).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
    }

    @Test
    public void testUndo() {

        try {
            calculator.eval("5 4 3 2");
            assertEquals(4, calculator.getSize());
            calculator.eval("undo undo *");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(20).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
            calculator.eval("5 *");
            assertEquals(1, calculator.getSize());
            assertEquals(new BigDecimal(100).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
            calculator.eval("undo");
            assertEquals(2, calculator.getSize());
            assertEquals(new BigDecimal(20).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
            assertEquals(new BigDecimal(5), calculator.getValueFromIndex(1));
            calculator.eval("+ undo - undo / undo * undo sqrt undo pow undo");
            assertEquals(2, calculator.getSize());
            assertEquals(new BigDecimal(20).setScale(15,RoundingMode.HALF_UP), calculator.getValueFromIndex(0));
            assertEquals(new BigDecimal(5), calculator.getValueFromIndex(1));
        } catch (Exception e) {
            fail();
        }


    }

    @Test
    public void testOnlyOperators() throws CalculatorException {
        exception.expect(CalculatorException.class);
        calculator.eval("+ +");

    }

    @Test
    public void testInvalidCharacters() throws CalculatorException {

        exception.expect(CalculatorException.class);
        calculator.eval("2 a +");

    }

    @Test
    public void testNoSpaces() throws CalculatorException {
        exception.expect(CalculatorException.class);
        calculator.eval("2 3 +");
        calculator.eval("22+");

    }

    @Test
    public void testNoSpaces2() throws CalculatorException {
        exception.expect(CalculatorException.class);
        calculator.eval("2 2+ 3");

    }

    @Test
    public void testDivideByZero() throws CalculatorException {
        exception.expect(CalculatorException.class);
        calculator.eval("1 0 /");


    }

    @Test
    public void testNullInput() throws CalculatorException {
        exception.expect(CalculatorException.class);
        calculator.eval(null);

    }
}
