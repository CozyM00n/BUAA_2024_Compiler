package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class ConstDecl extends Node {

    public ConstDecl(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
