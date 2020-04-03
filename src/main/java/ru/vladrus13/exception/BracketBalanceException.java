package ru.vladrus13.exception;

public class BracketBalanceException extends ParsingException {
    public BracketBalanceException(String s) {
        super(s);
    }

    public BracketBalanceException(String s, Exception e) {
        super(s, e);
    }
}
