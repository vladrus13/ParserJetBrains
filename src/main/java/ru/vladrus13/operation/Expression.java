package ru.vladrus13.operation;

import java.util.List;

/**
 * Abstract class for mono of expression
 */
public abstract class Expression {

    /**
     * Type of operation
     */
    protected TYPE type;

    /**
     * Return string view of expression
     *
     * @return {@link String} view
     */
    public abstract String toString();

    public abstract boolean equals(Object o);

    /**
     * Get expressions
     *
     * @return list of expressions
     */
    public abstract List<Expression> getExpressions();
}
