package ru.vladrus13.operation;

import ru.vladrus13.exception.WrongParamException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Special class for filter and map
 */
public class CallOperation extends Expression {
    protected final Expression expression;
    protected final Operation operation;

    /**
     * Constructor for filter or map
     *
     * @param a         {@link Expression} on filter or map
     * @param operation type of {@link Operation}
     */
    public CallOperation(Expression a, Operation operation) throws WrongParamException {
        this.expression = a;
        this.operation = operation;
        type = TYPE.CALL;
        if (operation == Operation.MAP && !(a.type == TYPE.VARIABLE_INTEGER || a.type == TYPE.INTEGER)) {
            throw new WrongParamException(a.toString() + " in map call");
        }
        if (operation == Operation.FILTER && !(a.type == TYPE.LOGICAL || a.type == TYPE.VARIABLE_LOGICAL)) {
            throw new WrongParamException(a.toString() + " in filter call");
        }
    }

    /**
     * Get {@link Operation} of call class
     *
     * @return {@link} of call class
     */
    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        String name;
        switch (operation) {
            case MAP:
                name = "map";
                break;
            case FILTER:
                name = "filter";
                break;
            default:
                name = "";
                break;
        }
        return name + "{" + expression.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallOperation that = (CallOperation) o;
        return Objects.equals(expression, that.expression) &&
                operation == that.operation;
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.singletonList(expression);
    }

}
