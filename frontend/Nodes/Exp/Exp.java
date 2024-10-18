package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;

import java.util.ArrayList;

public class Exp extends Node {
    // 表达式 Exp → AddExp
    public Exp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public TypeInfo getTypeInfo() {
        for (Node child : children) {
            if (child.getTypeInfo() != null) {
                return child.getTypeInfo();
            }
        }
        return null;
    }
}
