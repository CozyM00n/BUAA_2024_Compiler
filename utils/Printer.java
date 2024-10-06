package utils;

import Enums.SyntaxVarType;
import frontend.Lexer.Token;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Printer {
    private static String outputFile;
    private static HashMap<Integer, Error> errorList;
    private static FileWriter outputWriter;
    private static FileWriter errorWriter;
    public static boolean enable;

    public static void initPrinter() throws IOException {
        outputFile = "parser.txt";
        errorList = new HashMap<>();
        outputWriter = new FileWriter(outputFile);
        errorWriter = new FileWriter("error.txt");
        enable = true;
    }

    public static void closePrinter() {
        try {
            outputWriter.close();
            errorWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addError(Error error) {
        errorList.put(error.getLineno(), error);
    }

//    public static void printLA(ArrayList<Token> tks) throws IOException {
//        if (errorList.isEmpty()) {
//            for (Token tk: tks) {
//                printToken(tk);
//            }
//            outputWriter.close();
//        }
//        else {
//            printErrors();
//            errorWriter.close();
//        }
//    }

    public static void printToken(Token tk) {
        if (enable && tk != null) {
            try {
                outputWriter.write(tk + "\n");
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public static void printErrors(){
        if (enable) {
            try {
                List<Map.Entry<Integer, Error>> errorEntries = new ArrayList<>(errorList.entrySet());
                errorEntries.sort(Comparator.comparing(entry -> entry.getValue().getLineno()));
                for (Map.Entry<Integer, Error> entry: errorEntries) {
                    Error e = entry.getValue();
                    errorWriter.write(e.getLineno() + " " + e.getErrType() + "\n");
                }
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public static void printSA() throws IOException {
        printErrors();
    }

    public static void printSynVarType(SyntaxVarType type) {
        if (enable) {
            try{
                outputWriter.write(type.toString());
            } catch (IOException e) {throw new RuntimeException(e);}
        }
    }
}
