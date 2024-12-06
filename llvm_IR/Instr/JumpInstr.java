package llvm_IR.Instr;

import BackEnd.Mips.ASM.JumpAsm;
import Enums.InstrType;
import llvm_IR.llvm_Types.VoidType;
import llvm_IR.BasicBlock;

public class JumpInstr extends Instr {
    // br label %7
    private BasicBlock toBlock;

    public JumpInstr(BasicBlock toBlock) {
        super("%br", VoidType.VOID, InstrType.JUMP_INSTR);
        this.toBlock = toBlock;  addOperand(toBlock);
    }

    public BasicBlock getToBlock() {
        return toBlock;
    }

    @Override
    public String toString() {
        return "br label %" + toBlock.getName();
    }

    @Override
    public void genAsm() {
        super.genAsm();
        new JumpAsm(JumpAsm.JumpOp.J, toBlock.getName());
    }
}
