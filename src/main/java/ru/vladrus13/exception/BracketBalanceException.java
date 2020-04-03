package ru.vladrus13.exception;

/**
 * Class for clause problem with bracket balance ("())")
 */
public class BracketBalanceException extends ParsingException {
    /**
     * Constructor with string
     *
     * @param s text with problem to help
     */
    public BracketBalanceException(String s) {
        super(s);
    }

}
