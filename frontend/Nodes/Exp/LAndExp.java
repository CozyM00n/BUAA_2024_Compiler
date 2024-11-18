package frontend.Nodes.Exp;

import Enums.SyntaxVarType;
import frontend.Nodes.Node;
import llvm_IR.IRManager;
import llvm_IR.Instr.BranchInstr;
import llvm_IR.Instr.IcmpInstr;
import llvm_IR.Instr.JumpInstr;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Values.BasicBlock;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

import java.util.ArrayList;

public class LAndExp extends Node {
    // LAndExp → EqExp { '&&' EqExp }

    public LAndExp(SyntaxVarType type, ArrayList<Node> children) {
        super(type, children);
    }

    public void genBranchInstr(Value eqExp, BasicBlock trueBlock, BasicBlock falseBlock) {
        if (eqExp instanceof Constant) { // 真假可判断，可以直接生成jump指令
            if (((Constant) eqExp).getValue() == 0) {
                new JumpInstr(falseBlock);
            } else {
                new JumpInstr(trueBlock);
            }
        }
        else {
            if (eqExp.getLlvmType() != IntType.INT1) { // 统一为i1
                eqExp = new IcmpInstr(IRManager.getInstance().genVRName(),
                        IcmpInstr.cmpOp.NE, eqExp, new Constant(0, IntType.INT32));
            }
            new BranchInstr(eqExp, trueBlock, falseBlock);
        }
    }

    public void genAndIR(BasicBlock trueBlock, BasicBlock falseBlock) {
        for (int i = 0; i < children.size(); i += 2) {
            if (i == children.size() - 1) {
                Value eqExp = children.get(i).generateIR();
                genBranchInstr(eqExp, trueBlock, falseBlock);
            }
            else {
                BasicBlock nextBlock = new BasicBlock("");
                Value eqExp = children.get(i).generateIR(); // 这里eqExp仍有可能是i32/i1
                genBranchInstr(eqExp, nextBlock, falseBlock);
                IRManager.getInstance().addAndSetCurBlock(nextBlock);
                IRManager.getInstance().resetBlockName(nextBlock);
            }
        }
    }
}
