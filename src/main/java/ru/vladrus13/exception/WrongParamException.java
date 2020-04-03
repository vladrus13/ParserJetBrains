package ru.vladrus13.exception;

/**
 * Exception with problems with types errors ("2 + (element = 2)")
 */
public class WrongParamException extends ParsingException {
    /**
     * Constructor with string
     *
     * @param s text with problem to help
     */
    public WrongParamException(String s) {
        super(s);
    }

}
