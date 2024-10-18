package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;

import java.util.ArrayList;

public class PrimExp extends Node {

    public PrimExp(SyntaxVarType type, ArrayList<Node> children) {
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
