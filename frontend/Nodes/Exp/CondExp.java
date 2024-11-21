package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import llvm_IR.BasicBlock;

import java.util.ArrayList;

public class CondExp extends Node {
    //  Cond → LOrExp
    public CondExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void genConditionIR(BasicBlock trueBlock, BasicBlock falseBlock) {
        ((LOrExp) children.get(0)).genOrIR(trueBlock, falseBlock);
    }
}
