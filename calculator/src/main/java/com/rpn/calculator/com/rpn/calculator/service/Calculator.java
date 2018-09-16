package com.rpn.calculator.com.rpn.calculator.service;


import com.google.common.base.Strings;
import com.rpn.calculator.com.rpn.calculator.data.Operation;
import com.rpn.calculator.com.rpn.calculator.data.Operator;
import com.rpn.calculator.com.rpn.calculator.parser.InputParser;
import com.rpn.calculator.com.rpn.calculator.exception.CalculatorException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Calculator class responsible to do calculator operation based on user input
 * which can be to perform operation , to undo the last operation or to clear
 * calculator data.
 */
public class Calculator implements Serializable {

    // stack to store actual values of calculator.
    private final Stack<BigDecimal> valuesStack = new Stack<>();
    // stack for storing all successful performed operations.
    private final Stack<Operation> operationPerformedStack = new Stack<>();
    private AtomicInteger indexCounter;
    private static volatile Calculator calculatorInstance;

    /**
     * safe guarding calculator instance from reflection.
     * Throws Runtime exception if instance already there and someone tries to create it again
     */
    private Calculator() {

        if (calculatorInstance != null) {
            throw new RuntimeException("User getInstance() method to get object");
        }
    }

    /**
     * Method exposed to get the singleton instance of calculator
     *
     * @return
     */
    public static Calculator getInstance() {

        if (calculatorInstance == null) {

            synchronized (Calculator.class) {

                if (calculatorInstance == null) {
                    calculatorInstance = new Calculator();
                }
            }
        }

        return calculatorInstance;
    }

    /**
     * Processes a RPN string token
     *
     * @param token RPN token
     * @throws CalculatorException
     */
    private void processToken(String token) throws CalculatorException {
        Double value = InputParser.parseInput(token);
        if (value == null) {
            processOperator(token);
        } else {
            // it's a double value or digit
            valuesStack.push(new BigDecimal(token));
            operationPerformedStack.push(null);
        }
    }

    /**
     * Executes an operation on the stack
     *
     * @param operatorString RPN valid operator
     * @throws CalculatorException
     */
    private void processOperator(String operatorString) throws CalculatorException {

        // check if there is an empty stack
        if (valuesStack.isEmpty()) {
            throw new CalculatorException("empty stack");
        }

        // searching for the operator in cache, cannot use valueOf() as it throw IllegalArgumentException if value not found
        // cache lookup will be faster than enums's valueOf()
        Operator operator = Operator.getEnumFromString(operatorString);
        if (operator == null) {
            throw new CalculatorException("invalid operator");
        }

        // clear value stack and instructions stack
        if (operator == Operator.CLEAR) {
            clearStacks();
            return;
        }

        // undo evaluates the last instruction in stack
        if (operator == Operator.UNDO) {
            undoLastInstruction();
            return;
        }

        // Checking that there are enough operand for the operation
        if (operator.getOperandsNumber() > valuesStack.size()) {
            throwInvalidOperand(operatorString);
        }

        // getting operands
        BigDecimal firstOperand = valuesStack.pop();
        BigDecimal secondOperand = (operator.getOperandsNumber() > 1) ? valuesStack.pop() : null;
        // calculate
        BigDecimal result = operator.calculate(firstOperand, secondOperand);

        if (result != null) {
            valuesStack.push(result);
            Operator operatorValue = Operator.getEnumFromString(operatorString);
            if (operatorValue.getOperandsNumber() > 1) {
                operationPerformedStack.push(new Operation(operatorValue, firstOperand, secondOperand));
            } else {

                operationPerformedStack.push(new Operation(operatorValue, firstOperand));
            }
        }

    }

    private void undoLastInstruction() throws CalculatorException {
        if (operationPerformedStack.isEmpty()) {
            throw new CalculatorException("no operations to undo");
        }

        Operation lastOperation = operationPerformedStack.pop();
        if (lastOperation == null) {
            valuesStack.pop();
        } else {
            undo(lastOperation);
        }
    }

    /**
     * Undo operation will revert the last operation performed
     * Method takes last operation performed as input, remove the last element
     * added in value stack and put back the elements on which last operation
     * was performed.
     *
     * @param lastOperation
     */
    private void undo(Operation lastOperation) {
        valuesStack.pop();

        //Checking if last operation was performed on 2 elements then adding back both elements

        if (lastOperation.getOperator().getOperandsNumber() > 1) {
            valuesStack.push(lastOperation.getSecondOperand());
            valuesStack.push(lastOperation.getFirstOperand());
        } else {
            //if one element was there in last operation then adding back one element.
            valuesStack.push(lastOperation.getFirstOperand());
        }
    }

    /**
     * Method to clear both stacks i.e. valuStack and operationPerformedStack
     */
    private void clearStacks() {
        valuesStack.clear();
        operationPerformedStack.clear();
    }

    private void throwInvalidOperand(String operator) throws CalculatorException {
        throw new CalculatorException(
                String.format("operator %s (position: %d): insufficient parameters", operator, indexCounter.get()));
    }

    /**
     * Method to get current values  stack
     *
     * @return
     */
    public Stack<BigDecimal> getValues() {
        Stack<BigDecimal> copyStack = new Stack<>();
        copyStack.addAll(valuesStack);
        return copyStack;
    }


    /**
     * returns the size of valuesStack
     *
     * @return
     */
    public int getSize() {

        return valuesStack.size();
    }

    /**
     * returns the specific item from valuesStack for specific stack
     *
     * @param index index of the element to return
     */
    public BigDecimal getValueFromIndex(int index) {
        return valuesStack.get(index);
    }

    /**
     * Public method exposed to evaluate the RPN expression
     *
     * @param inputExpression valid RPN expression
     * @throws CalculatorException
     */
    public void eval(String inputExpression) throws CalculatorException {
        if (Strings.isNullOrEmpty(inputExpression)) {

            throw new CalculatorException("Input cannot be null or empty.");
        }
        indexCounter = new AtomicInteger(0);

        String[] result = inputExpression.split("\\s");
        for (String aResult : result) {
            indexCounter.incrementAndGet();
            processToken(aResult);
            //AS command is splited based on spaces so if everyting is successful then we increment it again by one for space.
            //for understanding please have a look at CalculatorTest file testInsuficientParameters() test case.
            indexCounter.incrementAndGet();
        }

    }


    /**
     * Safe guard calculator instance from serialize and deserialize operation.
     *
     * @return same calculator instance
     */
    protected Calculator readResolve() {
        return getInstance();
    }

}
