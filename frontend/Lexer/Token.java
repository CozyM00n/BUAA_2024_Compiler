package frontend.Lexer;

import Enums.tokenType;

public class Token {
    private tokenType type; // 单词类别
    private String value; // 单词值
    private int lineno;

    public Token(tokenType type, String value, int lineno) {
        this.type = type;
        this.value = value;
        this.lineno = lineno;
    }

    public tokenType getType() {
        return type;
    }

    public int getLineno() {
        return lineno;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " " + value;
    }
}
