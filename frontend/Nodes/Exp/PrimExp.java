package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import frontend.Symbol.TypeInfo;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

//  '(' Exp ')' | LVal | Number | Character
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

    @Override
    public int calculate() {
        if (children.size() > 1) return children.get(1).calculate();
        else return children.get(0).calculate();
    }

    @Override
    public Value generateIR() {
        if (children.size() > 1) return children.get(1).generateIR();
        else if (children.get(0) instanceof LValExp) {
            return ((LValExp) children.get(0)).generateIR();
        }
        return children.get(0).generateIR();
    }
}
