package ru.vladrus13;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.operation.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConverterTest {

    @BeforeClass
    public static void before() {
        System.out.println("===============================");
        System.out.println("Start converter tests");
        System.out.println("===============================");
    }

    public void writeStatus(String nameTest, String test, List<CallOperation> rightAnswer, List<CallOperation> answer) {
        System.out.println("Test " + nameTest + ": " + test);
        System.out.println("\tAnswer: " + answer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
        System.out.println("\tRight answer: " + rightAnswer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
        System.out.println();
    }

    public void writeStatus(String nameTest, String test, String rightAnswer, List<CallOperation> answer) {
        System.out.println("Test " + nameTest + ": " + test);
        System.out.println("\tAnswer: " + answer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
        System.out.println("\tRight answer: " + rightAnswer);
        System.out.println();
    }

    @Test
    public void samples() {
        String test = "";
        List<CallOperation> rightAnswer;
        List<CallOperation> answer;
        Parser parser = new Parser();
        Simplifier simplifier = new Simplifier();
        try {
            test = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}";
            answer = simplifier.simplify(parser.parse(test));
            rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.ADD), new Const(new BigInteger("10")), Operation.MORE), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.ADD),
                            new BinaryOperation(new Variable("element"), new Const(new BigInteger("10")), Operation.ADD), Operation.MULTIPLY), Operation.MAP));
            writeStatus("sampleTest02", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);

            test = "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)";
            answer = simplifier.simplify(parser.parse(test));
            rightAnswer = Arrays.asList(new CallOperation(new BinaryOperation(new BinaryOperation(new Variable("element"), new Const(BigInteger.ZERO), Operation.MORE), new BinaryOperation(new Variable("element"), new Const(BigInteger.ZERO), Operation.LESS), Operation.AND), Operation.FILTER),
                    new CallOperation(new BinaryOperation(new Variable("element"), new Variable("element"), Operation.MULTIPLY), Operation.MAP));
            writeStatus("sampleTest03", test, rightAnswer, answer);
            assertEquals(rightAnswer, answer);
        } catch (ParsingException e) {
            fail("Throw exception at test " + test + "\n" + e.getMessage());
        }
    }

    @Test
    public void shortTests() {
        Parser parser = new Parser();
        Simplifier simplifier = new Simplifier();
        System.out.println("=== Short tests ===");
        List<String> tests = Arrays.asList(
                "",
                "filter{(1=0)}",
                "map{(element)}",
                "filter{(0=0)}>%>map{(element * element)}>%>map{(element * element)}>%>map{(element * element)}",
                "filter{((2+2)=(2-2))}%>%map{(2+2+  2+2+2+2+        2 +2+element)}",
                "map{(element*element)}>%>map{(element * element)}>%>map{(element * element)}>%>filter{(0=0)}",
                "map{(element*element)}>%>map{(element * element)}>%>map{(element * element)}>%>filter{(element>0)}"
        );
        List<String> answers = Arrays.asList(
                "filter{(0=0)}%>%map{element}",
                "filter{(1=0)}%>%map{element}",
                "filter{(0=0)}%>%map{element}",
                "filter{(0=0)}%>%map{(((element*element)*(element*element))*((element*element)*(element*element)))}",
                "filter{(4=0)}%>%map{(16+element)}",
                "filter{(0=0)}%>%map{(((element*element)*(element*element))*((element*element)*(element*element)))}",
                "filter{((((element*element)*(element*element))*((element*element)*(element*element)))>0)}%>%map{(((element*element)*(element*element))*((element*element)*(element*element)))}"
        );
        for (int i = 0; i < tests.size(); i++) {
            try {
                List<CallOperation> answer = simplifier.simplify(parser.parse(tests.get(i)));
                writeStatus(String.format("Test shortTest%03d", i + 1), tests.get(i), answers.get(i), answer);
                String answerString = answer.stream().map(CallOperation::toString).collect(Collectors.joining("%>%"));
                assertEquals(answerString, answers.get(i));
            } catch (ParsingException e) {
                fail("Throw exception at test " + i + "\n" + e.getMessage());
            }
        }
    }
}