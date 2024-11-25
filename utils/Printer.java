package utils;

import BackEnd.Mips.MipsManager;
import frontend.Lexer.Token;
import frontend.Symbol.SymbolManager;
import frontend.Symbol.SymbolTable;
import llvm_IR.Module;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Printer {
    private static String outputFile = "";
    private static HashMap<Integer, Error> errorList;
    private static ArrayList<String> outFileList; // for parser
    private static FileWriter outputWriter;
    private static FileWriter errorWriter;
    public static String hwTask;
    public static boolean enable;
    public static boolean genMips;

    public static void initPrinter(String task) throws IOException {
        switch (task) {
            case "LA": outputFile = "lexer.txt"; break;
            case "SA": outputFile = "parser.txt"; break;
            case "ST": outputFile = "symbol.txt"; break;
            case "CG": outputFile = "llvm_ir.txt"; break;
        }
        errorList = new HashMap<>();
        outFileList = new ArrayList<>();
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

    public static boolean hasError() {
        return !errorList.isEmpty();
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

    public static void printSymbol() throws IOException {
        if (enable && hwTask.equals("ST")) {
            if (errorList.isEmpty()) {
                HashMap<Integer, SymbolTable> symbolMap = SymbolManager.getInstance().getSymbolMap();
                List<Integer> sortedKeys = new ArrayList<>(symbolMap.keySet());
                Collections.sort(sortedKeys);
                for (Integer key : sortedKeys) {
                    outputWriter.write(symbolMap.get(key).toString());
                }
            } else {
                printErrors();
            }
        }
    }

    public static void printLLVM(Module module) throws IOException {
        if (enable && hwTask.equals("CG")) {
            outputWriter.write(module.toString());
        }
    }

    public static void printMips() throws IOException {
        if (enable && hwTask.equals("CG") && genMips) {
            try (FileWriter mipsWriter = new FileWriter("mips.txt")) {
                // 块结束时会自动关闭 FileWriter
                mipsWriter.write(MipsManager.getInstance().genMipsCode());
            }
        }
    }
}
