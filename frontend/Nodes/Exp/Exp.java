package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class Exp extends Node {

    public Exp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
