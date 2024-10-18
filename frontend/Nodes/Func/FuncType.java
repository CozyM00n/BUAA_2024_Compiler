package frontend.Nodes.Func;

import Enums.ReturnType;
import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Nodes.Var.TokenNode;

import java.util.ArrayList;

// FuncType → 'void' | 'int' | 'char'

public class FuncType extends Node {
    public FuncType(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public ReturnType getFuncRetType() {
        TokenType tokenType = ((TokenNode)children.get(0)).getTokenType();
        if (tokenType == TokenType.INTTK) return ReturnType.RETURN_INT;
        if (tokenType == TokenType.CHARTK) return ReturnType.RETURN_CHAR;
        return ReturnType.RETURN_VOID;
    }
}
