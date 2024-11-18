package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

// ConstExp → AddExp 注：使用的 Ident 必须是常量
public class ConstExp extends Node {
    public ConstExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    @Override
    public int calculate() {
        return children.get(0).calculate();
    }
}
