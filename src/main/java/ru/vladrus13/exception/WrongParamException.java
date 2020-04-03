package ru.vladrus13.exception;

public class WrongParamException extends ParsingException {
    public WrongParamException(String s) {
        super(s);
    }

    public WrongParamException(String s, Exception e) {
        super(s, e);
    }
}