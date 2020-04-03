package ru.vladrus13;

import ru.vladrus13.exception.WrongParamException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Simplifier {

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

    private List<CallOperation> simplifyMapAndMap(CallOperation a, CallOperation b) throws WrongParamException {
        return Collections.singletonList(new CallOperation(replaceAllVariables(b.getExpressions().get(0), a.getExpressions().get(0)), Operation.MAP));
    }

    private List<CallOperation> simplifyMapAndFilter(CallOperation a, CallOperation b) throws WrongParamException {
        return Arrays.asList(new CallOperation(replaceAllVariables(b.getExpressions().get(0), a.getExpressions().get(0)), Operation.FILTER), a);
    }

    private List<CallOperation> simplifyFilterAndFilter(CallOperation a, CallOperation b) throws WrongParamException {
        return Collections.singletonList(new CallOperation(new BinaryOperation(a.getExpressions().get(0), b.getExpressions().get(0), Operation.AND), Operation.FILTER));
    }

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

    private boolean isSimplified(List<CallOperation> list) {
        if (list.size() > 2) return false;
        if (list.size() == 2) {
            return list.get(0).getOperation() == Operation.FILTER && list.get(1).getOperation() == Operation.MAP;
        }
        return true;
    }

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
        return newList;
    }
}
