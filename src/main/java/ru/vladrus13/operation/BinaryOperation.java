package ru.vladrus13.operation;

import ru.vladrus13.exception.WrongParamException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BinaryOperation extends Expression {
    protected final Expression a, b;
    protected final Operation operation;

    public BinaryOperation(Expression a, Expression b, Operation operation) throws WrongParamException {
        this.a = a;
        this.b = b;
        this.operation = operation;
        switch (operation) {
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
                if ((b.type == TYPE.INTEGER || b.type == TYPE.VARIABLE_INTEGER) && (a.type == TYPE.INTEGER || a.type == TYPE.VARIABLE_INTEGER)) {
                    this.type = (a.type == TYPE.INTEGER && b.type == TYPE.INTEGER ? TYPE.INTEGER : TYPE.VARIABLE_INTEGER);
                } else {
                    throw new WrongParamException(a.toString() + " and " + b.toString() + " wrong");
                }
                break;
            case AND:
            case EQUALS:
            case MORE:
            case LESS:
            case OR:
                this.type = TYPE.VARINABLE_LOGCAL;
        }
    }

    public Operation getOperation() {
        return operation;
    }

    public String toString() {
        String operation;
        switch (this.operation) {
            case ADD:
                operation = "+";
                break;
            case OR:
                operation = "|";
                break;
            case AND:
                operation = "&";
                break;
            case LESS:
                operation = "<";
                break;
            case MORE:
                operation = ">";
                break;
            case EQUALS:
                operation = "=";
                break;
            case MULTIPLY:
                operation = "*";
                break;
            case SUBTRACT:
                operation = "-";
                break;
            default:
                operation = "";
                break;
        }
        return "(" + a.toString() + operation + b.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryOperation that = (BinaryOperation) o;
        return Objects.equals(a, that.a) &&
                Objects.equals(b, that.b) &&
                operation == that.operation;
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(a, b);
    }
}
