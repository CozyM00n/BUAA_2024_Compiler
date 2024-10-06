package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class IfStmt extends Stmt{

    public IfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
