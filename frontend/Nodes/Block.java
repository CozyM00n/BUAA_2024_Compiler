package frontend.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;
// Block → '{' { BlockItem } '}'
public class Block extends Node {
    public Block(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
