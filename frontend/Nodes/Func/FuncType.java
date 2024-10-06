package frontend.Nodes.Func;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;

import java.util.ArrayList;

// FuncType → 'void' | 'int' | 'char'

public class FuncType extends Node {
    public FuncType(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }
}
