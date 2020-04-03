package ru.vladrus13;

import ru.vladrus13.exception.BracketBalanceException;
import ru.vladrus13.exception.MissingOperatorException;
import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.exception.UnknownOperationException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

public class Parser {

    private String variableName;
    private String data;
    private int index = 0;
    private BigInteger value = BigInteger.ZERO;
    private int balance = 0;
    private Token current;
    private final Set<Token> operators = EnumSet.of(Token.ADD, Token.MULTIPLY, Token.SUBTRACT, Token.AND, Token.OR, Token.EQUALS, Token.MORE, Token.LESS);

    /**
     * Skip useless spaces
     */
    private void skipSpaces() {
        while (index < data.length() && Character.isWhitespace(data.charAt(index))) {
            index++;
        }
    }

    /**
     * Check for next operation
     *
     * @throws MissingOperatorException if we have NOT potential operation
     */
    private void checkOperation() throws MissingOperatorException {
        if (current == Token.CLOSE_BRACKET || current == Token.VARIABLE || current == Token.CONST) {
            throw new MissingOperatorException("Start from operator at position: " + index);
        }
    }

    /**
     * Check for next operator
     *
     * @throws MissingOperatorException if we have NOT potential operator
     */
    private void checkOperator() throws MissingOperatorException {
        if (operators.contains(current) || current == Token.OPEN_BRACKET) {
            throw new MissingOperatorException("Missing second value at position: " + index);
        }
    }

    /**
     * Save to current next {@link Token}.
     *
     * @throws ParsingException if we have some troubles with parsing.
     */
    private void nextToken() throws ParsingException {
        skipSpaces();
        if (index >= data.length()) {
            checkOperator();
            current = Token.END;
            return;
        }
        char currentCharacter = data.charAt(index);
        index++;
        switch (currentCharacter) {
            case '+':
                checkOperator();
                current = Token.ADD;
                return;
            case '-':
                if (current == Token.VARIABLE || current == Token.CONST || current == Token.CLOSE_BRACKET) {
                    current = Token.SUBTRACT;
                    return;
                } else {
                    if (index >= data.length()) {
                        throw new MissingOperatorException("No value after minus. \n\tPosition at line: " + index);
                    }
                    if (Character.isDigit(data.charAt(index))) {
                        int beginIndex = index;
                        while (index < data.length() && Character.isDigit(data.charAt(index))) {
                            index++;
                        }
                        value = new BigInteger(data.substring(beginIndex, index));
                        current = Token.CONST;
                        return;
                    } else {
                        current = Token.NEGATE;
                        return;
                    }
                }
            case '*':
                checkOperator();
                current = Token.MULTIPLY;
                return;
            case '&':
                checkOperator();
                current = Token.AND;
                return;
            case '|':
                checkOperator();
                current = Token.OR;
                return;
            case '=':
                checkOperator();
                current = Token.EQUALS;
                return;
            case '<':
                checkOperator();
                current = Token.LESS;
                return;
            case '>':
                checkOperator();
                current = Token.MORE;
                return;
            case '(':
                checkOperation();
                balance++;
                current = Token.OPEN_BRACKET;
                return;
            case ')':
                if (operators.contains(current) || current == Token.OPEN_BRACKET) {
                    throw new MissingOperatorException("Operator before bracket at position: " + index);
                }
                balance--;
                if (balance < 0) {
                    throw new BracketBalanceException("Negate balance at position: " + index);
                }
                current = Token.CLOSE_BRACKET;
                return;
            default:
                if (Character.isDigit(currentCharacter)) {
                    index--;
                    int beginIndex = index;
                    while (index < data.length() && Character.isDigit(data.charAt(index))) {
                        index++;
                    }
                    value = new BigInteger(data.substring(beginIndex, index));
                    current = Token.CONST;
                } else {
                    if (Character.isLetter(currentCharacter)) {
                        index--;
                        int beginChar = index;
                        while (index < data.length() && Character.isLetter(data.charAt(index))) {
                            index++;
                        }
                        variableName = data.substring(beginChar, index);
                        if (variableName.equals("filter")) {
                            index++;
                            current = Token.FILTER;
                            return;
                        }
                        if (variableName.equals("map")) {
                            index++;
                            current = Token.MAP;
                            return;
                        }
                        if (variableName.equals("element")) {
                            current = Token.VARIABLE;
                            return;
                        }
                        throw new UnknownOperationException("Unknown operator \"" + variableName + "\" at position: " + index);
                    } else {
                        if (currentCharacter == '}') {
                            index += 3;
                        }
                    }
                }
        }
    }

    /**
     * Return {@link Expression} next unary operation
     *
     * @return {@link Expression} next unary operation
     * @throws ParsingException if we have some troubles with parsing (maybe, unknown operator or problem with brackets)
     */
    private Expression unary() throws ParsingException {
        nextToken();
        Expression returned;
        switch (current) {
            case CONST:
                returned = new Const(value);
                nextToken();
                return returned;
            case VARIABLE:
                returned = new Variable(variableName);
                nextToken();
                return returned;
            case NEGATE:
                return new Negate(unary());
            case OPEN_BRACKET:
                int position = index;
                returned = andOrMoreEqualsLess();
                if (current != Token.CLOSE_BRACKET) {
                    throw new BracketBalanceException("Missed bracket at positions: " + position + "-" + index);
                }
                nextToken();
                return returned;
            case FILTER:
                return new CallOperation(andOrMoreEqualsLess(), Operation.FILTER);
            case MAP:
                return new CallOperation(andOrMoreEqualsLess(), Operation.MAP);
            default:
                throw new UnknownOperationException("Unknown operator at position: " + index);
        }
    }

    /**
     * Return multiply or higher level operation
     *
     * @return multiply or higher level {@link Expression}
     * @throws ParsingException if we have some troubles with parsing
     */
    private Expression multi() throws ParsingException {
        Expression returned = unary();
        while (true) {
            if (current == Token.MULTIPLY) {
                returned = new BinaryOperation(returned, unary(), Operation.MULTIPLY);
            } else {
                return returned;
            }
        }
    }

    /**
     * Return add or subtract or higher level operation
     *
     * @return add or subtract or higher level {@link Expression}
     * @throws ParsingException if we have some troubles with parsing
     */
    private Expression addSub() throws ParsingException {
        Expression returned = multi();
        while (true) {
            switch (current) {
                case ADD:
                    returned = new BinaryOperation(returned, multi(), Operation.ADD);
                    break;
                case SUBTRACT:
                    returned = new BinaryOperation(returned, multi(), Operation.SUBTRACT);
                    break;
                default:
                    return returned;
            }
        }
    }

    /**
     * Return and, or, more, equals or higher level operation
     *
     * @return and, or, more, equals or higher level {@link Expression}
     * @throws ParsingException if we have some troubles with parsing
     */
    private Expression andOrMoreEqualsLess() throws ParsingException {
        Expression returned = addSub();
        while (true) {
            switch (current) {
                case AND:
                    returned = new BinaryOperation(returned, addSub(), Operation.AND);
                    break;
                case OR:
                    returned = new BinaryOperation(returned, addSub(), Operation.OR);
                    break;
                case LESS:
                    returned = new BinaryOperation(returned, addSub(), Operation.LESS);
                    break;
                case MORE:
                    returned = new BinaryOperation(returned, addSub(), Operation.MORE);
                    break;
                case EQUALS:
                    returned = new BinaryOperation(returned, addSub(), Operation.EQUALS);
                    break;
                default:
                    return returned;
            }
        }
    }

    /**
     * Return {@link ArrayList} of {@link CallOperation} parse
     *
     * @param in {@link String} for parsing
     * @return {@link ArrayList} of parsing {@link CallOperation}
     * @throws ParsingException if we get some troubles on parsing
     */
    public ArrayList<CallOperation> parse(String in) throws ParsingException {
        index = 0;
        data = in;
        balance = 0;
        current = Token.ERROR;
        ArrayList<CallOperation> expressions = new ArrayList<>();
        while (index < data.length()) {
            Expression get = andOrMoreEqualsLess();
            if (get instanceof CallOperation) {
                expressions.add((CallOperation) get);
            } else {
                throw new ParsingException(get.toString() + " is not call");
            }
        }
        return expressions;
    }

    /**
     * Token for successfully parsing strings
     */
    private enum Token {
        ADD, CLOSE_BRACKET, OPEN_BRACKET, MULTIPLY, SUBTRACT, CONST, VARIABLE, END, FILTER, MAP, AND, OR, EQUALS, MORE, LESS, ERROR, NEGATE
    }
}
