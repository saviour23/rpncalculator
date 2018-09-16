package com.rpn.calculator.com.rpn.calculator.parser;

import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InputParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testParserInputForOperator() {

        try {
            Double returnValue = InputParser.parseInput("+");
            Assert.assertEquals(null, returnValue);
        } catch (CalculatorException e) {
            Assert.fail();
        }

    }

    @Test
    public void testParserInputForDigit() {

        try {
            Double returnValue = InputParser.parseInput("2");
            Assert.assertEquals(Double.valueOf("2"), returnValue);
        } catch (CalculatorException e) {
            Assert.fail();
        }

    }

    @Test
    public void testParserInputForInvalidValue() throws CalculatorException {

        exception.expect(CalculatorException.class);
        Double returnValue = InputParser.parseInput("2a");

    }
}
