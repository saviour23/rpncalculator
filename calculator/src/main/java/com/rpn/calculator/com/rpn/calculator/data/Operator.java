package com.rpn.calculator.com.rpn.calculator.data;

import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;


/**
 * Enum storing all operators and define the operations to be performed based on operator.
 */
public enum Operator {

    ADDITION("+", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws CalculatorException {
            return secondOperand.add(firstOperand).setScale(15, RoundingMode.HALF_UP);
        }
    },

    SUBTRACTION("-", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return secondOperand.subtract(firstOperand).setScale(15, RoundingMode.HALF_UP);
        }
    },

    MULTIPLICATION("*", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return secondOperand.multiply(firstOperand).setScale(15, RoundingMode.HALF_UP);
        }
    },

    DIVISION("/", 2) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws CalculatorException {
            if (firstOperand.equals(BigDecimal.ZERO)) {
                throw new CalculatorException("Cannot divide by 0.");
            }
            return secondOperand.divide(firstOperand).setScale(15, RoundingMode.HALF_UP);
        }
    },

    SQUAREROOT("sqrt", 1) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return Operator.sqrtBigDecimal(firstOperand, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE)).setScale(15,RoundingMode.HALF_UP);
        }
    },

    POWER("pow", 1) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
            return firstOperand.pow(2).setScale(15, RoundingMode.HALF_UP);
        }
    },

    UNDO("undo", 0) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws CalculatorException {
            throw new CalculatorException("Invalid operation");
        }
    },

    CLEAR("clear", 0) {
        public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws CalculatorException {
            throw new CalculatorException("Invalid operation");
        }
    };


    // maintaining cache values for operator symbol and operator for faster lookup as we always need to fetch operator based on symbol
    private static final Map<String, Operator> operatorEnumCache = new HashMap<>();

    static {
        for (Operator o : Operator.values()) {
            operatorEnumCache.put(o.getSymbol(), o);
        }
    }

    private String symbol;
    private int operandsNumber;

    Operator(String symbol, int operandsNumber) {
        this.symbol = symbol;
        this.operandsNumber = operandsNumber;
    }

    public abstract BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) throws CalculatorException;

    public String getSymbol() {
        return symbol;
    }

    public int getOperandsNumber() {
        return operandsNumber;
    }

    public static Operator getEnumFromString(String value) {
        return operatorEnumCache.get(value);
    }

    private static final BigDecimal SQRT_DIG = new BigDecimal(50);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    /**
     * method used to compute the square root of a BigDecimal.
     * "Newton–Raphson method"
     */
    private static BigDecimal sqrtBigDecimal(BigDecimal c, BigDecimal xn, BigDecimal precision) {
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1) {
            return xn1;
        }
        return sqrtBigDecimal(c, xn1, precision);
    }


}