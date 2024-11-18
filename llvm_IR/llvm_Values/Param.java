package llvm_IR.llvm_Values;

import llvm_IR.IRManager;
import llvm_IR.llvm_Types.LLVMType;
import llvm_IR.llvm_Values.Value;

public class Param extends Value {

    public Param(String name, LLVMType llvmType) {
        super(name, llvmType);
        IRManager.getInstance().addParam(this);
    }

    @Override
    public String toString() { // i32 %0
        return llvmType + " " + name;
    }
}
