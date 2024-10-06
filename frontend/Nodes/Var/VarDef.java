package frontend.Nodes.Var;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class VarDef extends Node {
    public VarDef(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
