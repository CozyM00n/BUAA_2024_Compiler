package frontend.Lexer;

import Enums.TokenType;

public class Token {
    private TokenType type; // 单词类别
    private String value; // 单词值
    private int lineno;

    public Token(TokenType type, String value, int lineno) {
        this.type = type;
        this.value = value;
        this.lineno = lineno;
    }

    public TokenType getTokenType() {
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
        return type + " " + value + "\n";
    }
}
