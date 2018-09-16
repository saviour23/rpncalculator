package com.rpn.calculator;

import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import com.rpn.calculator.com.rpn.calculator.service.Calculator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class RpnCalculatorTest {


    private RpnCalculator rpnCalc;
    private Calculator calculator;

    @Before
    public void setup() {
        rpnCalc = new RpnCalculator();
        calculator = Calculator.getInstance();
    }

    /**
     * Testing for execute method
     */
    @Test
    public void testExecuteForPlace() {
        try {
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2 3 +");
            Assert.assertEquals("5", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }
    }


    @Test
    public void testNegative() {
        try {
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2a");
            Assert.assertEquals("invalid operator", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }


    }

    @Test
    public void testNegativeSpecialCase() {
        try {
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2 3 + +");
            Assert.assertEquals("operator + (position: 7): insufficient parameters\n" +
                    "5", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }


    }


    @After
    public void tearDown() {
        //clearing values from calculator stack
        try {
            calculator.eval("clear");
        } catch (CalculatorException e) {
            //not required as if stack is empty as we try to clear it, it throws exception
        }

    }
}
