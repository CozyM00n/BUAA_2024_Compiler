package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class ExpStmt extends Stmt{

    public ExpStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
