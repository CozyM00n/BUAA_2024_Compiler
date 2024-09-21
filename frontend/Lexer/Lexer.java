package frontend.Lexer;

import Enums.tokenType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;

public class Lexer {
    private PushbackInputStream inputStream;
    private char curChar;
    private int lineno;
    private ArrayList<Token> tokenList;
    private TokenStream tokenStream;
    private ArrayList<Integer> errorList;


    public Lexer(PushbackInputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.curChar = (char) inputStream.read();
        this.lineno = 1;
        this.tokenList = new ArrayList<>();
        // this.tokenStream = new TokenStream(new ArrayList<>());
        this.errorList = new ArrayList<>();
    }

    public char getChar() throws IOException {
        return (char) inputStream.read();
    }

    public void unGetChar() throws IOException {
        inputStream.unread(curChar);
    }

    public boolean isEnglishLetter(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    public boolean isalnum(char ch) {
        return isEnglishLetter(ch) || Character.isDigit(ch);
    }

    private boolean isEnd() {
        return curChar == '\uFFFF';
    }

    private boolean isSpace() throws IOException {
        return curChar == '\t' || curChar == ' ' || curChar == '\0'
                || isNewLine();
    }

    private boolean isNewLine() throws IOException {
        if (curChar == '\r') { curChar = getChar(); }
        return curChar == '\n';
    }

    private void skipSpace() throws IOException {
        while (isSpace()) {
            if (curChar == '\n') lineno++;
            curChar = getChar();
        }
    }

    private tokenType getIdentType(String ident) {
        switch (ident) {
            case "main": return tokenType.MAINTK;
            case "const": return tokenType.CONSTTK;
            case "int": return tokenType.INTTK;
            case "char": return tokenType.CHARTK;
            case "break": return tokenType.BREAKTK;
            case "continue": return tokenType.CONTINUETK;
            case "if": return tokenType.IFTK;
            case "else": return tokenType.ELSETK;
            case "for": return tokenType.FORTK;
            case "getint": return tokenType.GETINTTK;
            case "getchar": return tokenType.GETCHARTK;
            case "printf": return tokenType.PRINTFTK;
            case "return": return tokenType.RETURNTK;
            case "void": return tokenType.VOIDTK;
            default:return tokenType.IDENFR;
        }
    }

    private Token recgonizeSign(StringBuilder content) throws IOException {
        if (curChar == '!') {
            curChar = getChar();
            if (curChar == '=') { // !=
                content.append(curChar);
                curChar = getChar(); // 预先读一个字符？？
                return new Token(tokenType.NEQ, content.toString(), lineno);
            }
            else {
                return new Token(tokenType.NOT, content.toString(), lineno);
            }
        }
        else if (curChar == '<') {
            curChar = getChar();
            if (curChar == '=') {
                content.append(curChar);
                curChar = getChar();
                return new Token(tokenType.LEQ, content.toString(), lineno);
            } else {
                return new Token(tokenType.LSS, content.toString(), lineno);
            }
        }
        else if (curChar == '>') {
            curChar = getChar();
            if (curChar == '=') {
                content.append(curChar);
                curChar = getChar();
                return new Token(tokenType.GEQ, content.toString(), lineno);
            } else { return new Token(tokenType.GRE, content.toString(), lineno); }
        }
        else if (curChar == '=') {
            curChar = getChar();
            if (curChar == '=') { // ==
                content.append(curChar);
                curChar = getChar();
                return new Token(tokenType.EQL, content.toString(), lineno);
            } else { return new Token(tokenType.ASSIGN, content.toString(), lineno); }
        }
        else if (curChar == '/') {
            curChar = getChar();
            if (curChar == '/') { //
                curChar = getChar(); /* // 的下一个字符*/
                while (true) {
                    if (isNewLine()) {
                        lineno++;
                        break;
                    } else if (isEnd()){
                        return new Token(tokenType.EOFTK, content.toString(), lineno); // content 内容？
                    }
                    curChar = getChar();
                }
                curChar = getChar(); // \n的下一个字符
                return next();
            } else if (curChar == '*') {
                curChar = getChar();
                while (true) {
                    while (curChar != '*') {
                        if (isNewLine()) lineno++;
                        if (isEnd()) return new Token(tokenType.EOFTK, content.toString(), lineno);// content 内容？
                        curChar = getChar();
                    }
                    while (curChar == '*') curChar = getChar();
                    if (curChar == '/') break;
                }
                curChar = getChar(); // */ 的下一个字符
                return next();
            } else {
                return new Token(tokenType.DIV, content.toString(), lineno);
            }
        }
        else if (curChar == '&') {
            curChar = getChar();
            if (curChar == '&') { // &&
                content.append(curChar);
                curChar = getChar();
            } else {
                addError(); // 单词名称(Value) = "&"
            }
            return new Token(tokenType.AND, content.toString(), lineno);
        } else if (curChar == '|') {
            curChar = getChar();
            if (curChar == '|') { // ||
                content.append(curChar);
                curChar = getChar();
            } else {
                addError(); // 单词名称(Value) = "|"
            }
            return new Token(tokenType.OR, content.toString(), lineno);
        } else {
            char ch = curChar;
            curChar = getChar();
            switch (ch) {
                case '+': return new Token(tokenType.PLUS, content.toString(), lineno);
                case '-': return new Token(tokenType.MINU, content.toString(), lineno);
                case '*': return new Token(tokenType.MULT, content.toString(), lineno);
                case '%': return new Token(tokenType.MOD, content.toString(), lineno);
                case ';': return new Token(tokenType.SEMICN, content.toString(), lineno);
                case ',': return new Token(tokenType.COMMA, content.toString(), lineno);
                case '(': return new Token(tokenType.LPARENT, content.toString(), lineno);
                case ')': return new Token(tokenType.RPARENT, content.toString(), lineno);
                case '[': return new Token(tokenType.LBRACK, content.toString(), lineno);
                case ']': return new Token(tokenType.RBRACK, content.toString(), lineno);
                case '{': return new Token(tokenType.LBRACE, content.toString(), lineno);
                case '}': return new Token(tokenType.RBRACE, content.toString(), lineno);
            }
        }
        System.out.println("error: undifined ch: " + curChar);
        return null;
    }

    public Token next() throws IOException {
        skipSpace();
        StringBuilder content = new StringBuilder();
        content.append(curChar);
        if (isEnglishLetter(curChar) || curChar == '_') {
            curChar = getChar();
            while (isalnum(curChar) || curChar == '_') {
                content.append(curChar);
                curChar = getChar();
            }
            // unGetChar();
            String ident = content.toString();
            tokenType type = getIdentType(ident);
            return new Token(type, ident, lineno);
        }
        else if (Character.isDigit(curChar)) {
            curChar = getChar();
            while (Character.isDigit(curChar)) {
                content.append(curChar);
                curChar = getChar();
            }
            // unGetChar();
            return new Token(tokenType.INTCON, content.toString(), lineno);
        }
        else if (curChar == '\'') { // CharConst
            curChar = getChar(); // ASCII
            if (curChar == '\\') { // 转义 再读一个
                content.append(curChar);
                curChar = getChar();
            }
            content.append(curChar);
            curChar = getChar();
            content.append(curChar);
            curChar = getChar(); // 提前读
            return new Token(tokenType.CHRCON, content.toString(), lineno);
        }
        else if (curChar == '"') { // StringConst
            curChar = getChar();
            while (curChar != '"') {
                if (curChar == '\\') {
                    content.append(curChar);
                    curChar = getChar();
                }
                content.append(curChar);
                curChar = getChar();
            }
            content.append(curChar); // "
            curChar = getChar();
            return new Token(tokenType.STRCON, content.toString(), lineno);
        }
        else if (isEnd()) {
            return new Token(tokenType.EOFTK, content.toString(), lineno);
        }
        else {
            return recgonizeSign(content);
        }
    }

    public ArrayList<Token> getTokenList() throws IOException {
        if (tokenList.isEmpty()) {
            Token tk = next();
            while (tk.getType() != tokenType.EOFTK) {
                tokenList.add(tk);
                tk = next();
            }
        }
        return tokenList;
    }

    public TokenStream getTokenStream() throws IOException {
        this.tokenStream = new TokenStream(getTokenList());
        return tokenStream;
    }

    private void printError(int lineno, char errorType) throws IOException {
        FileWriter writer = new FileWriter("error.txt");
        writer.write(lineno + " " + errorType);
        writer.close();
    }

    private void addError() {
        errorList.add(lineno);
    }

    public void printAns() throws IOException {
        ArrayList<Token> tks = getTokenList();
        if (errorList.isEmpty()) {
            FileWriter writer = new FileWriter("lexer.txt");
            for (Token tk: tks) {
                writer.write(tk.toString() + "\n");
            }
            writer.close();
        }
        else {
            for (int lineno : errorList)
                printError(lineno, 'a');
        }
    }
}
