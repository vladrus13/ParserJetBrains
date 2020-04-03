package ru.vladrus13.operation;

import java.util.List;

public abstract class Expression {
    protected TYPE type;

    @Override
    public String toString() {
        return type.toString();
    }

    public abstract boolean equals(Object o);

    public abstract List<Expression> getExpressions();
}
