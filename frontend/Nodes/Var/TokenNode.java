package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import frontend.Lexer.Token;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class TokenNode extends Node {
    private Token token;

    public TokenNode(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public TokenNode(SyntaxVarType type, ArrayList<Node> children, Token token) {
        super(type, children);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
