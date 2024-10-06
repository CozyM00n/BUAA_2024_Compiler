package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class ReturnStmt extends Stmt{

    public ReturnStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
