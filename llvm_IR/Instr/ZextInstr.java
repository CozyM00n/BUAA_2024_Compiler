package llvm_IR.Instr;

import BackEnd.Mips.MipsManager;
import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;
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
        Integer offset = MipsManager.getInstance().getOffsetOfValue(fromValue);
        MipsManager.getInstance().addValueToStack(this, offset);
    }
}
