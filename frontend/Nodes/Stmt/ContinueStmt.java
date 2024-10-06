package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class ContinueStmt extends Stmt{

    public ContinueStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
