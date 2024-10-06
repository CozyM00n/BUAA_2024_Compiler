package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

// ForStmt =>
// 'for' '(' [ForStmt] ';' [Cond] ';' [ForStmt] ')' Stmt

public class ForBodyStmt extends Stmt{

    public ForBodyStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
