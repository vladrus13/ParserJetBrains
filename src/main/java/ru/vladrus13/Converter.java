package ru.vladrus13;

import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.operation.CallOperation;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main class. Parse, simplify expression here.
 */
public class Converter {
    /**
     * Debug (or check) class
     *
     * @param args lines of tests. If args.length = 0, it will read form System.in. Else it parse args
     */
    public static void main(String[] args) {
        Parser parser = new Parser();
        Simplifier simplifier = new Simplifier();
        if (args != null) {
            if (args.length == 0) {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    try {
                        System.out.println(simplify(s, parser, simplifier).stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
                    } catch (ParsingException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } else {
                for (String s : args) {
                    try {
                        System.out.println(simplify(s, parser, simplifier).stream().map(CallOperation::toString).collect(Collectors.joining("%>%")));
                    } catch (ParsingException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Run parse and simplify for {@link String}
     *
     * @param s          string for parser
     * @param parser     {@link Parser} class for parsing to {@link ru.vladrus13.operation.Expression}
     * @param simplifier {@link Simplifier} class for simplify {@link ru.vladrus13.operation.Expression}
     * @return ready list of filter and map
     * @throws ParsingException if we get some troubles
     */
    public static List<CallOperation> simplify(String s, Parser parser, Simplifier simplifier) throws ParsingException {
        return simplifier.simplify(parser.parse(s));
    }
}
