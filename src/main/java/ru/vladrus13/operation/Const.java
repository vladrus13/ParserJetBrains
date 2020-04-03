package ru.vladrus13.operation;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Const extends Expression {
    protected final BigInteger number;

    public Const(BigInteger number) {
        this.number = number;
        type = TYPE.INTEGER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const aConst = (Const) o;
        return Objects.equals(number, aConst.number);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
