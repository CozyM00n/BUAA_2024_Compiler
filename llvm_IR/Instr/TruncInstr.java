package llvm_IR.Instr;

import Enums.InstrType;
import llvm_IR.llvm_Types.LLVMType;
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
}
