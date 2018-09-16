package com.rpn.calculator;


import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import com.rpn.calculator.com.rpn.calculator.service.Calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RpnCalculator {

    private static Boolean keepRunning;

    public static void main(String[] args) throws CalculatorException {
        RpnCalculator calcMainObject = new RpnCalculator();
        calcMainObject.startCalculator();
    }

    private void startCalculator() throws CalculatorException {
        Calculator calculator = Calculator.getInstance();
        BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter your expression or 'Exit' to quit: ");
        keepRunning = true;
        String inputString = "";
        while (keepRunning) {
            try {
                inputString = commandLineInput.readLine();
                System.out.println(execute(inputString));
            } catch (Exception e) {
                throw new CalculatorException("Exception in reading from console", e);
            }
        }

    }

    private String execute(String inputString) {
        StringBuilder sb = new StringBuilder();
        Calculator calculator = Calculator.getInstance();
        if ("exit".equalsIgnoreCase(inputString)) {
            keepRunning = false;
        } else {
            try {
                calculator.eval(inputString);
            } catch (CalculatorException e) {
                sb.append(e.getMessage()).append("\n");
            }

            DecimalFormat fmt = new DecimalFormat("0.##########");
            for (BigDecimal value : calculator.getValues()) {
                sb.append(fmt.format(value));

            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
