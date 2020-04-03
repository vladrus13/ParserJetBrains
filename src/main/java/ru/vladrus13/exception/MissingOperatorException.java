package ru.vladrus13.exception;

public class MissingOperatorException extends ParsingException {
    public MissingOperatorException(String s) {
        super(s);
    }

    public MissingOperatorException(String s, Exception e) {
        super(s, e);
    }
}
