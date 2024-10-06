package frontend.Nodes;

import Enums.SyntaxVarType;

import java.util.ArrayList;

public class Node {
    private SyntaxVarType type;
    private ArrayList<Node> children;

    public Node(SyntaxVarType type, ArrayList<Node> children) {
        this.type = type;
        this.children = children;
    }

    public void checkError() {

    }
}
