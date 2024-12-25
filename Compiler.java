import Optimizer.Optimizer;
import frontend.Lexer.Lexer;
import frontend.Nodes.Node;
import frontend.Parser.Parser;
import llvm_IR.Module;
import utils.Printer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws IOException {
        String task = "CG";
        Printer.initPrinter(task);
        Printer.genMips = true;

        if (task.equals("LA")) {
            FileInputStream fin = new FileInputStream("testfile.txt");
            PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
            Lexer lexer = new Lexer(inputStream);
            //lexer.test();
            Printer.printLA(lexer.getTokenList());
        } else if (task.equals("SA")) {
            FileInputStream fin = new FileInputStream("testfile.txt");
            PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
            Lexer lexer = new Lexer(inputStream);
            Parser parser = new Parser(lexer.getTokenStream());
            Node node = parser.parseCompUnit();
            Printer.printSA();
        } else if (task.equals("ST")) {
            FileInputStream fin = new FileInputStream("testfile.txt");
            PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
            Lexer lexer = new Lexer(inputStream);
            Parser parser = new Parser(lexer.getTokenStream());
            Node compUnit = parser.parseCompUnit();
            compUnit.checkError();
            Printer.printSymbol();
        } else if (task.equals("CG")) { // code generation
            FileInputStream fin = new FileInputStream("testfile.txt");
            PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
            Lexer lexer = new Lexer(inputStream);
            Parser parser = new Parser(lexer.getTokenStream());
            Node compUnit = parser.parseCompUnit();
            compUnit.checkError();
            //Printer.debugSymbol();
            if (Printer.hasError()) {
                Printer.printErrors();
                System.out.println("compiler.java: task = Code Generation : see error.txt");
            } else { // 只为正确的testFile生成llvm
                compUnit.generateIR();
                Module module = Module.getInstance();
                Printer.printLLVM(module);
                module.genAsm();
                Printer.printMips();
            }
        }
        Printer.closePrinter();
    }
}
