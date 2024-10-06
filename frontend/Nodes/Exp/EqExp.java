package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class EqExp extends Node {

    public EqExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
