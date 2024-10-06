package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

// ForStmt => LVal '=' Exp
public class ForStmt extends Node {
    public ForStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
