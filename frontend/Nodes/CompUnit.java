package frontend.Nodes;

import Enums.SyntaxVarType;
import frontend.Symbol.SymbolManager;

import java.util.ArrayList;

public class CompUnit extends Node {
    public CompUnit(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().pushBlock();
        super.checkError();
        SymbolManager.getInstance().popBlock();
    }
}
