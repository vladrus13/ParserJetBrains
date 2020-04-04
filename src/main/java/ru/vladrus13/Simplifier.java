package ru.vladrus13;

import ru.vladrus13.exception.WrongParamException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for simplify {@link Expression}
 */
public class Simplifier {

    /**
     * Try to simplify expression
     *
     * @param expression input {@link Expression}
     * @return simplified {@link Expression}
     * @throws WrongParamException if we got something bad until simplifying
     */
    private Expression recursiveSimplifier(Expression expression) throws WrongParamException {
        if (expression instanceof BinaryOperation) {
            Expression newFirst = recursiveSimplifier(expression.getExpressions().get(0));
            Expression newSecond = recursiveSimplifier(expression.getExpressions().get(1));
            if (newFirst instanceof Const && newSecond instanceof Const) {
                switch (((BinaryOperation) expression).getOperation()) {
                    case SUBTRACT:
                        return new Const(((Const) newFirst).getNumber().subtract(((Const) newSecond).getNumber()));
                    case MULTIPLY:
                        return new Const(((Const) newFirst).getNumber().multiply(((Const) newSecond).getNumber()));
                    case ADD:
                        return new Const(((Const) newFirst).getNumber().add(((Const) newSecond).getNumber()));
                    default:
                        return new BinaryOperation(newFirst, newSecond, ((BinaryOperation) expression).getOperation());
                }
            } else {
                return new BinaryOperation(newFirst, newSecond, ((BinaryOperation) expression).getOperation());
            }
        }
        if (expression instanceof Negate) {
            Expression newExpression = recursiveSimplifier(expression.getExpressions().get(0));
            if (newExpression instanceof Const) {
                return new Const(((Const) newExpression).getNumber());
            }
            return newExpression;
        }
        // if (expression instanceof Variable || expression instanceof Const) {
        return expression;
    }

    /**
     * Try to simplify {@link CallOperation}
     *
     * @param callOperation input {@link CallOperation}
     * @return simplified {@link CallOperation}
     * @throws WrongParamException if we got something bad until simplifying
     */
    private CallOperation recursiveSimplifier(CallOperation callOperation) throws WrongParamException {
        return new CallOperation(recursiveSimplifier(callOperation.getExpressions().get(0)), callOperation.getOperation());
    }

    /**
     * Replace all variables in first arg on second
     *
     * @param in        where we replace
     * @param replacing to what we replace
     * @return right {@link Expression}
     * @throws WrongParamException if replace not successful
     */
    private Expression replaceAllVariables(Expression in, Expression replacing) throws WrongParamException {
        if (in instanceof Variable) return replacing;
        if (in instanceof Negate) {
            return new Negate(replaceAllVariables(in.getExpressions().get(0), replacing));
        }
        if (in instanceof BinaryOperation) {
            return new BinaryOperation(replaceAllVariables(in.getExpressions().get(0), replacing),
                    replaceAllVariables(in.getExpressions().get(1), replacing), ((BinaryOperation) in).getOperation());
        }
        return in;
    }

    /**
     * Simplify pair of map and map
     *
     * @param a {@link CallOperation} and have {@link Operation} map
     * @param b {@link CallOperation} and have {@link Operation} map
     * @return list of one argument - simplifying {@link CallOperation}
     * @throws WrongParamException if we get replace bad
     */
    private List<CallOperation> simplifyMapAndMap(CallOperation a, CallOperation b) throws WrongParamException {
        return Collections.singletonList(new CallOperation(replaceAllVariables(b.getExpressions().get(0), a.getExpressions().get(0)), Operation.MAP));
    }

    /**
     * Simplify pair of map and filter
     *
     * @param a {@link CallOperation} and have {@link Operation} map
     * @param b {@link CallOperation} and have {@link Operation} filter
     * @return list of two argument (filter and map) - simplifying {@link CallOperation}
     * @throws WrongParamException if we get replace bad
     */
    private List<CallOperation> simplifyMapAndFilter(CallOperation a, CallOperation b) throws WrongParamException {
        return Arrays.asList(new CallOperation(replaceAllVariables(b.getExpressions().get(0), a.getExpressions().get(0)), Operation.FILTER), a);
    }

    /**
     * Simplify pair of filter and filter
     *
     * @param a {@link CallOperation} and have {@link Operation} filter
     * @param b {@link CallOperation} and have {@link Operation} filter
     * @return list of one argument - simplifying {@link CallOperation}
     * @throws WrongParamException if we get replace bad
     */
    private List<CallOperation> simplifyFilterAndFilter(CallOperation a, CallOperation b) throws WrongParamException {
        return Collections.singletonList(new CallOperation(new BinaryOperation(a.getExpressions().get(0), b.getExpressions().get(0), Operation.AND), Operation.FILTER));
    }

    /**
     * Simplify pair of {@link CallOperation}
     *
     * @param a {@link CallOperation}
     * @param b {@link CallOperation}
     * @return list of simplifying {@link CallOperation}
     * @throws WrongParamException if we get replace bad
     */
    private List<CallOperation> simplify(CallOperation a, CallOperation b) throws WrongParamException {
        if (a.getOperation() == Operation.MAP && b.getOperation() == Operation.MAP) {
            return simplifyMapAndMap(a, b);
        }
        if (a.getOperation() == Operation.MAP && b.getOperation() == Operation.FILTER) {
            return simplifyMapAndFilter(a, b);
        }
        if (a.getOperation() == Operation.FILTER && b.getOperation() == Operation.FILTER) {
            return simplifyFilterAndFilter(a, b);
        }
        return Arrays.asList(a, b);
    }

    /**
     * Checks, is list simplified
     *
     * @param list {@link List} which we check
     * @return result of check
     */
    private boolean isSimplified(List<CallOperation> list) {
        if (list.size() > 2) return false;
        if (list.size() == 2) {
            return list.get(0).getOperation() == Operation.FILTER && list.get(1).getOperation() == Operation.MAP;
        }
        return true;
    }

    /**
     * Simplify list of {@link CallOperation}, which we simplify
     *
     * @param list list which we check
     * @return list of two elements - filter and map
     * @throws WrongParamException if we get replace bad
     */
    public List<CallOperation> simplify(List<CallOperation> list) throws WrongParamException {
        List<CallOperation> newList = list;
        while (!isSimplified(newList)) {
            List<CallOperation> simple = new ArrayList<>();
            CallOperation last = newList.get(0);
            for (int i = 1; i < newList.size(); i++) {
                List<CallOperation> pair = simplify(last, newList.get(i));
                if (pair.size() == 2) {
                    simple.add(pair.get(0));
                }
                last = pair.get(pair.size() - 1);
            }
            simple.add(last);
            newList = simple;
        }
        if (newList.size() == 0) {
            newList = Arrays.asList(new CallOperation(new BinaryOperation(new Const(new BigInteger("0")), new Const(new BigInteger("0")), Operation.EQUALS), Operation.FILTER),
                    new CallOperation(new Variable("element"), Operation.MAP));
        }
        if (newList.size() == 1) {
            if (newList.get(0).getOperation() == Operation.MAP) {
                newList.add(0, new CallOperation(new BinaryOperation(new Const(new BigInteger("0")), new Const(new BigInteger("0")), Operation.EQUALS), Operation.FILTER));
            } else {
                newList.add(new CallOperation(new Variable("element"), Operation.MAP));
            }
        }
        newList.set(0, recursiveSimplifier(newList.get(0)));
        newList.set(1, recursiveSimplifier(newList.get(1)));
        return newList;
    }
}
