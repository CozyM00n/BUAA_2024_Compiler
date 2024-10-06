package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class BreakStmt extends Stmt{

    public BreakStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
