package frontend.Nodes;

import Enums.SyntaxVarType;
import frontend.Symbol.TypeInfo;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class Node {
    protected SyntaxVarType type;
    protected ArrayList<Node> children;
    protected int startLine;
    protected int endLine;

    // for tokenNode
    public Node(SyntaxVarType type, ArrayList<Node> children, int startLine, int endLine) {
        this.type = type;
        this.children = children;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    // for other noneLeaf node
    public Node(SyntaxVarType type, ArrayList<Node> children) {
        this.type = type;
        this.children = children;
        if (children.isEmpty()) {
            System.out.println("Err: Node constructor for NoneLeaf Node");
        }
        this.startLine = children.get(0).startLine;
        this.endLine = children.get(children.size() - 1).endLine;
    }

    public void checkError() {
        if (children == null) {
            // System.out.println("children = null");
            return;
        }
        for (Node child: children) {
            child.checkError();
        }
    }

    public Value generateIR() {
        if (children == null) return null;
        for (Node child: children)
            child.generateIR();
        return null;
    }

    public int calculate() {
        return 0;
    }

    public TypeInfo getTypeInfo() {
        return null;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public int getStartLine() {
        return this.startLine;
    }

    public int getEndLine() {
        return this.endLine;
    }
}
