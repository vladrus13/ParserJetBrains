package ru.vladrus13.exception;

public class UnknownOperationException extends ParsingException {
    public UnknownOperationException(String s) {
        super(s);
    }

    public UnknownOperationException(String s, Exception e) {
        super(s, e);
    }
}
