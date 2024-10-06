package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class RelExp extends Node {

    public RelExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
