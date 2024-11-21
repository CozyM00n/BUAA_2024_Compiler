package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import llvm_IR.IRManager;
import llvm_IR.BasicBlock;

import java.util.ArrayList;

public class LOrExp extends Node {
    // LOrExp → LAndExp { '||' LAndExp }

    public LOrExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void genOrIR(BasicBlock trueBlock, BasicBlock falseBlock) {
        for (int i = 0; i < children.size(); i += 2) {
            if (i == children.size() - 1) {
                ((LAndExp) children.get(i)).genAndIR(trueBlock, falseBlock);
            }
            else {
                BasicBlock nextBlock = new BasicBlock(IRManager.getInstance().genBlockName());
                ((LAndExp) children.get(i)).genAndIR(trueBlock, nextBlock);
                IRManager.getInstance().addAndSetCurBlock(nextBlock);
                //IRManager.getInstance().resetBlockName(nextBlock);
            }
        }
    }
}
