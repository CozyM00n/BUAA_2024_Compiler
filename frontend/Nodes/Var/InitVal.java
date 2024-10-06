package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class InitVal extends Node {
    public InitVal(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
