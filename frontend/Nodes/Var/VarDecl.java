package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;

import java.util.ArrayList;

// VarDecl → BType VarDef { ',' VarDef } ';'
public class VarDecl extends Node {
    public VarDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public String judgeType() {
        if (((TokenNode)children.get(0)).getTokenType() == TokenType.INTTK) {
            return "Int";
        } else {
            return "Char";
        }
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().setDeclaredType(judgeType());
        super.checkError();
    }
}
