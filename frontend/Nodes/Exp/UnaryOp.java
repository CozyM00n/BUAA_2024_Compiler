package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class UnaryOp extends Node {

    public UnaryOp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
