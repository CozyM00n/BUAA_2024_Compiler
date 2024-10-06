package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class BlockStmt extends Stmt{

    public BlockStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
