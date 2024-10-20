package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;

import java.util.ArrayList;

public class Char extends Node {

    public Char(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public TypeInfo getTypeInfo() {
        return new TypeInfo(false, "Char");
    }
}
