package com.rpn.calculator.com.rpn.calculator.parser;

import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;
import com.rpn.calculator.com.rpn.calculator.data.Operator;

/**
 * Class responsible for parsing the input parameter
 */
public class InputParser {

    /**
     * It will check if paramter is operator, value or unrecognized character.
     *
     * @param inputParameter
     * @return NULL for operator and Double for actual value
     * @throws CalculatorException if unrecognized string is passed
     */
    public static Double parseInput(String inputParameter) throws CalculatorException {
        try {
            for (Operator opr : Operator.values()) {
                if (opr.getSymbol().equalsIgnoreCase(inputParameter)) {
                    //if input string is matching with operator then returning null
                    return null;
                }

            }
            //try to parse value for double
            return Double.parseDouble(inputParameter);
        } catch (NumberFormatException nfe) {
            //exception is catched to handle case when worng input is passed like 22+, a etc
            throw new CalculatorException("invalid operator");
        }
    }

}
