import frontend.Lexer.Lexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class Compiler {
    public static void main(String[] args) throws IOException {
        FileInputStream fin = new FileInputStream("testfile.txt");
        PushbackInputStream inputStream = new PushbackInputStream(fin, 16);
        Lexer lexer = new Lexer(inputStream);
        lexer.printAns();
    }
}
