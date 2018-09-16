package com.rpn.calculator.service;

import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import com.rpn.calculator.com.rpn.calculator.service.Operator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

public class OperatorTest {
    public static BigDecimal firstOperand;
    public static BigDecimal secondOperand;

    @BeforeClass
    public static void setUp() {
        firstOperand = new BigDecimal(100);
        secondOperand = new BigDecimal(200);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testInvalidUndoOperations() throws CalculatorException {
        exception.expect(CalculatorException.class);
        Operator.UNDO.calculate(BigDecimal.ZERO, BigDecimal.ZERO);
        Operator.CLEAR.calculate(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    public void testInvalidClearOperations() throws CalculatorException {
        exception.expect(CalculatorException.class);
        Operator.CLEAR.calculate(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    public void testDivideByZero() throws CalculatorException {
        exception.expect(CalculatorException.class);
        Operator.DIVISION.calculate(BigDecimal.ZERO, BigDecimal.ZERO);
    }


    @Test
    public void testCalculateAddition() throws CalculatorException {
        assertEquals(secondOperand.add(firstOperand).setScale(15, RoundingMode.HALF_UP), Operator.ADDITION.calculate(firstOperand, secondOperand));
    }

    @Test
    public void testCalculateSubstraction() throws CalculatorException {
        assertEquals(secondOperand.subtract(firstOperand).setScale(15, RoundingMode.HALF_UP), Operator.SUBTRACTION.calculate(firstOperand, secondOperand));
    }

    @Test
    public void testCalculateMultiplication() throws CalculatorException {
        assertEquals(secondOperand.multiply(firstOperand).setScale(15, RoundingMode.HALF_UP), Operator.MULTIPLICATION.calculate(firstOperand, secondOperand));
    }

    @Test
    public void testCalculateDevision() throws CalculatorException {
        assertEquals(secondOperand.divide(firstOperand).setScale(15, RoundingMode.HALF_UP), Operator.DIVISION.calculate(firstOperand, secondOperand));
    }

    @Test
    public void testCalculatePower() throws CalculatorException {
        assertEquals(firstOperand.pow(2).setScale(15, RoundingMode.HALF_UP), Operator.POWER.calculate(firstOperand, null));
    }


    @Test
    public void testCalculateSquareroot() throws CalculatorException {
        assertEquals("14.142135623730950", Operator.SQUAREROOT.calculate(secondOperand, null).toString());
    }


}
