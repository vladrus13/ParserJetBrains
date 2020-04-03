package ru.vladrus13;

import ru.vladrus13.exception.ParsingException;
import ru.vladrus13.operation.CallOperation;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Converter {
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

    public static List<CallOperation> simplify(String s, Parser parser, Simplifier simplifier) throws ParsingException {
        return simplifier.simplify(parser.parse(s));
    }
}
