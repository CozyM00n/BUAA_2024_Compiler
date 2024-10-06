package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class MulExp extends Node {

    public MulExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
