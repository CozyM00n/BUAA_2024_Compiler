package llvm_IR.Instr;

import BackEnd.Mips.ASM.Comment;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.BasicBlock;
import llvm_IR.IRManager;
import llvm_IR.User;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class Instr extends User {
    private InstrType instrType;
    private BasicBlock parentBlock;

    public Instr(String name, LLVMType llvmType, InstrType instrType) {
        super(name, llvmType);
        this.instrType = instrType;
        this.parentBlock = null;
        IRManager.getInstance().addInstr(this);
    }

    public void setParentBlock(BasicBlock parentBlock) {
        this.parentBlock = parentBlock;
    }

    public BasicBlock getParentBlock() {
        return parentBlock;
    }

    public void loadValueToReg(Value value, Register register) {
        if (value instanceof Constant) {
            new LiAsm(register, ((Constant) value).getValue());
        } else {
            // 从内存中将值加载到寄存器
            Integer offset = MipsManager.getInstance().getOffsetOfValue(value);
            assert offset != null;
            new MemoryAsm(MemoryAsm.memOp.LW, register, offset, Register.SP);
        }
    }

    @Override
    public void genAsm() {
        new Comment(this.toString());
    }
}
