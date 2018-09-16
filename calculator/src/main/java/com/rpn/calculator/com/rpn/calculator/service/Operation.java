package com.rpn.calculator.com.rpn.calculator.service;

import java.math.BigDecimal;

/**
 * Class that maintains the action performed on calculator
 */
public class Operation {
    // to maintain the operator used
    Operator operator;
    //operands used in calculator action
    BigDecimal firstOperand, secondOperand;

    /**
     * constructor used when operation is performed on only one value like sqrt, pow
     * @param operator
     * @param firstOperand
     */
    public Operation(Operator operator, BigDecimal firstOperand) {

        this.operator = operator;
        this.firstOperand = firstOperand;
    }

    /**
     * constructor used when operation is performed on two value like + . -, * etc
     * @param operator
     * @param firstOperand
     */
    public Operation(Operator operator, BigDecimal firstOperand, BigDecimal secondOperand) {
        this(operator, firstOperand);
        this.secondOperand = secondOperand;
    }

}
