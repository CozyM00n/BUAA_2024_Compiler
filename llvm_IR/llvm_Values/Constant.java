package llvm_IR.llvm_Values;

import llvm_IR.llvm_Types.IntType;
import llvm_IR.llvm_Types.LLVMType;

public class Constant extends Value {
    private int value;

    public Constant(int value, LLVMType llvmType) {
        super(String.valueOf(value), llvmType);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void switchToi8() {
        this.llvmType = IntType.INT8;
    }

    public void switchToi32() {
        this.llvmType = IntType.INT32;
    }

    public void switchType(LLVMType type) {
        this.llvmType = type;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
