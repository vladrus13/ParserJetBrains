package ru.vladrus13.exception;

/**
 * Exception for problems with unknown operators ("log2(4)")
 */
public class UnknownOperationException extends ParsingException {
    /**
     * Constructor with string
     *
     * @param s text with problem to help
     */
    public UnknownOperationException(String s) {
        super(s);
    }

}
