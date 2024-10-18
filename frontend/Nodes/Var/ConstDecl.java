package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import Enums.TokenType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;

import java.util.ArrayList;

// ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
public class ConstDecl extends Node {
    public ConstDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        boolean isInt = ((TokenNode)(children.get(1))).getTokenType() == TokenType.INTTK;
        SymbolManager.getInstance().setIsInt(isInt);
        super.checkError();
    }
}
