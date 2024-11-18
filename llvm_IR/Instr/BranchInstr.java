package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.llvm_Values.BasicBlock;
import llvm_IR.llvm_Values.Value;

public class BranchInstr extends Instr {
    // br i1 %6, label %7, label %9
    private Value cond;
    private BasicBlock trueBlock;
    private BasicBlock falseBlock;

    public BranchInstr(Value cond, BasicBlock trueBlock, BasicBlock falseBlock) {
        super("%br", VoidType.VOID, InstrType.BR_INSTR);
        this.cond = cond;  addOperand(cond);
        this.trueBlock = trueBlock; addOperand(trueBlock);
        this.falseBlock = falseBlock; addOperand(falseBlock);
    }

    public Value getCond() {
        return cond;
    }

    public BasicBlock getTrueBlock() {
        return trueBlock;
    }

    public BasicBlock getFalseBlock() {
        return falseBlock;
    }

    @Override
    public String toString() {
        return "br " + cond.toTypeAndName() + ", "
                + "label %" + trueBlock.getName() + ", "
                + "label %" + falseBlock.getName();
    }
}
