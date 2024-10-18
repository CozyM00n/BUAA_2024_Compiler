package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.SymbolManager;

import java.util.ArrayList;

// ForStmt =>
// 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt // h

public class ForBodyStmt extends Stmt{

    public ForBodyStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public void checkError() {
        SymbolManager.getInstance().enterLoop();
        super.checkError(); // todo
        SymbolManager.getInstance().leaveLoop();
    }
}
