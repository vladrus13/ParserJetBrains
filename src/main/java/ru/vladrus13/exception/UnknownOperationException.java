package ru.vladrus13.exception;

public class UnknownOperationException extends ParsingException {
    public UnknownOperationException(String s) {
        super(s);
    }

}
