package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class ConstExp extends Node {

    public ConstExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
