package llvm_IR.Instr;

import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class ZextInstr extends Instr {
    // %7 = zext i8 %6 to i32
    private Value fromValue;
    private LLVMType toType;

    public ZextInstr(String name, Value fromValue, LLVMType toType) {
        super(name, toType, InstrType.ZEXT_INSTR);
        this.toType = toType;
        this.fromValue = fromValue;
        addOperand(fromValue);
    }

    public LLVMType getToType() {
        return toType;
    }

    public Value getFromValue() {
        return fromValue;
    }

    @Override
    public String toString() {
        return name + " = zext "
                + fromValue.getLlvmType() + " " + fromValue.getName()
                + " to " + toType;
    }

    @Override
    public void genAsm() {
        super.genAsm();
        // 如果这里 fromValue == Constant, 将其存入栈中
        Integer offset = MipsManager.getInstance().getOffsetOfValue(fromValue);
        if (offset == null) {
            offset = MipsManager.getInstance().pushAndRetStackFrame(4);
            Register tmpReg = Register.K0;
            new LiAsm(tmpReg, ((Constant)fromValue).getValue());
            new MemoryAsm(MemoryAsm.memOp.SW, tmpReg, offset, Register.SP);
        }
        MipsManager.getInstance().addValueToStack(this, offset);
    }
}
