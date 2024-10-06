package frontend.Lexer;

import java.util.ArrayList;

public class TokenStream {
    private ArrayList<Token> tokenList;
    private int pos;
    private int posLogger;

    public TokenStream(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        this.pos = -1;
    }

    public Token read() {
        pos++; // todo
        if(pos >= tokenList.size()) {
            return null;
        }
        return tokenList.get(pos);
    }

    public void unread() {
        if(pos - 1 >= 0) {
            pos--;
        } else {
            System.out.println("Token Stream: pos is below 0!");
        }
    }

    public Token look(int n) {
        if (pos + n < tokenList.size()) {
            return tokenList.get(pos + n);
        } else {
            return null;
        }
    }

    public void logPosition() {
        this.posLogger = pos;
    }

    public Token gotoLogPosition() {
        this.pos = posLogger;
        return tokenList.get(posLogger);
    }
}
