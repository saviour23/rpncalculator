package com.rpn.calculator.com.rpn.calculator.exception;

/**
 * Calculator Exception class
 */
public class CalculatorException extends Exception {
    public CalculatorException(String message) {
        super(message);
    }

    public CalculatorException(String message, Throwable e) {
        super(message, e);
    }
}