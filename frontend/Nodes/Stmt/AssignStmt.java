package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class AssignStmt extends Stmt{

    public AssignStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
