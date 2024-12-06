package llvm_IR.Instr;

import BackEnd.Mips.ASM.BranchAsm;
import BackEnd.Mips.ASM.JumpAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.BasicBlock;
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

    @Override
    public void genAsm() {
        super.genAsm();
        // 将cond Value加载到寄存器k0
        Register condReg = Register.K0;
        Integer offset = MipsManager.getInstance().getOffsetOfValue(cond);
        new MemoryAsm(MemoryAsm.memOp.LW, condReg, offset, Register.SP);
        // 如果cond!=0, 跳转到trueBlock
        new BranchAsm(BranchAsm.brOp.BNE, condReg, Register.ZERO, trueBlock.getName());
        // cond==0, 直接jump至FalseBlock
        new JumpAsm(JumpAsm.JumpOp.J, falseBlock.getName());
    }
}
