package ru.vladrus13.operation;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Special class for constants
 */
public class Const extends Expression {
    protected final BigInteger number;

    public BigInteger getNumber() {
        return number;
    }

    /**
     * Constructor of Const
     *
     * @param number {@link BigInteger} - constant
     */
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
