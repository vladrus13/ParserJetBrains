package ru.vladrus13.operation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class for variable
 */
public class Variable extends Expression {
    protected final String variable;

    /**
     * Constructor for Variable
     *
     * @param variable name of variable
     */
    public Variable(String variable) {
        type = TYPE.VARIABLE_INTEGER;
        this.variable = variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable1 = (Variable) o;
        return Objects.equals(variable, variable1.variable);
    }

    @Override
    public List<Expression> getExpressions() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return variable;
    }
}
