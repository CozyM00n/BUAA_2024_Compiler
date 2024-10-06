package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class FuncFParams extends Node {
    public FuncFParams(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
