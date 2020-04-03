package ru.vladrus13.exception;

public class MissingOperatorException extends ParsingException {
    public MissingOperatorException(String s) {
        super(s);
    }

}
