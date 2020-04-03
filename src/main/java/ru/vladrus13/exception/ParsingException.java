package ru.vladrus13.exception;

public class ParsingException extends Exception {
    public ParsingException(String s) {
        super("Parsing exception: \n" + s);
    }

}
