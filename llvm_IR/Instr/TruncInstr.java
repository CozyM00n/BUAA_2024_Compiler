package llvm_IR.Instr;

import BackEnd.Mips.ASM.AluAsm;
import BackEnd.Mips.ASM.LiAsm;
import BackEnd.Mips.ASM.MemoryAsm;
import BackEnd.Mips.MipsManager;
import BackEnd.Mips.Register;
import Enums.InstrType;
import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Constant;
import llvm_IR.llvm_Values.Value;

public class TruncInstr extends Instr{
    // %5 = trunc i32 %3 to i8
    private Value fromValue;
    private LLVMType toType;

    public TruncInstr(String name, Value fromValue, LLVMType toType) {
        super(name, toType, InstrType.TRUNC_INSTR);
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
        return name + " = trunc "
                + fromValue.getLlvmType() + " " + fromValue.getName()
                + " to " + toType;
    }

    public void loadValueToReg(Value value, Register register) {
        if (value instanceof Constant) {
            new LiAsm(register, ((Constant) value).getValue());
        } else {
            // 从内存中将值加载到寄存器
            Integer offset = MipsManager.getInstance().getOffsetOfValue(value);
            assert offset != null;
            new MemoryAsm(MemoryAsm.memOp.LBU, register, offset, Register.SP);
        }
    }

    @Override
    public void genAsm() {
        super.genAsm();
        Register toReg = Register.K0;
        Register register = Register.K0;
        assert toType == IntType.INT8;
        loadValueToReg(fromValue, register);
        // int imme = toType == IntType.INT8 ? 0xff : 0x1;
        // new AluAsm(AluAsm.aluOp.AND, toReg, register, imme);
        int offset = MipsManager.getInstance().pushAndRetStackFrame(4);
        MipsManager.getInstance().addValueToStack(this, offset);
        new MemoryAsm(MemoryAsm.memOp.SW, toReg, offset, Register.SP);
    }
}
