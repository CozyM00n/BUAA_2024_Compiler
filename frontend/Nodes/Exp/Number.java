package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class Number extends Node {

    public Number(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
