package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class UnaryExp extends Node {

    public UnaryExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
