package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;

import java.util.ArrayList;
// BlockStmt ->  Block
public class BlockStmt extends Stmt{

    public BlockStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().pushBlock();
        super.checkError();
        SymbolManager.getInstance().popBlock();
    }
}
