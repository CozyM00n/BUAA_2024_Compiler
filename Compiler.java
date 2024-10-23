import frontend.Lexer.Lexer;
import frontend.Nodes.Node;
import frontend.Parser.Parser;
import utils.Printer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws IOException {

        String task = "ST";
        Printer.initPrinter(task);

        if (task.equals("LA")) {
            FileInputStream fin = new FileInputStream("testfile.txt");
            PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
            Lexer lexer = new Lexer(inputStream);
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
        }
        Printer.closePrinter();
    }
}
