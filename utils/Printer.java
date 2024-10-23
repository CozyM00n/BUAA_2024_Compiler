package utils;

import frontend.Lexer.Token;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Printer {
    private static String outputFile = "";
    private static HashMap<Integer, Error> errorList;
    private static ArrayList<String> outFileList; // for parser
    private static HashMap<Integer, ArrayList<String>> symbolMaps;
    private static FileWriter outputWriter;
    private static FileWriter errorWriter;
    public static String hwTask;
    public static boolean enable;

    public static void initPrinter(String task) throws IOException {
        switch (task) {
            case "LA": outputFile = "lexer.txt"; break;
            case "SA": outputFile = "parser.txt"; break;
            case "ST": outputFile = "symbol.txt"; break;
        }
        errorList = new HashMap<>();
        outFileList = new ArrayList<>();
        symbolMaps = new HashMap<>();
        outputWriter = new FileWriter(outputFile);
        errorWriter = new FileWriter("error.txt");
        enable = true;
        hwTask = task;
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

    public static void printToken(Token tk) {
        try {
            outputWriter.write(tk.toString());
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void printLA(ArrayList<Token> tks) {
        if (enable && hwTask.equals("LA")) {
            if (errorList.isEmpty()) {
                for (Token tk: tks) {
                    printToken(tk);
                }
            } else {
                printErrors();
            }
        }
    }

    public static void printErrors() {
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

    public static void addString(String s) {
        // for parser
        if (enable && hwTask.equals("SA")) {
            outFileList.add(s);
        }
    }

    public static void printSA() throws IOException {
        if (enable && hwTask.equals("SA")) {
            if (errorList.isEmpty()) {
                for (String s : outFileList) {
                    outputWriter.write(s);
                }
            }
            else {
                printErrors();
            }
        }
    }

    public static void addString(int symbolMapNo, ArrayList<String> strings) {
        symbolMaps.put(symbolMapNo, strings);
    }

    public static void printSymbol() throws IOException {
        if (enable && hwTask.equals("ST")) {
            if (errorList.isEmpty()) {
                List<Integer> sortedKeys = new ArrayList<>(symbolMaps.keySet());
                Collections.sort(sortedKeys);
                for (Integer key : sortedKeys) {
                    ArrayList<String> strings = symbolMaps.get(key);
                    for (String s : strings) {
                        outputWriter.write(s + "\n");
                    }
                }
            } else {
                printErrors();
            }
        }
    }
}
