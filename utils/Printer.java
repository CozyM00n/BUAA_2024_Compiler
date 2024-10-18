package utils;

import Enums.SyntaxVarType;
import frontend.Lexer.Token;
import frontend.Symbol.SymbolTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Printer {
    private static HashMap<Integer, Error> errorList;
    private static ArrayList<String> outFileList;
    private static HashMap<Integer, ArrayList<String>> symbolMaps;
    private static FileWriter outputWriter;
    private static FileWriter errorWriter;
    public static int hwNo;
    public static boolean enable;

    public static void initPrinter(String outputFile, int hwNumber) throws IOException {
        errorList = new HashMap<>();
        outFileList = new ArrayList<>();
        symbolMaps = new HashMap<>();
        outputWriter = new FileWriter(outputFile);
        errorWriter = new FileWriter("error.txt");
        enable = true;
        hwNo = hwNumber;
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

    public static void printLA(ArrayList<Token> tks) throws IOException {
        if (errorList.isEmpty()) {
            for (Token tk: tks) {
                printToken(tk);
            }
        }
        else {
            printErrors();
        }
    }

    public static void printToken(Token tk) {
        if (enable && tk != null && (hwNo == 1 || hwNo == 2)) {
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

    public static void printSynVarType(SyntaxVarType type) {
        if (enable && (hwNo == 1 || hwNo == 2)) {
            try{
                outputWriter.write(type.toString());
            } catch (IOException e) {throw new RuntimeException(e);}
        }
    }

    public static void addOutFileInfo(String s) {
        // for parser
        if (hwNo == 2) {
            outFileList.add(s + "\n");
        }
    }

    public static void addOutFileInfo(int symbolMapNo, ArrayList<String> strings) {
        symbolMaps.put(symbolMapNo, strings);
    }

    public static void printSymbol() throws IOException {
        if (enable && hwNo == 3) {
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
