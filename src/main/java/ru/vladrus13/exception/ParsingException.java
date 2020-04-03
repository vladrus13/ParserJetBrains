package ru.vladrus13.exception;

/**
 * General exception for parsing and simplifying
 */
public class ParsingException extends Exception {
    /**
     * Constructor with string
     *
     * @param s text with problem to help
     */
    public ParsingException(String s) {
        super("Parsing exception: \n" + s);
    }

}
