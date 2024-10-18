package frontend.Lexer;

import Enums.TokenType;
import utils.Error;
import utils.Printer;

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

    public boolean isEnd() {
        return curChar == '\uFFFF';
    }

    public boolean isSpace() throws IOException {
        return curChar == '\t' || curChar == ' ' || curChar == '\0'
                || isNewLine();
    }

    public boolean isNewLine() throws IOException {
        if (curChar == '\r') { curChar = getChar(); }
        return curChar == '\n';
    }

    public void skipSpace() throws IOException {
        while (isSpace()) {
            if (curChar == '\n') lineno++;
            curChar = getChar();
        }
    }

    public TokenType getIdentType(String ident) {
        switch (ident) {
            case "main": return TokenType.MAINTK;
            case "const": return TokenType.CONSTTK;
            case "int": return TokenType.INTTK;
            case "char": return TokenType.CHARTK;
            case "break": return TokenType.BREAKTK;
            case "continue": return TokenType.CONTINUETK;
            case "if": return TokenType.IFTK;
            case "else": return TokenType.ELSETK;
            case "for": return TokenType.FORTK;
            case "getint": return TokenType.GETINTTK;
            case "getchar": return TokenType.GETCHARTK;
            case "printf": return TokenType.PRINTFTK;
            case "return": return TokenType.RETURNTK;
            case "void": return TokenType.VOIDTK;
            default:return TokenType.IDENFR;
        }
    }

    public Token recgonizeSign(StringBuilder content) throws IOException {
        if (curChar == '!') {
            curChar = getChar();
            if (curChar == '=') { // !=
                content.append(curChar);
                curChar = getChar(); // 预先读一个字符？？
                return new Token(TokenType.NEQ, content.toString(), lineno);
            }
            else {
                return new Token(TokenType.NOT, content.toString(), lineno);
            }
        }
        else if (curChar == '<') {
            curChar = getChar();
            if (curChar == '=') {
                content.append(curChar);
                curChar = getChar();
                return new Token(TokenType.LEQ, content.toString(), lineno);
            } else {
                return new Token(TokenType.LSS, content.toString(), lineno);
            }
        }
        else if (curChar == '>') {
            curChar = getChar();
            if (curChar == '=') {
                content.append(curChar);
                curChar = getChar();
                return new Token(TokenType.GEQ, content.toString(), lineno);
            } else { return new Token(TokenType.GRE, content.toString(), lineno); }
        }
        else if (curChar == '=') {
            curChar = getChar();
            if (curChar == '=') { // ==
                content.append(curChar);
                curChar = getChar();
                return new Token(TokenType.EQL, content.toString(), lineno);
            } else { return new Token(TokenType.ASSIGN, content.toString(), lineno); }
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
                        return new Token(TokenType.EOFTK, content.toString(), lineno); // content 内容？
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
                        if (isEnd()) return new Token(TokenType.EOFTK, content.toString(), lineno);// content 内容？
                        curChar = getChar();
                    }
                    while (curChar == '*') curChar = getChar();
                    if (curChar == '/') break;
                }
                curChar = getChar(); // */ 的下一个字符
                return next();
            } else {
                return new Token(TokenType.DIV, content.toString(), lineno);
            }
        }
        else if (curChar == '&') {
            curChar = getChar();
            if (curChar == '&') { // &&
                content.append(curChar);
                curChar = getChar();
            } else {
                Printer.addError(new Error(lineno, 'a'));// 单词名称(Value) = "&"
            }
            return new Token(TokenType.AND, content.toString(), lineno);
        } else if (curChar == '|') {
            curChar = getChar();
            if (curChar == '|') { // ||
                content.append(curChar);
                curChar = getChar();
            } else {
                Printer.addError(new Error(lineno, 'a')); // 单词名称(Value) = "|"
            }
            return new Token(TokenType.OR, content.toString(), lineno);
        } else {
            char ch = curChar;
            curChar = getChar();
            switch (ch) {
                case '+': return new Token(TokenType.PLUS, content.toString(), lineno);
                case '-': return new Token(TokenType.MINU, content.toString(), lineno);
                case '*': return new Token(TokenType.MULT, content.toString(), lineno);
                case '%': return new Token(TokenType.MOD, content.toString(), lineno);
                case ';': return new Token(TokenType.SEMICN, content.toString(), lineno);
                case ',': return new Token(TokenType.COMMA, content.toString(), lineno);
                case '(': return new Token(TokenType.LPARENT, content.toString(), lineno);
                case ')': return new Token(TokenType.RPARENT, content.toString(), lineno);
                case '[': return new Token(TokenType.LBRACK, content.toString(), lineno);
                case ']': return new Token(TokenType.RBRACK, content.toString(), lineno);
                case '{': return new Token(TokenType.LBRACE, content.toString(), lineno);
                case '}': return new Token(TokenType.RBRACE, content.toString(), lineno);
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
            TokenType type = getIdentType(ident);
            return new Token(type, ident, lineno);
        }
        else if (Character.isDigit(curChar)) {
            curChar = getChar();
            while (Character.isDigit(curChar)) {
                content.append(curChar);
                curChar = getChar();
            }
            // unGetChar();
            return new Token(TokenType.INTCON, content.toString(), lineno);
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
            return new Token(TokenType.CHRCON, content.toString(), lineno);
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
            return new Token(TokenType.STRCON, content.toString(), lineno);
        }
        else if (isEnd()) {
            return new Token(TokenType.EOFTK, content.toString(), lineno);
        }
        else {
            return recgonizeSign(content);
        }
    }

    public ArrayList<Token> getTokenList() throws IOException {
        if (tokenList.isEmpty()) {
            Token tk = next();
            while (tk.getTokenType() != TokenType.EOFTK) {
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

    public void printError(int lineno, char errorType) throws IOException {
        FileWriter writer = new FileWriter("error.txt");
        writer.write(lineno + " " + errorType);
        writer.close();
    }

    public void addError() {
        errorList.add(lineno);
    }
}
