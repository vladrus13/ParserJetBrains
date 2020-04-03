package ru.vladrus13;

import org.junit.Test;
import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ParserTest {

    @Test
    public void oneAdd() {
        try {
            assertEquals(new CallOperation(new BinaryOperation(new Const(new BigInteger("2")), new Const(new BigInteger("2")), Operation.ADD), Operation.MAP), new Parser().parse("map{2+2}").get(0));
        } catch (ParsingException e) {
            fail("Throw exception" + e.getMessage());
        }
    }

    @Test
    public void filterAndMap() {
        try {
            assertEquals(new Parser().parse("filter{(element>10)}%>%filter{(element<20)}"),
                    Arrays.asList(new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.MORE), Operation.FILTER),
                            new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("20")), Operation.LESS), Operation.FILTER)));
        } catch (ParsingException e) {
            fail("Throw exception" + e.getMessage());
        }
    }
}