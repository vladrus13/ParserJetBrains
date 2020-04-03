package ru.vladrus13.operation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class for negate expressions ("-(2 + 2)")
 */
public class Negate extends Expression {
    protected final Expression a;

    /**
     * Constructor of negate
     *
     * @param a {@link Expression}
     */
    public Negate(Expression a) {
        this.a = a;
        type = a.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negate negate = (Negate) o;
        return Objects.equals(a, negate.a);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return "-" + a.toString();
    }
}
