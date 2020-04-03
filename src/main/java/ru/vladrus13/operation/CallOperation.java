package ru.vladrus13.operation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CallOperation extends Expression {
    protected final Expression expression;
    protected final Operation operation;

    public CallOperation(Expression a, Operation operation) {
        this.expression = a;
        this.operation = operation;
        type = TYPE.LOGICAL;
    }

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
