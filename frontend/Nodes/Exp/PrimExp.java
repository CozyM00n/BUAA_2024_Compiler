package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class PrimExp extends Node {

    public PrimExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
