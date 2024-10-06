package frontend.Nodes.Stmt;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

public class Stmt extends Node {
    public Stmt(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
