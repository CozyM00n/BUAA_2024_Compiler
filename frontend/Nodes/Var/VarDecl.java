package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class VarDecl extends Node {
    public VarDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
