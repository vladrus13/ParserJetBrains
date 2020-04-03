package ru.vladrus13.exception;

/**
 * Exception for problems with missing operators ("+ 2")
 */
public class MissingOperatorException extends ParsingException {
    /**
     * Constructor with string
     *
     * @param s text with problem to help
     */
    public MissingOperatorException(String s) {
        super(s);
    }

}
