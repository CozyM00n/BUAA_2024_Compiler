package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;

import java.util.ArrayList;

public class Number extends Node {

    public Number(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(false, "Int");
    }
}
