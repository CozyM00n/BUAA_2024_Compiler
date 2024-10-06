package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class CondExp extends Node {

    public CondExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
