package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class GetCharStmt extends Stmt{

    public GetCharStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
