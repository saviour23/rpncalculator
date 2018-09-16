package com.rpn.calculator;

import com.rpn.calculator.com.rpn.calculator.service.Calculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class RpnCalculatorTest {

    private static Calculator calc;

    /**
     * Testing for execute method
     */
    @Test
    public void testExecuteForPlace() {
        try {
            RpnCalculator rpnCalc = new RpnCalculator();
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2 3 +");
            Assert.assertEquals("5", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }
    }


    @Test
    public void testNegative() {
        try {
            RpnCalculator rpnCalc = new RpnCalculator();
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2a");
            Assert.assertEquals("invalid operator", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }


    }

    @Test
    public void testNegativeSpecialCase() {
        try {
            RpnCalculator rpnCalc = new RpnCalculator();
            String value = Whitebox.invokeMethod(rpnCalc, "execute", "2 3 + +");
            Assert.assertEquals("operator + (position: 7): insufficient parameters\n" +
                    "5", value.trim());
        } catch (Exception e) {
            Assert.fail("Exception occoured in parsing");
        }


    }
}
