package ru.vladrus13;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ParserTest {

    @BeforeClass
    public static void before() {
        System.out.println("=================================");
        System.out.println("Start testing ParserTest");
        System.out.println("=================================");
    }

    public void writeStatus(String nameTest, String test, List<CallOperation> rightAnswer, List<CallOperation> answer) {
        System.out.println("Test " + nameTest + ": " + test);
        System.out.println("\tAnswer: " + answer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
        System.out.println("\tRight answer: " + rightAnswer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
        System.out.println();
    }

    @Test
    public void oneAdd() {
        try {
            String test = "map{2+2}";
            List<CallOperation> rightAnswer = Collections.singletonList(new CallOperation(new BinaryOperation(new Const(new BigInteger("2")), new Const(new BigInteger("2")), Operation.ADD), Operation.MAP));
            List<CallOperation> answer = new Parser().parse(test);
            writeStatus("oneAdd", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);
        } catch (ParsingException e) {
            fail("Throw exception" + e.getMessage());
        }
    }

    @Test
    public void filterAndMap() {
        try {
            String test = "filter{(element>10)}%>%filter{(element<20)}";
            List<CallOperation> rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.MORE), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("20")), Operation.LESS), Operation.FILTER));
            List<CallOperation> answer = new Parser().parse(test);
            writeStatus("filterAndMap", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);
        } catch (ParsingException e) {
            fail("Throw exception" + e.getMessage());
        }
    }

    @Test
    public void exception1() {
        System.out.println("Test exceptions:");
        System.out.println("\ttest: filter{(element)}");
        assertThrows(ParsingException.class, () -> new Parser().parse("filter{(element)}"));
        System.out.println("\ttest: map{(element>0)}");
        assertThrows(ParsingException.class, () -> new Parser().parse("map{(element>0)}"));
        System.out.println("\ttest: map{(element*)}");
        assertThrows(ParsingException.class, () -> new Parser().parse("map{(element*)}"));
        System.out.println("\ttest: map{(element*(element>10))}");
        assertThrows(ParsingException.class, () -> new Parser().parse("map{(element*(element>10)))}"));
        System.out.println("\ttest: element*element");
        assertThrows(ParsingException.class, () -> new Parser().parse("element*element"));
        System.out.println();
    }

    @Test
    public void sampleTest() {
        String test = "";
        List<CallOperation> rightAnswer;
        List<CallOperation> answer;
        Parser parser = new Parser();
        try {
            test = "filter{(element>10)}%>%filter{(element<20)}";
            answer = parser.parse(test);
            rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.MORE), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("20")), Operation.LESS), Operation.FILTER));
            writeStatus("sampleTest01", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);

            test = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}";
            answer = parser.parse(test);
            rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.ADD), Operation.MAP),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.MORE), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Variable("element"), Operation.MULTIPLY), Operation.MAP));
            writeStatus("sampleTest02", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);

            test = "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)";
            answer = parser.parse(test);
            rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new Variable("element"), new Const(BigInteger.ZERO), Operation.MORE), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Const(BigInteger.ZERO), Operation.LESS), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Variable("element"), Operation.MULTIPLY), Operation.MAP));
            writeStatus("sampleTest03", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);
        } catch (ParsingException e) {
            fail("Throw exception at test " + test + "\n" + e.getMessage());
        }
    }
}