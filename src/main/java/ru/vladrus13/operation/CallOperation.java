package ru.vladrus13.operation;

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
    public CallOperation(Expression a, Operation operation) {
        this.expression = a;
        this.operation = operation;
        type = TYPE.LOGICAL;
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
