package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class PrintfStmt extends Stmt{

    public PrintfStmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
